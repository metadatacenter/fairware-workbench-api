package org.metadatacenter.fairware.core.services.evaluation;

import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.core.domain.CedarTemplateFieldSpecification;
import org.metadatacenter.fairware.shared.RepairAction;
import org.metadatacenter.fairware.shared.IssueCategory;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.MetadataIssue;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.util.Optional;

public class StringValueChecker {

  public Optional<EvaluationReportItem> checkValue(@Nonnull MetadataFieldInfo metadataField,
                                                   @Nonnull CedarTemplateFieldSpecification fieldSpecification) {
    var possibleValue = metadataField.getValue();
    if (possibleValue.isPresent()) {
      var value = possibleValue.get();
      if (value instanceof Number || value instanceof Boolean) {
        var report = EvaluationReportItem.create(
            MetadataIssue.create(
                IssueCategory.VALUE_ERROR,
                IssueType.EXPECTING_INPUT_STRING,
                GeneralUtil.generateFullPathDotNotation(metadataField),
                value),
            RepairAction.ofEnterStringValue(String.valueOf(value)));
        return Optional.of(report);
      }
    }
    return Optional.empty();
  }
}
