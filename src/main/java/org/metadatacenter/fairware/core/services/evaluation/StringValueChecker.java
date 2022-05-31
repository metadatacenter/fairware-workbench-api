package org.metadatacenter.fairware.core.services.evaluation;

import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.util.Optional;

public class StringValueChecker {

  public Optional<EvaluationReportItem> checkValue(@Nonnull MetadataFieldInfo metadataField) {
    var possibleValue = metadataField.getValue();
    if (possibleValue.isPresent()) {
      var value = possibleValue.get();
      if (!(value instanceof String)) {
        var report = EvaluationReportItem.create(
            GeneralUtil.generateFullPathDotNotation(metadataField),
            MetadataIssue.create(IssueType.INVALID_VALUE_REPRESENTATION),
            RepairAction.ofEnterStringTypeValue(String.valueOf(value)));
        return Optional.of(report);
      }
    }
    return Optional.empty();
  }
}
