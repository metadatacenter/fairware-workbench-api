package org.metadatacenter.fairware.core.services.evaluation;

import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.shared.RepairAction;
import org.metadatacenter.fairware.shared.IssueCategory;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.MetadataIssue;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.domain.CedarTemplateFieldSpecification;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

public class NumberValueChecker {

  public Optional<EvaluationReportItem> checkValue(@Nonnull MetadataFieldInfo metadataField,
                                                   @Nonnull CedarTemplateFieldSpecification fieldSpecification) {
    var possibleValue = metadataField.getValue();
    if (possibleValue.isPresent()) {
      var value = possibleValue.get();
      if (value instanceof String || value instanceof Boolean) {
        try {
          var number = attemptToParseValueToNumber(value);
          var report = EvaluationReportItem.create(
              MetadataIssue.create(
                  IssueCategory.VALUE_ERROR,
                  IssueType.EXPECTING_INPUT_NUMBER,
                  GeneralUtil.generateFullPathDotNotation(metadataField),
                  value),
              RepairAction.ofEnterNumberValue(number));
          return Optional.of(report);
        } catch (ParseException e) {
          var report = EvaluationReportItem.create(
              MetadataIssue.create(
                  IssueCategory.VALUE_ERROR,
                  IssueType.INVALID_NUMBER_FORMAT,
                  GeneralUtil.generateFullPathDotNotation(metadataField),
                  value),
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
