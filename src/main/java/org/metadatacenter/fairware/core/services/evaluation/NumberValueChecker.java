package org.metadatacenter.fairware.core.services.evaluation;

import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.FieldSpecification;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

public class NumberValueChecker {

  public Optional<EvaluationReportItem> checkValue(@Nonnull MetadataFieldInfo metadataField,
                                                   @Nonnull FieldSpecification fieldSpecification) {
    var possibleValue = metadataField.getValue();
    if (possibleValue.isPresent()) {
      var value = possibleValue.get();
      if (value instanceof String) {
        try {
          var number = attemptToParseValueToNumber(value);
          var report = EvaluationReportItem.create(
              GeneralUtil.generateFullPathDotNotation(metadataField),
              MetadataIssue.create(IssueType.INVALID_VALUE_REPRESENTATION),
              RepairAction.ofEnterNumberTypeValue(number));
          return Optional.of(report);
        } catch (ParseException e) {
          var report = EvaluationReportItem.create(
              GeneralUtil.generateFullPathDotNotation(metadataField),
              MetadataIssue.create(IssueType.INVALID_NUMBER_FORMAT),
              RepairAction.ofEnterCorrectValue());
          return Optional.of(report);
        }
      }
    }
    return Optional.empty();
  }

  private static Number attemptToParseValueToNumber(Object value) throws ParseException {
    var usLocale = new Locale("en","US");
    var numberFormat = NumberFormat.getInstance(usLocale);
    return numberFormat.parse(String.valueOf(value));
  }
}
