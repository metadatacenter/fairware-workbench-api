package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpClass;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.shared.IssueCategory;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.MetadataIssue;
import org.metadatacenter.fairware.shared.RepairAction;
import org.metadatacenter.fairware.shared.SuggestedOntologyTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * For metadata fields that have not been aligned to template fields, this evaluator determines whether they align with
 * terms from ontologies that could make the field names more FAIR.
 */
public class ExtraFieldsEvaluator implements IMetadataEvaluator {

  private static final Logger logger = LoggerFactory.getLogger(ExtraFieldsEvaluator.class);

  private BioportalService bioportalService;
  private CoreConfig coreConfig;

  public ExtraFieldsEvaluator(@Nonnull BioportalService bioportalService, @Nonnull CoreConfig coreConfig) {
    this.bioportalService = checkNotNull(bioportalService);
    this.coreConfig = checkNotNull(coreConfig);
  }

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldMap,
                                                     ImmutableMap<String, CedarTemplateField> templateFieldMap,
                                                     List<FieldAlignment> fieldAlignments)
      throws HttpException, IOException {

    var nonMatchedFields = getNonMatchedMetadataFields(fieldAlignments, metadataFieldMap);
    var reportItems = Lists.<EvaluationReportItem>newArrayList();

    // Use BioPortal to find top matching ontology terms
    for (var mf : nonMatchedFields) {
      var metadataFieldName = mf.getName();
      var metadataFieldValue = mf.getValue().get().toString();
      var results = bioportalService.search(metadataFieldName);
      var suggestedTerms = Lists.<SuggestedOntologyTerm>newArrayList();
      var suggestedFieldNames = Sets.<String>newHashSet(metadataFieldName.toLowerCase());
      for (var c : results.getCollection()) {
        var suggestedOntologyTerm = createSuggestedOntologyTerm(c);
        if (suggestedOntologyTerm.isPresent()) {
          var suggestedFieldName = suggestedOntologyTerm.get().getLabel();
          if (!suggestedFieldNames.contains(suggestedFieldName.toLowerCase())) {
            suggestedTerms.add(suggestedOntologyTerm.get());
            suggestedFieldNames.add(suggestedFieldName.toLowerCase());
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
                  metadataFieldValue),
              RepairAction.ofReplaceMetadataFieldWithStandardizedName(
                  ImmutableList.copyOf(suggestedTerms))));
    }
    return reportItems;
  }

  private ImmutableList<MetadataFieldInfo> getNonMatchedMetadataFields(List<FieldAlignment> fieldAlignments,
                                                                       Map<String, MetadataFieldInfo> mfMap) {
    // Create set with the paths of all the metadata fields that have been aligned to the template
    var pathsSet = Sets.<String>newHashSet();
    for (var fa : fieldAlignments) {
      pathsSet.add(fa.getMetadataFieldPath());
    }
    // Keep the fields that have not been aligned to the template
    var result = Lists.<MetadataFieldInfo>newArrayList();
    for (var mf : mfMap.entrySet()) {
      if (!pathsSet.contains(mf.getKey())) {
        result.add(mf.getValue());
      }
    }
    return ImmutableList.copyOf(result);
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
