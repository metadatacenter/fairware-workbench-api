package org.metadatacenter.fairware.core.services.evaluation;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpClass;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpPagedResults;
import org.metadatacenter.fairware.core.services.cedar.CedarService;

import java.io.IOException;
import java.util.*;

/**
 * For metadata fields that have not been aligned to template fields, this evaluator determines whether they align with
 * terms from ontologies that could make the field names more FAIR.
 */
public class ExtraFieldsEvaluator implements IMetadataEvaluator {

  private BioportalService bioportalService;

  private ExtraFieldsEvaluator() {
    // disabled constructor, never called
  }

  public ExtraFieldsEvaluator(BioportalService bioportalService) {
    this.bioportalService = bioportalService;
  }

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> mfMap,
                                                     Map<String, TemplateNodeInfo> tfMap,
                                                     List<FieldAlignment> fieldAlignments)
      throws HttpException, IOException {

    List<EvaluationReportItem> reportItems = new ArrayList<>();
    List<MetadataFieldInfo> nonMatchedFields = getNonMatchedMetadataFields(fieldAlignments, mfMap);

    // Use BioPortal to find a matching ontology term
    for (MetadataFieldInfo mf : nonMatchedFields) {
      String termName = mf.getName();
      BpPagedResults<BpClass> terms = bioportalService.search(termName);
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
}
