package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.alignment.AlignMetadataResponse;
import org.metadatacenter.fairware.api.response.alignment.AlignmentReport;
import org.metadatacenter.fairware.api.response.evaluation.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReport;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.report.CompletenessReport;
import org.metadatacenter.fairware.api.response.report.EvaluationReportResponse;
import org.metadatacenter.fairware.api.response.report.FieldReport;
import org.metadatacenter.fairware.api.response.report.FieldsCompletenessReport;
import org.metadatacenter.fairware.api.response.report.RecordReport;
import org.metadatacenter.fairware.api.response.report.RecordsCompletenessReport;
import org.metadatacenter.fairware.api.response.search.SearchMetadataResponse;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.core.domain.CedarTemplate;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
import org.metadatacenter.fairware.core.services.evaluation.ControlledTermEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.ExtraFieldsEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.OptionalValuesEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.RequiredValuesEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.ValueTypeEvaluator;
import org.metadatacenter.fairware.core.util.FieldsAlignmentUtil;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.HungarianAlgorithm;
import org.metadatacenter.fairware.core.util.MetadataContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.shared.FieldSpecification;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.Metadata;
import org.metadatacenter.fairware.shared.MetadataSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

public class FairwareService {

  private static final Logger logger = LoggerFactory.getLogger(FairwareService.class);

  private final CoreConfig coreConfig;
  private final MetadataContentExtractor metadataContentExtractor;
  private final RequiredValuesEvaluator requiredValuesEvaluator;
  private final OptionalValuesEvaluator optionalValuesEvaluator;
  private final ExtraFieldsEvaluator extraFieldsEvaluator;
  private final ValueTypeEvaluator valueTypeEvaluator;
  private final ControlledTermEvaluator controlledTermEvaluator;

  public FairwareService(@Nonnull CoreConfig coreConfig,
                         @Nonnull MetadataContentExtractor metadataContentExtractor,
                         @Nonnull RequiredValuesEvaluator requiredValuesEvaluator,
                         @Nonnull OptionalValuesEvaluator optionalValuesEvaluator,
                         @Nonnull ExtraFieldsEvaluator extraFieldsEvaluator,
                         @Nonnull ValueTypeEvaluator valueTypeEvaluator,
                         @Nonnull ControlledTermEvaluator controlledTermEvaluator) {
    this.coreConfig = checkNotNull(coreConfig);
    this.metadataContentExtractor = checkNotNull(metadataContentExtractor);
    this.requiredValuesEvaluator = checkNotNull(requiredValuesEvaluator);
    this.optionalValuesEvaluator = checkNotNull(optionalValuesEvaluator);
    this.extraFieldsEvaluator = checkNotNull(extraFieldsEvaluator);
    this.valueTypeEvaluator = checkNotNull(valueTypeEvaluator);
    this.controlledTermEvaluator = checkNotNull(controlledTermEvaluator);
  }

  public SearchMetadataResponse searchMetadata(Metadata metadata) {
    return SearchMetadataResponse.create(metadata);
  }

  public AlignMetadataResponse alignMetadata(Metadata metadata, CedarTemplate template) throws IOException, HttpException {
    var fieldAlignments = findFieldAlignments(metadata, template);
    return AlignMetadataResponse.create(
        metadata,
        MetadataSpecification.create(
            template.getId(),
            template.getName(),
            template.getFields().stream()
                .map(field -> {
                  var valueField = field.valueField();
                  return FieldSpecification.create(
                      valueField.getIri(), field.getJsonPath(),
                      valueField.getPrefLabel().orElse(valueField.getName()),
                      valueField.getJsonValueType(), valueField.isRequired());
                })
                .collect(ImmutableList.toImmutableList())),
        AlignmentReport.create(fieldAlignments));
  }

  /**
   * This method finds an alignment between the fields in a metadata record and a given CEDAR template. It makes use
   * of the Hungarian algorithm to ensure that the alignment is optimal.
   */
  private ImmutableList<FieldAlignment> findFieldAlignments(Metadata metadata, CedarTemplate template) throws IOException {
    var metadataRecord = metadata.getMetadataRecord();
    var templateFields = template.getFields();
    // Extract metadata fields from the metadata record
    var metadataFields = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord, template.asMap());

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

  public EvaluateMetadataResponse evaluateMetadata(@Nonnull Metadata metadata,
                                                   @Nonnull CedarTemplate template) throws HttpException, IOException {
    var fieldAlignments = findFieldAlignments(metadata, template);
    return evaluateMetadata(metadata, template, fieldAlignments);
  }

  public EvaluateMetadataResponse evaluateMetadata(@Nonnull Metadata metadata,
                                                   @Nonnull CedarTemplate template,
                                                   @Nonnull ImmutableList<FieldAlignment> fieldAlignments) throws HttpException, IOException {
    var templateFields = template.getFields();
    var templateFieldMap = templateFields.<String, CedarTemplateField>stream()
        .collect(collectingAndThen(
            toMap(CedarTemplateField::getJsonPath, templateField -> templateField),
            ImmutableMap::copyOf));

    // Extract metadata fields from the metadata record and store them into a Map too (mfMap)
    var metadataFields = metadata.getFields();
    var metadataRecord = metadata.getMetadataRecord();
    var metadataFieldInfos = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord, template.asMap());
    var metadataFieldInfoMap = Maps.<String, MetadataFieldInfo>newHashMap();
    for (var metadataFieldInfo : metadataFieldInfos) {
      var key = GeneralUtil.generateFullPathDotNotation(metadataFieldInfo);
      metadataFieldInfoMap.put(key, metadataFieldInfo);
    }

    var reportItems = Lists.<EvaluationReportItem>newArrayList();

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

    var metadataId = metadata.getId();
    var metadataName = metadata.getName();

    var templateId = template.getId();
    var templateName = template.getName();
    var templateFieldPaths = templateFields.stream()
        .collect(collectingAndThen(
            toMap(CedarTemplateField::getJsonPath,
                field -> field.valueField().getJsonValueType()),
            ImmutableMap::copyOf));
    var requiredFields = templateFields.stream()
        .filter(templateField -> templateField.valueField().isRequired())
        .map(CedarTemplateField::getJsonPath)
        .collect(ImmutableList.toImmutableList());

    return EvaluateMetadataResponse.create(
        Metadata.create(metadataId, metadataName, metadataFields, metadataRecord),
        MetadataSpecification.create(templateId, templateName,
            templateFields.stream()
                .map(field -> {
                  var valueField = field.valueField();
                  return FieldSpecification.create(
                      valueField.getIri(), field.getJsonPath(),
                      valueField.getPrefLabel().orElse(valueField.getName()),
                      valueField.getJsonValueType(), valueField.isRequired());
                })
                .collect(ImmutableList.toImmutableList())),
        AlignmentReport.create(ImmutableList.copyOf(fieldAlignments)),
        EvaluationReport.create(ImmutableList.copyOf(reportItems), LocalDateTime.now()));
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
      int fieldsCount = metadataSpecification.getTemplateFields().size();
      var recordReport = RecordReport.create(recordEvaluationResult.getMetadataArtifact().getId(),
          recordEvaluationResult.getMetadataArtifact().getName(),
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
      var templateFieldNames = metadataSpecification.getTemplateFields().stream()
          .map(FieldSpecification::getName)
          .collect(ImmutableList.toImmutableList());
      for (var templateFieldName : templateFieldNames) {
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
