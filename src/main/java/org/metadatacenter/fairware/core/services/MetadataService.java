package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluationReport;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.MetadataSpecification;
import org.metadatacenter.fairware.api.response.evaluationReport.CompletenessReport;
import org.metadatacenter.fairware.api.response.evaluationReport.EvaluationReportResponse;
import org.metadatacenter.fairware.api.response.evaluationReport.FieldReport;
import org.metadatacenter.fairware.api.response.evaluationReport.FieldsCompletenessReport;
import org.metadatacenter.fairware.api.response.evaluationReport.RecordReport;
import org.metadatacenter.fairware.api.response.evaluationReport.RecordsCompletenessReport;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.search.BatchSearchMetadataResponse;
import org.metadatacenter.fairware.api.response.search.SearchMetadataResponse;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.core.services.citation.CitationService;
import org.metadatacenter.fairware.core.services.evaluation.ControlledTermEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.ExtraFieldsEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.OptionalValuesEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.RequiredValuesEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.ValueTypeEvaluator;
import org.metadatacenter.fairware.core.util.CedarUtil;
import org.metadatacenter.fairware.core.util.FieldsAlignmentUtil;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.HungarianAlgorithm;
import org.metadatacenter.fairware.core.util.MetadataContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

public class MetadataService {

  private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);

  private final CedarService cedarService;
  private final BioportalService bioportalService;
  private final CitationService citationService;
  private final CoreConfig coreConfig;
  private final BioportalConfig bioportalConfig;
  private final MetadataContentExtractor metadataContentExtractor;
  private final RequiredValuesEvaluator requiredValuesEvaluator;
  private final OptionalValuesEvaluator optionalValuesEvaluator;
  private final ExtraFieldsEvaluator extraFieldsEvaluator;
  private final ValueTypeEvaluator valueTypeEvaluator;
  private final ControlledTermEvaluator controlledTermEvaluator;

  public MetadataService(@Nonnull CedarService cedarService,
                         @Nonnull BioportalService bioportalService,
                         @Nonnull CitationService citationService,
                         @Nonnull CoreConfig coreConfig,
                         @Nonnull BioportalConfig bioportalConfig,
                         @Nonnull MetadataContentExtractor metadataContentExtractor,
                         @Nonnull RequiredValuesEvaluator requiredValuesEvaluator,
                         @Nonnull OptionalValuesEvaluator optionalValuesEvaluator,
                         @Nonnull ExtraFieldsEvaluator extraFieldsEvaluator,
                         @Nonnull ValueTypeEvaluator valueTypeEvaluator,
                         @Nonnull ControlledTermEvaluator controlledTermEvaluator) {
    this.cedarService = checkNotNull(cedarService);
    this.bioportalService = checkNotNull(bioportalService);
    this.citationService = checkNotNull(citationService);
    this.coreConfig = checkNotNull(coreConfig);
    this.bioportalConfig = checkNotNull(bioportalConfig);
    this.metadataContentExtractor = checkNotNull(metadataContentExtractor);
    this.requiredValuesEvaluator = checkNotNull(requiredValuesEvaluator);
    this.optionalValuesEvaluator = checkNotNull(optionalValuesEvaluator);
    this.extraFieldsEvaluator = checkNotNull(extraFieldsEvaluator);
    this.valueTypeEvaluator = checkNotNull(valueTypeEvaluator);
    this.controlledTermEvaluator = checkNotNull(controlledTermEvaluator);
  }

  /**
   * This method finds an alignment between the fields in a metadata record and a given CEDAR template. It makes use
   * of the Hungarian algorithm to ensure that the alignment is optimal.
   *
   * @param templateId     CEDAR template identifier
   * @param metadataRecord Input metadata record
   * @return A list of alignments between fields in the metadata record and fields in the given template
   * @throws IOException
   * @throws HttpException
   */
  public ImmutableList<FieldAlignment> alignMetadata(String templateId, ImmutableMap<String, Object> metadataRecord)
      throws IOException, HttpException {
    var template = cedarService.findTemplate(templateId);

    // Extract template nodes from the template. Keep fields only
    var templateFields = cedarService.retrieveCedarTemplate(templateId).getTemplateFields();
    // Extract metadata fields from the metadata record
    var metadataFields = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord, template);

    // Find alignments between metadata fields and template fields
    var maxDimension = Math.max(metadataFields.size(), templateFields.size()); // Relevant when the matrix is non-square
    var similarityMatrix = new double[maxDimension][maxDimension];
    for (var i = 0; i < maxDimension; i++) {
      for (var j = 0; j < maxDimension; j++) {
        if (i >= metadataFields.size() || j >= templateFields.size()) {
          similarityMatrix[i][j] = -1;
        } else {
          // Calculate and save similarity
          similarityMatrix[i][j] = FieldsAlignmentUtil.calculateSimilarity(
              metadataFields.get(i),
              templateFields.get(j),
              coreConfig.getNameSimilarityWeight(),
              coreConfig.getPathSimilarityWeight());
        }
      }
    }

    // Apply a similarity filter to discard any correspondences under a threshold
    similarityMatrix = FieldsAlignmentUtil.filterBySimilarity(similarityMatrix, coreConfig.getSimilarityThreshold());

    var hungarianAlgorithm = new HungarianAlgorithm(FieldsAlignmentUtil.translateMatrix(similarityMatrix));
    var optimalAlignment = hungarianAlgorithm.execute();

    var fieldAlignments =
        FieldsAlignmentUtil.generateFieldAlignments(metadataFields,
            templateFields,
            similarityMatrix,
            optimalAlignment);
    return ImmutableList.copyOf(fieldAlignments);
  }

  public EvaluateMetadataResponse evaluateMetadata(@Nonnull Optional<String> metadataRecordId,
                                                   @Nonnull ImmutableMap<String, Object> metadataRecord,
                                                   @Nonnull String templateId,
                                                   @Nonnull ImmutableList<FieldAlignment> fieldAlignments) throws HttpException, IOException {
    // Extract nodes from the template (limited to fields) and store them into a HashMap (tfMap)
    var template = cedarService.findTemplate(templateId);
    var templateFields = cedarService.retrieveCedarTemplate(templateId).getTemplateFields();
    var templateFieldMap = templateFields.<String, TemplateField>stream()
        .collect(collectingAndThen(
            toMap(TemplateField::getJsonPath, templateField -> templateField),
            ImmutableMap::copyOf));

    // Extract metadata fields from the metadata record and store them into a Map too (mfMap)
    var metadataFieldInfos = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord, template);
    var metadataFieldInfoMap = Maps.<String, MetadataFieldInfo>newHashMap();
    for (var metadataFieldInfo : metadataFieldInfos) {
      var key = GeneralUtil.generateFullPathDotNotation(metadataFieldInfo);
      metadataFieldInfoMap.put(key, metadataFieldInfo);
    }

    // Use the alignments to apply the template constraints against each metadata field
    var reportItems = Lists.<EvaluationReportItem>newArrayList();
    // Use an automatically-generated alignment if one has not been provided
    if (fieldAlignments.size() == 0) {
      fieldAlignments = alignMetadata(templateId, metadataRecord);
    }

    // Check missing required values
    var requiredValuesReports = requiredValuesEvaluator.evaluateMetadata(
        metadataFieldInfoMap,
        templateFieldMap,
        fieldAlignments);
    reportItems.addAll(requiredValuesReports);

    // Check missing optional values
    var optionalValuesReports = optionalValuesEvaluator.evaluateMetadata(
        metadataFieldInfoMap,
        templateFieldMap,
        fieldAlignments);
    reportItems.addAll(optionalValuesReports);

    // Check missing template fields
    var missingFieldsReports = extraFieldsEvaluator.evaluateMetadata(
        metadataFieldInfoMap,
        templateFieldMap,
        fieldAlignments);
    reportItems.addAll(missingFieldsReports);

    // Check value types
    var valueTypeReports = valueTypeEvaluator.evaluateMetadata(
        metadataFieldInfoMap,
        templateFieldMap,
        fieldAlignments);
    reportItems.addAll(valueTypeReports);

    // Check controlled terms
    var controlledTermReports = controlledTermEvaluator.evaluateMetadata(
        metadataFieldInfoMap,
        templateFieldMap,
        fieldAlignments);
    reportItems.addAll(controlledTermReports);

    var metadataRecordName = Optional.<String>empty();
    if (metadataRecord.containsKey(CedarModelConstants.SCHEMA_ORG_NAME)) {
      metadataRecordName = Optional.of(metadataRecord.get(CedarModelConstants.SCHEMA_ORG_NAME).toString());
    }
    var templateName = cedarService.findTemplate(templateId).get(CedarModelConstants.SCHEMA_ORG_NAME).toString();
    var templateFieldPaths = templateFields.stream()
        .collect(collectingAndThen(
            toMap(TemplateField::getJsonPath,
                field -> field.valueField().getJsonValueType()),
            ImmutableMap::copyOf));

    return EvaluateMetadataResponse.create(metadataRecordId,
        metadataRecordName,
        metadataRecord,
        MetadataSpecification.create(templateId, templateName, templateFieldPaths),
        EvaluationReport.create(ImmutableList.copyOf(reportItems), LocalDateTime.now()));
  }

  /**
   * Retrieves in batch the metadata associated to the metadata identifier
   *
   * @param metadataRecordIds a list of metadata identifiers
   * @return A batch search metadata response object
   */
  public BatchSearchMetadataResponse searchMetadata(ImmutableList<String> metadataRecordIds) throws IOException {
    var metadataRecords = Lists.<ImmutableMap<String, Object>>newArrayList();
    for (var metadataRecordId : metadataRecordIds) {
      var metadataRecord = ImmutableMap.<String, Object>of();
      if (CedarUtil.isCedarTemplateInstanceId(metadataRecordId)) {
        metadataRecord = cedarService.retrieveMetadataById(metadataRecordId);
      } else {
        metadataRecord = citationService.retrieveMetadataById(metadataRecordId);
      }
      metadataRecords.add(metadataRecord);
    }
    return BatchSearchMetadataResponse.create(ImmutableList.copyOf(metadataRecords));
  }

  /**
   * Retrieves the metadata associated to the metadata identifier
   *
   * @param metadataRecordId a metadata identifier
   * @return A search metadata response object
   */
  public SearchMetadataResponse searchMetadata(String metadataRecordId) throws IOException {
    var metadataRecord = ImmutableMap.<String, Object>of();
    if (CedarUtil.isCedarTemplateInstanceId(metadataRecordId)) {
      metadataRecord = cedarService.retrieveMetadataById(metadataRecordId);
    } else {
      metadataRecord = citationService.retrieveMetadataById(metadataRecordId);
    }
    return SearchMetadataResponse.create(metadataRecord);
  }

  public EvaluationReportResponse generateEvaluationReport(ImmutableList<EvaluateMetadataResponse> evaluationResults) {
    var recordReports = Lists.<RecordReport>newArrayList();
    int completeRecordsCount = 0;
    int recordsWithMissingRequiredCount = 0;
    int recordsWithMissingOptionalCount = 0;
    for (var recordEvaluationResult : evaluationResults) {
      int missingRequiredCount = 0;
      int missingOptionalCount = 0;
      for (var fieldEvaluationResult : recordEvaluationResult.getEvaluationReport().getAllIssueReports()) {
        var issueType = fieldEvaluationResult.getMetadataIssue().getIssueType();
        if (issueType.equals(IssueType.MISSING_REQUIRED_VALUE)) {
          missingRequiredCount++;
        } else if (issueType.equals(IssueType.MISSING_OPTIONAL_VALUE)) {
          missingOptionalCount++;
        }
      }

      var metadataSpecification = recordEvaluationResult.getMetadataSpecification();
      int fieldsCount = metadataSpecification.getTemplateFieldNames().size();
      var recordReport = RecordReport.create(recordEvaluationResult.getMetadataRecordId(),
          recordEvaluationResult.getMetadataRecordName(),
          metadataSpecification.getTemplateId(),
          metadataSpecification.getTemplateName(),
          fieldsCount,
          missingRequiredCount,
          missingOptionalCount);
      recordReports.add(recordReport);

      if (missingRequiredCount == 0 && missingOptionalCount == 0) {
        completeRecordsCount++;
      } else if (missingRequiredCount > 0) {
        recordsWithMissingRequiredCount++;
      } else {
        recordsWithMissingOptionalCount++;
      }
    }

    // Sort recordReports by number of missing values
    Collections.sort(recordReports, (r1, r2) -> {
      if (r1.getMissingRequiredValuesCount() == r2.getMissingRequiredValuesCount()) {
        return r2.getMissingOptionalValuesCount() - r1.getMissingOptionalValuesCount();
      } else {
        return r2.getMissingRequiredValuesCount() - r1.getMissingRequiredValuesCount();
      }
    });
    var recordsCompletenessReport = RecordsCompletenessReport.create(recordReports.size(),
        completeRecordsCount,
        recordsWithMissingRequiredCount,
        recordsWithMissingOptionalCount,
        ImmutableList.copyOf(recordReports));

    // Fields completeness report
    var fieldReportMap = Maps.<String, FieldReport>newHashMap();
    for (var recordEvaluationResult : evaluationResults) {
      var metadataSpecification = recordEvaluationResult.getMetadataSpecification();
      var templateId = metadataSpecification.getTemplateId();
      // Add to the map all the fields, and assume that they don't have any missing values
      for (var templateFieldName : metadataSpecification.getTemplateFieldNames().keySet()) {
        var key = templateId + "#" + templateFieldName;
        if (!fieldReportMap.containsKey(key)) {
          fieldReportMap.put(key, FieldReport.create(templateFieldName,
              templateId,
              metadataSpecification.getTemplateName(),
              0,
              0,
              0,
              0));
        }
        var existingFieldReportMap = fieldReportMap.get(key);
        fieldReportMap.put(key, existingFieldReportMap.incrementFieldsCount());
        fieldReportMap.put(key, existingFieldReportMap.incrementCompleteCount());
      }
      // Add to the map the info about the fields with missing values
      for (var fieldEvaluationResult : recordEvaluationResult.getEvaluationReport().getAllIssueReports()) {
        var fieldPath = fieldEvaluationResult.getMetadataIssue().getIssueLocation();
        var key = templateId + "#" + fieldPath;
        var issueType = fieldEvaluationResult.getMetadataIssue().getIssueType();
        if (issueType.equals(IssueType.MISSING_REQUIRED_VALUE)) {
          var existingFieldReport = fieldReportMap.get(key);
          fieldReportMap.put(key, existingFieldReport.incrementMissingRequiredValuesCount());
          fieldReportMap.put(key, existingFieldReport.decrementCompleteCount());
        } else if (issueType.equals(IssueType.MISSING_OPTIONAL_VALUE)) {
          var existingFieldReport = fieldReportMap.get(key);
          fieldReportMap.put(key, existingFieldReport.incrementMissingOptionalValuesCount());
          fieldReportMap.put(key, existingFieldReport.decrementCompleteCount());
        }
      }
    }

    int completeFieldsCount = 0;
    int fieldsWithMissingRequiredCount = 0;
    int fieldsWithMissingOptionalCount = 0;
    var fieldReports = Lists.newArrayList(fieldReportMap.values());
    for (var fieldReport : fieldReports) {
      if (fieldReport.getMissingRequiredValuesCount() > 0) {
        fieldsWithMissingRequiredCount++;
      } else if (fieldReport.getMissingOptionalValuesCount() > 0) {
        fieldsWithMissingOptionalCount++;
      } else {
        completeFieldsCount++;
      }
    }

    // Sort fieldReports by number of missing values
    Collections.sort(fieldReports, (r1, r2) -> {
      if (r1.getMissingRequiredValuesCount() == r2.getMissingRequiredValuesCount()) {
        return r2.getMissingOptionalValuesCount() - r1.getMissingOptionalValuesCount();
      } else {
        return r2.getMissingRequiredValuesCount() - r1.getMissingRequiredValuesCount();
      }
    });
    var fieldsCompletenessReport = FieldsCompletenessReport.create(completeFieldsCount,
        fieldsWithMissingRequiredCount,
        fieldsWithMissingOptionalCount,
        ImmutableList.copyOf(fieldReports));

    // Generate completeness report
    var completenessReport = CompletenessReport.create(recordsCompletenessReport,
        fieldsCompletenessReport);

    // Generate evaluation report
    var reportResponse = EvaluationReportResponse.create(completenessReport);
    return reportResponse;
  }
}
