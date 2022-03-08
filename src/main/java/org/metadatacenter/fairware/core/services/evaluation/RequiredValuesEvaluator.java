package org.metadatacenter.fairware.core.services.evaluation;

import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.EnterFieldValueAction;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateNodeInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequiredValuesEvaluator implements IMetadataEvaluator {

  final MetadataIssue issue = new MetadataIssue(IssueType.MISSING_REQUIRED_VALUE);

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> mfMap,
                                                     Map<String, TemplateNodeInfo> tfMap,
                                                     List<FieldAlignment> fieldAlignments) {

    List<EvaluationReportItem> reportItems = new ArrayList<>();

    for (FieldAlignment al : fieldAlignments) {

      if (mfMap.containsKey(al.getMetadataFieldPath()) && tfMap.containsKey(al.getTemplateFieldPath())) {
        MetadataFieldInfo mf = mfMap.get(al.getMetadataFieldPath());
        TemplateNodeInfo tf = tfMap.get(al.getTemplateFieldPath());
        // Check required value constraint
        if (tf.isValueRequired() && (mf.getValue() == null
            || (mf.getValue() instanceof String && mf.getValue().toString().trim().isEmpty()))) {
          RepairAction repairAction = new EnterFieldValueAction();
          reportItems.add(new EvaluationReportItem(al.getMetadataFieldPath(), issue, repairAction));
        }
      }

    }
    return reportItems;
  }
}
