package org.metadatacenter.fairware.core.services.evaluation;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.ReplaceFieldNameWithOntologyTermAction;
import org.metadatacenter.fairware.api.response.action.SuggestedOntologyTerm;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;
import org.metadatacenter.fairware.core.services.IMetadataService;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpClass;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpPagedResults;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * For metadata fields that have not been aligned to template fields, this evaluator determines whether they align with
 * terms from ontologies that could make the field names more FAIR.
 */
public class ExtraFieldsEvaluator implements IMetadataEvaluator {

  private static final Logger logger = LoggerFactory.getLogger(ExtraFieldsEvaluator.class);

  final MetadataIssue issue = new MetadataIssue(IssueType.FIELD_NOT_FOUND_IN_TEMPLATE);
  private BioportalService bioportalService;
  private CoreConfig coreConfig;
  private BioportalConfig bioportalConfig;

  private ExtraFieldsEvaluator() {
    // disabled constructor, never called
  }

  public ExtraFieldsEvaluator(BioportalService bioportalService, CoreConfig coreConfig, BioportalConfig bioportalConfig) {
    this.bioportalService = bioportalService;
    this.coreConfig = coreConfig;
    this.bioportalConfig = bioportalConfig;
  }

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> mfMap,
                                                     Map<String, TemplateNodeInfo> tfMap,
                                                     List<FieldAlignment> fieldAlignments)
      throws HttpException, IOException {

    List<MetadataFieldInfo> nonMatchedFields = getNonMatchedMetadataFields(fieldAlignments, mfMap);
    List<EvaluationReportItem> reportItems = new ArrayList<>();

    // Use BioPortal to find top matching ontology terms
    for (MetadataFieldInfo mf : nonMatchedFields) {

      BpPagedResults<BpClass> results = bioportalService.search(mf.getName());
      ReplaceFieldNameWithOntologyTermAction repairAction = null;
      if (results.getCollection().size() > 0) {
        List<SuggestedOntologyTerm> suggestedTerms = new ArrayList<>();
        for (BpClass c : results.getCollection()) {
          if (bpClassToSuggestedTerm(c).isPresent()) {
            suggestedTerms.add(bpClassToSuggestedTerm(c).get());
          }
          if (suggestedTerms.size() == coreConfig.getTermSuggestionsListSize()) {
            break; // Exit when reaching default size
          }
        }
        repairAction = new ReplaceFieldNameWithOntologyTermAction(suggestedTerms.get(0).getLabel(), suggestedTerms);
      }
      reportItems.add(new EvaluationReportItem(GeneralUtil.generateFullPathDotNotation(mf), issue, repairAction));
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
  private List<MetadataFieldInfo> getNonMatchedMetadataFields(List<FieldAlignment> fieldAlignments, Map<String,
      MetadataFieldInfo> mfMap) {
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

  private Optional<SuggestedOntologyTerm> bpClassToSuggestedTerm(BpClass c) {
    if (c.getId() == null) {
      logger.warn("Couldn't retrieve class id", c);
      Optional.empty();
    }
    if (c.getPrefLabel() == null) {
      logger.warn("Couldn't retrieve preferred label", c);
      Optional.empty();
    }
    if (c.getLinks() == null || c.getLinks().getOntology() == null) {
      logger.warn("Couldn't retrieve ontology acronym", c);
      Optional.empty();
    }
    String definition = null;
    if (c.getDefinition() != null && c.getDefinition().size() > 0) {
      definition = c.getDefinition().get(0).toString();
    }
    return Optional.of(new SuggestedOntologyTerm(c.getId(), c.getPrefLabel(), c.getLinks().getOntology(), definition));
  }
}
