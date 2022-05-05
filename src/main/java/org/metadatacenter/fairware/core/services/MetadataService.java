package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.evaluationReport.CompletenessReport;
import org.metadatacenter.fairware.api.response.evaluationReport.EvaluationReportResponse;
import org.metadatacenter.fairware.api.response.evaluationReport.FieldReport;
import org.metadatacenter.fairware.api.response.evaluationReport.FieldsCompletenessReport;
import org.metadatacenter.fairware.api.response.evaluationReport.RecordReport;
import org.metadatacenter.fairware.api.response.evaluationReport.RecordsCompletenessReport;
import org.metadatacenter.fairware.api.response.issue.IssueLevel;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.search.SearchMetadataItem;
import org.metadatacenter.fairware.api.response.search.SearchMetadataResponse;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.core.services.citation.CitationService;
import org.metadatacenter.fairware.core.services.evaluation.OptionalValuesEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.RequiredValuesEvaluator;
import org.metadatacenter.fairware.core.util.CedarUtil;
import org.metadatacenter.fairware.core.util.FieldsAlignmentUtil;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.HungarianAlgorithm;
import org.metadatacenter.fairware.core.util.MetadataContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateNodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetadataService implements IMetadataService {

  private static final Logger logger = LoggerFactory.getLogger(IMetadataService.class);

  private final CedarService cedarService;
  private final BioportalService bioportalService;
  private final CitationService citationService;
  private final CoreConfig coreConfig;
  private final BioportalConfig bioportalConfig;
  private final MetadataContentExtractor metadataContentExtractor;

  public MetadataService(CedarService cedarService, BioportalService bioportalService, CitationService citationService,
                         CoreConfig coreConfig, BioportalConfig bioportalConfig, MetadataContentExtractor metadataContentExtractor) {
    this.cedarService = cedarService;
    this.bioportalService = bioportalService;
    this.citationService = citationService;
    this.coreConfig = coreConfig;
    this.bioportalConfig = bioportalConfig;
    this.metadataContentExtractor = metadataContentExtractor;
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
  @Override
  public List<FieldAlignment> alignMetadata(String templateId,
                                            Map<String, Object> metadataRecord) throws IOException, HttpException {
    Map<String, Object> template = cedarService.findTemplate(templateId);

    // Extract template nodes from the template. Keep fields only
    List<TemplateNodeInfo> templateFields = CedarTemplateContentExtractor.getTemplateNodes(template)
        .stream()
        .filter(TemplateNodeInfo::isTemplateFieldNode)
        .collect(Collectors.toList());
    // Extract metadata fields from the metadata record
    List<MetadataFieldInfo> metadataFields = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord, template);

    // Find alignments between metadata fields and template fields
    int maxDimension = Math.max(metadataFields.size(), templateFields.size()); // Relevant when the matrix is non-square
    double[][] similarityMatrix = new double[maxDimension][maxDimension];
    for (int i = 0; i < maxDimension; i++) {
      for (int j = 0; j < maxDimension; j++) {
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

    HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(FieldsAlignmentUtil.translateMatrix(similarityMatrix));
    int[] optimalAlignment = hungarianAlgorithm.execute();

    return FieldsAlignmentUtil.generateFieldAlignments(
        metadataFields,
        templateFields,
        similarityMatrix,
        optimalAlignment);
  }

  @Override
  public EvaluateMetadataResponse evaluateMetadata(String metadataRecordId,
                                                   String templateId,
                                                   Map<String, Object> metadataRecord,
                                                   List<FieldAlignment> fieldAlignments) throws HttpException, IOException {
    List<EvaluationReportItem> reportItems =
        evaluateMetadata(templateId, metadataRecord, fieldAlignments);
    // Count errors and warnings
    int warningsCount = 0;
    int errorsCount = 0;
    for (EvaluationReportItem item : reportItems) {
      if (item.getIssue().getIssueLevel().equals(IssueLevel.WARNING)) {
        warningsCount++;
      } else if (item.getIssue().getIssueLevel().equals(IssueLevel.ERROR)) {
        errorsCount++;
      } else {
        throw new InvalidParameterException("Invalid issue type");
      }
    }

    // The following lines are doing work that is already done by "evaluateMetadata". They could be optimized
    Map<String, Object> template = cedarService.findTemplate(templateId);
    List<TemplateNodeInfo> templateFields = CedarTemplateContentExtractor.getTemplateNodes(template)
        .stream()
        .filter(TemplateNodeInfo::isTemplateFieldNode)
        .collect(Collectors.toList());
    List<String> metadataFieldsPaths = new ArrayList<>();
    for (TemplateNodeInfo tf : templateFields) {
      metadataFieldsPaths.add(GeneralUtil.generateFullPathDotNotation(tf));
    }

    String metadataRecordName = null;
    if (metadataRecord.containsKey(CedarModelConstants.SCHEMA_ORG_NAME)) {
      metadataRecordName = metadataRecord.get(CedarModelConstants.SCHEMA_ORG_NAME).toString();
    }
    String templateName = cedarService.findTemplate(templateId).get(CedarModelConstants.SCHEMA_ORG_NAME).toString();

    return new EvaluateMetadataResponse(metadataRecordId, metadataRecordName,
        templateId, templateName, metadataRecord, metadataFieldsPaths, reportItems.size(), warningsCount, errorsCount,
        reportItems, LocalDateTime.now());
  }

  /**
   * Evaluates an input metadata record against a given metadata template
   *
   * @param templateId
   * @param metadataRecord
   * @param fieldAlignments
   * @return
   */
  @Override
  public List<EvaluationReportItem> evaluateMetadata(String templateId,
                                                     Map<String, Object> metadataRecord,
                                                     List<FieldAlignment> fieldAlignments) throws HttpException, IOException {

    // Extract nodes from the template (limited to fields) and store them into a HashMap (tfMap)
    Map<String, Object> template = cedarService.findTemplate(templateId);
    List<TemplateNodeInfo> templateFields = CedarTemplateContentExtractor.getTemplateNodes(template)
        .stream()
        .filter(TemplateNodeInfo::isTemplateFieldNode)
        .collect(Collectors.toList());
    Map<String, TemplateNodeInfo> tfMap = new HashMap<>();
    for (TemplateNodeInfo tf : templateFields) {
      tfMap.put(GeneralUtil.generateFullPathDotNotation(tf), tf);
    }

    // Extract metadata fields from the metadata record and store them into a Map too (mfMap)
    List<MetadataFieldInfo> metadataFieldsInfo = metadataContentExtractor.extractMetadataFieldsInfo(metadataRecord, template);
    Map<String, MetadataFieldInfo> mfMap = new HashMap<>();
    for (MetadataFieldInfo mf : metadataFieldsInfo) {
      mfMap.put(GeneralUtil.generateFullPathDotNotation(mf), mf);
    }

    // Use the alignments to apply the template constraints against each metadata field
    List<EvaluationReportItem> reportItems = new ArrayList<>();
    // Use an automatically-generated alignment if one has not been provided
    if (fieldAlignments.size() == 0) {
      fieldAlignments.addAll(alignMetadata(templateId, metadataRecord));
    }
    // Check missing required values
    RequiredValuesEvaluator requiredValuesEv = new RequiredValuesEvaluator();
    reportItems.addAll(requiredValuesEv.evaluateMetadata(mfMap, tfMap, fieldAlignments));

    // Check missing optional values
    OptionalValuesEvaluator optionalValuesEv = new OptionalValuesEvaluator();
    reportItems.addAll(optionalValuesEv.evaluateMetadata(mfMap, tfMap, fieldAlignments));

    // 2. Check missing template fields (find ontology terms for the extra metadata fields)
    // ExtraFieldsEvaluator missingTemplateFieldsEv = new ExtraFieldsEvaluator(bioportalService, coreConfig, bioportalConfig);
    // reportItems.addAll(missingTemplateFieldsEv.evaluateMetadata(mfMap, tfMap, fieldAlignments));
    // Check ...

    return reportItems;
  }

  /**
   * Retrieves the metadata associated to a list of Digital Object Identifiers (DOIs)
   *
   * @param uris
   * @return
   */
  @Override
  public SearchMetadataResponse searchMetadata(List<String> uris) throws IOException, HttpException {
    List<SearchMetadataItem> records = new ArrayList<>();
    for (String uri : uris) {
      if (CedarUtil.isCedarTemplateInstanceId(uri)) {
        ImmutableMap<String, Object> templateInstance = cedarService.findTemplateInstance(uri);
        if (templateInstance != null) {
          records.add(cedarService.toMetadataItem(templateInstance));
        }
      } else {
        records.add(citationService.searchMetadata(uri));
      }
    }
    return SearchMetadataResponse.create(records.size(), ImmutableList.copyOf(records));
  }

  @Override
  public EvaluationReportResponse generateEvaluationReport(List<EvaluateMetadataResponse> evaluationResults) {

    // Records completeness report
    RecordsCompletenessReport recordsCompletenessReport = new RecordsCompletenessReport();
    List<RecordReport> recordReports = new ArrayList<>();
    int completeRecordsCount = 0;
    int recordsWithMissingRequiredCount = 0;
    int recordsWithMissingOptionalCount = 0;
    for (EvaluateMetadataResponse recordEvaluationResult : evaluationResults) {
      int missingRequiredCount = 0;
      int missingOptionalCount = 0;
      for (EvaluationReportItem fieldEvaluationResult : recordEvaluationResult.getItems()) {
        if (fieldEvaluationResult.getIssue().getIssueType().equals(IssueType.MISSING_REQUIRED_VALUE)) {
          missingRequiredCount++;
        } else if (fieldEvaluationResult.getIssue().getIssueType().equals(IssueType.MISSING_OPTIONAL_VALUE)) {
          missingOptionalCount++;
        }
      }
      int fieldsCount = recordEvaluationResult.getMetadataFieldPaths().size();
      RecordReport recordReport = new RecordReport();
      recordReport.setMetadataRecordId(recordEvaluationResult.getMetadataRecordId());
      recordReport.setMetadataRecordName(recordEvaluationResult.getMetadataRecordName());
      recordReport.setTemplateId(recordEvaluationResult.getTemplateId());
      recordReport.setTemplateName(recordEvaluationResult.getTemplateName());
      recordReport.setFieldsCount(fieldsCount);
      recordReport.setCompleteCount(fieldsCount - missingRequiredCount - missingOptionalCount);
      recordReport.setMissingRequiredValuesCount(missingRequiredCount);
      recordReport.setMissingOptionalValuesCount(missingOptionalCount);
      recordReports.add(recordReport);
      if (missingRequiredCount == 0 && missingOptionalCount == 0) {
        completeRecordsCount++;
      } else if (missingRequiredCount > 0) {
        recordsWithMissingRequiredCount++;
      } else {
        recordsWithMissingOptionalCount++;
      }
    }
    recordsCompletenessReport.setRecordsCount(recordReports.size());
    recordsCompletenessReport.setCompleteRecordsCount(completeRecordsCount);
    recordsCompletenessReport.setRecordsWithMissingRequiredValuesCount(recordsWithMissingRequiredCount);
    recordsCompletenessReport.setRecordsWithMissingOptionalValuesCount(recordsWithMissingOptionalCount);
    // Sort recordReports by number of missing values
    Collections.sort(recordReports, (r1, r2) -> {
      if (r1.getMissingRequiredValuesCount() == r2.getMissingRequiredValuesCount()) {
        return r2.getMissingOptionalValuesCount() - r1.getMissingOptionalValuesCount();
      } else {
        return r2.getMissingRequiredValuesCount() - r1.getMissingRequiredValuesCount();
      }
    });
    recordsCompletenessReport.setItems(recordReports);

    // Fields completeness report
    FieldsCompletenessReport fieldsCompletenessReport = new FieldsCompletenessReport();
    Map<String, FieldReport> fieldReportMap = new HashMap<>();
    for (EvaluateMetadataResponse recordEvaluationResult : evaluationResults) {
      String templateId = recordEvaluationResult.getTemplateId();
      // Add to the map all the fields, and assume that they don't have any missing values
      for (String fieldPath : recordEvaluationResult.getMetadataFieldPaths()) {
        String key = templateId + fieldPath;
        if (!fieldReportMap.containsKey(key)) {
          fieldReportMap.put(key, new FieldReport());
          fieldReportMap.get(key).setMetadataFieldPath(fieldPath);
          fieldReportMap.get(key).setTemplateId(templateId);
          fieldReportMap.get(key).setTemplateName(recordEvaluationResult.getTemplateName());
          fieldReportMap.get(key).setCompleteCount(0);
        }
        fieldReportMap.get(key).setFieldsCount(fieldReportMap.get(key).getFieldsCount() + 1);
        // Initially assume that they don't have any missing values
        fieldReportMap.get(key).setCompleteCount(fieldReportMap.get(key).getCompleteCount() + 1);
      }
      // Add to the map the info about the fields with missing values
      for (EvaluationReportItem fieldEvaluationResult : recordEvaluationResult.getItems()) {
        String fieldPath = fieldEvaluationResult.getMetadataFieldPath();
        String key = templateId + fieldPath;
        if (fieldEvaluationResult.getIssue().getIssueType().equals(IssueType.MISSING_REQUIRED_VALUE)) {
          int missingCount = fieldReportMap.get(key).getMissingRequiredValuesCount();
          fieldReportMap.get(key).setMissingRequiredValuesCount(missingCount + 1);
          int completeCount = fieldReportMap.get(key).getCompleteCount();
          fieldReportMap.get(key).setCompleteCount(completeCount - 1);
        } else if (fieldEvaluationResult.getIssue().getIssueType().equals(IssueType.MISSING_OPTIONAL_VALUE)) {
          int missingCount = fieldReportMap.get(key).getMissingOptionalValuesCount();
          fieldReportMap.get(key).setMissingOptionalValuesCount(missingCount + 1);
          int completeCount = fieldReportMap.get(key).getCompleteCount();
          fieldReportMap.get(key).setCompleteCount(completeCount - 1);
        }
      }
    }

    int completeFieldsCount = 0;
    int fieldsWithMissingRequiredCount = 0;
    int fieldsWithMissingOptionalCount = 0;
    List<FieldReport> fieldReports = new ArrayList<>(fieldReportMap.values());
    for (FieldReport fr : fieldReports) {
      if (fr.getMissingRequiredValuesCount() > 0) {
        fieldsWithMissingRequiredCount++;
      } else if (fr.getMissingOptionalValuesCount() > 0) {
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

    fieldsCompletenessReport.setCompleteFieldsCount(completeFieldsCount);
    fieldsCompletenessReport.setFieldsWithMissingRequiredValuesCount(fieldsWithMissingRequiredCount);
    fieldsCompletenessReport.setFieldsWithMissingOptionalValuesCount(fieldsWithMissingOptionalCount);
    fieldsCompletenessReport.setItems(fieldReports);

    // Generate completeness report
    CompletenessReport completenessReport = new CompletenessReport();
    completenessReport.setRecordsReport(recordsCompletenessReport);
    completenessReport.setFieldsReport(fieldsCompletenessReport);

    // Generate evaluation report
    EvaluationReportResponse reportResponse = new EvaluationReportResponse();
    reportResponse.setCompletenessReport(completenessReport);
    return reportResponse;
  }
}
