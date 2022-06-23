package org.metadatacenter.fairware.core.services.evaluation;

import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.IssueCategory;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.FieldSpecification;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class TimeValueChecker {

  private static void attemptToParseValueToTime(String value, String timeFormat) throws DateTimeParseException {
    DateTimeFormatter.ofPattern(timeFormat).parse(value);
  }

  public Optional<EvaluationReportItem> checkValue(@Nonnull MetadataFieldInfo metadataField,
                                                   @Nonnull FieldSpecification fieldSpecification) {
    var possibleValue = metadataField.getValue();
    if (possibleValue.isPresent()) {
      var value = possibleValue.get();
      if (value instanceof String) {
        try {
          attemptToParseValueToTime(String.valueOf(value), fieldSpecification.getValueFormat().get());
        } catch (DateTimeParseException e) {
          var report = EvaluationReportItem.create(
              MetadataIssue.create(
                  IssueCategory.VALUE_ERROR,
                  IssueType.INVALID_TIME_FORMAT,
                  GeneralUtil.generateFullPathDotNotation(metadataField),
                  value),
              RepairAction.ofEnterCorrectValue());
          return Optional.of(report);
        }
      } else if (value instanceof Number || value instanceof Boolean) {
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
