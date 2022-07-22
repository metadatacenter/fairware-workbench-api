package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.shared.IssueCategory;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.MetadataIssue;
import org.metadatacenter.fairware.shared.RepairAction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequiredValuesEvaluator implements IMetadataEvaluator {

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldInfoMap,
                                                     ImmutableMap<String, CedarTemplateField> templateFieldMap,
                                                     List<FieldAlignment> fieldAlignments) {
    var reportItems = Lists.<EvaluationReportItem>newArrayList();
    templateFieldMap.values().stream()
        .filter(templateField -> templateField.valueField().isRequired())
        .map(CedarTemplateField::getJsonPath)
        .forEach(templateFieldPath -> {
          var alignedMetadataFieldPath = fieldAlignments.stream()
              .filter(fieldAlignment -> fieldAlignment.getTemplateFieldPath().equals(templateFieldPath))
              .map(FieldAlignment::getMetadataFieldPath)
              .findFirst();
          if (alignedMetadataFieldPath.isPresent()) {
            var metadataFieldPath = alignedMetadataFieldPath.get();
            var metadataFieldInfo = metadataFieldInfoMap.get(metadataFieldPath);
            var metadataValue = metadataFieldInfo.getValue();
            if (metadataValue.isEmpty() || String.valueOf(metadataValue.get()).isEmpty()) {
              reportItems.add(createMissingRequiredValueReportItem(metadataFieldPath));
            }
          } else {
            reportItems.add(createMissingRequiredValueReportItem(templateFieldPath));
          }
        });
    return reportItems;
  }

  private EvaluationReportItem createMissingRequiredValueReportItem(String metadataFieldPath) {
    return EvaluationReportItem.create(
        MetadataIssue.create(
            IssueCategory.VALUE_ERROR,
            IssueType.MISSING_REQUIRED_VALUE,
            metadataFieldPath,
            ""),
        RepairAction.ofEnterMissingValue());
  }
}
