package org.metadatacenter.fairware.core.services.evaluation;

import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class DateValueChecker {

  private static Date attemptToParseValueToDate(Object value) throws ParseException {
    var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.parse(String.valueOf(value));
  }

  public Optional<EvaluationReportItem> checkValue(@Nonnull MetadataFieldInfo metadataField) {
    var possibleValue = metadataField.getValue();
    if (possibleValue.isPresent()) {
      var value = possibleValue.get();
      if (value instanceof String) {
        try {
          attemptToParseValueToDate(value);
        } catch (ParseException e) {
          var report = EvaluationReportItem.create(
              GeneralUtil.generateFullPathDotNotation(metadataField),
              MetadataIssue.create(IssueType.INVALID_DATE_FORMAT),
              RepairAction.ofEnterCorrectValue());
          return Optional.of(report);
        }
      } else {
        var report = EvaluationReportItem.create(
            GeneralUtil.generateFullPathDotNotation(metadataField),
            MetadataIssue.create(IssueType.INVALID_VALUE_REPRESENTATION),
            RepairAction.ofEnterCorrectValue());
        return Optional.of(report);
      }
    }
    return Optional.empty();
  }
}
