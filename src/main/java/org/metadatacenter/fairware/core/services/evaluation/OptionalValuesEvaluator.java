package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.Lists;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateNodeInfo;

import java.util.List;
import java.util.Map;

public class OptionalValuesEvaluator implements IMetadataEvaluator {

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldInfoMap,
                                                     Map<String, TemplateNodeInfo> templateNodeInfoMap,
                                                     List<FieldAlignment> fieldAlignments) {
    var reportItems = Lists.<EvaluationReportItem>newArrayList();
    for (var fieldAlignment : fieldAlignments) {
      if (metadataFieldInfoMap.containsKey(fieldAlignment.getMetadataFieldPath())
          && templateNodeInfoMap.containsKey(fieldAlignment.getTemplateFieldPath())) {
        var metadataFieldInfo = metadataFieldInfoMap.get(fieldAlignment.getMetadataFieldPath());
        var templateNodeInfo = templateNodeInfoMap.get(fieldAlignment.getTemplateFieldPath());
        // Check required value constraint
        if (!templateNodeInfo.isValueRequired()) {
          if (metadataFieldInfo.getValue() == null
            || (metadataFieldInfo.getValue() instanceof String
              && metadataFieldInfo.getValue().toString().trim().isEmpty())) {
            reportItems.add(
                EvaluationReportItem.create(
                    fieldAlignment.getMetadataFieldPath(),
                    MetadataIssue.create(IssueType.MISSING_OPTIONAL_VALUE),
                    RepairAction.ofEnterMissingValue()));
          }
        }
      }
    }
    return reportItems;
  }
}
