package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
import org.metadatacenter.fairware.shared.RepairAction;
import org.metadatacenter.fairware.shared.IssueCategory;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.MetadataIssue;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import java.util.List;
import java.util.Map;

public class RequiredValuesEvaluator implements IMetadataEvaluator {

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldInfoMap,
                                                     ImmutableMap<String, CedarTemplateField> templateFieldMap,
                                                     List<FieldAlignment> fieldAlignments) {
    var reportItems = Lists.<EvaluationReportItem>newArrayList();
    for (var fieldAlignment : fieldAlignments) {
      if (metadataFieldInfoMap.containsKey(fieldAlignment.getMetadataFieldPath())
          && templateFieldMap.containsKey(fieldAlignment.getTemplateFieldPath())) {
        var metadataFieldInfo = metadataFieldInfoMap.get(fieldAlignment.getMetadataFieldPath());
        var templateField = templateFieldMap.get(fieldAlignment.getTemplateFieldPath());
        // Check required value constraint
        if (templateField.valueField().isRequired()) {
          var fieldValue = metadataFieldInfo.getValue();
          if (!fieldValue.isPresent() || fieldValue.get().toString().isEmpty()) {
            reportItems.add(
                EvaluationReportItem.create(
                    MetadataIssue.create(
                        IssueCategory.VALUE_ERROR,
                        IssueType.MISSING_REQUIRED_VALUE,
                        fieldAlignment.getMetadataFieldPath(),
                        ""),
                    RepairAction.ofEnterMissingValue()));
          }
        }
      }
    }
    return reportItems;
  }
}
