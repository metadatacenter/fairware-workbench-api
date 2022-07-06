package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.action.SuggestedOntologyTerm;
import org.metadatacenter.fairware.api.response.issue.IssueCategory;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpClass;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpPagedResults;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * For metadata fields that have not been aligned to template fields, this evaluator determines whether they align with
 * terms from ontologies that could make the field names more FAIR.
 */
public class ExtraFieldsEvaluator implements IMetadataEvaluator {

  private static final Logger logger = LoggerFactory.getLogger(ExtraFieldsEvaluator.class);

  private BioportalService bioportalService;
  private CoreConfig coreConfig;

  public ExtraFieldsEvaluator(BioportalService bioportalService, CoreConfig coreConfig) {
    this.bioportalService = bioportalService;
    this.coreConfig = coreConfig;
  }

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldMap,
                                                     ImmutableMap<String, TemplateField> templateFieldMap,
                                                     List<FieldAlignment> fieldAlignments)
      throws HttpException, IOException {

    List<MetadataFieldInfo> nonMatchedFields = getNonMatchedMetadataFields(fieldAlignments, metadataFieldMap);
    List<EvaluationReportItem> reportItems = new ArrayList<>();

    // Use BioPortal to find top matching ontology terms
    for (MetadataFieldInfo mf : nonMatchedFields) {
      var metadataFieldName = mf.getName();
      var metadataFieldValue = mf.getValue().get();
      BpPagedResults<BpClass> results = bioportalService.search(metadataFieldName);
      List<SuggestedOntologyTerm> suggestedTerms = new ArrayList<>();
      for (BpClass c : results.getCollection()) {
        var suggestedOntologyTerm = createSuggestedOntologyTerm(c);
        if (suggestedOntologyTerm.isPresent()) {
          var suggestedFieldName = suggestedOntologyTerm.get().getLabel();
          if (metadataFieldName.equalsIgnoreCase(suggestedFieldName)) {
            suggestedTerms = Lists.<SuggestedOntologyTerm>newArrayList();
            break;
          } else {
            suggestedTerms.add(suggestedOntologyTerm.get());
          }
          if (suggestedTerms.size() == coreConfig.getTermSuggestionsListSize()) {
            break; // Exit when reaching default size
          }
        }
      }
      reportItems.add(
          EvaluationReportItem.create(
              MetadataIssue.create(
                  IssueCategory.FIELD_ERROR,
                  IssueType.FIELD_NOT_FOUND_IN_TEMPLATE,
                  GeneralUtil.generateFullPathDotNotation(mf),
                  metadataFieldValue.toString()),
              RepairAction.ofReplaceMetadataFieldWithStandardizedName(
                  ImmutableList.copyOf(suggestedTerms))));
    }
    return reportItems;
  }

  /**
   * Returns the metadata fields that have not been aligned to any template field
   *
   * @param fieldAlignments
   * @param mfMap
   * @return
   */
  private List<MetadataFieldInfo> getNonMatchedMetadataFields(List<FieldAlignment> fieldAlignments,
                                                              Map<String, MetadataFieldInfo> mfMap) {
    // Create set with the paths of all the metadata fields that have been aligned to the template
    Set<String> pathsSet = new HashSet<>();
    for (FieldAlignment fa : fieldAlignments) {
      pathsSet.add(fa.getMetadataFieldPath());
    }
    // Keep the fields that have not been aligned to the template
    List<MetadataFieldInfo> result = new ArrayList<>();
    for (Map.Entry<String, MetadataFieldInfo> mf : mfMap.entrySet()) {
      if (!pathsSet.contains(mf.getKey())) {
        result.add(mf.getValue());
      }
    }
    return result;
  }

  private Optional<SuggestedOntologyTerm> createSuggestedOntologyTerm(BpClass bioportalTerm) {
    if (bioportalTerm.getId() == null) {
      logger.warn("Couldn't retrieve class id", bioportalTerm);
      return Optional.empty();
    }
    if (bioportalTerm.getPrefLabel() == null) {
      logger.warn("Couldn't retrieve preferred label", bioportalTerm);
      return Optional.empty();
    }
    if (bioportalTerm.getLinks() == null || bioportalTerm.getLinks().getOntology() == null) {
      logger.warn("Couldn't retrieve ontology acronym", bioportalTerm);
      return Optional.empty();
    }
    return Optional.of(
        SuggestedOntologyTerm.create(
            bioportalTerm.getId(),
            bioportalTerm.getPrefLabel(),
            bioportalTerm.getLinks().getOntology()));
  }
}
