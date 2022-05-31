package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateField;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ValueTypeEvaluator implements IMetadataEvaluator {

  private final StringValueChecker stringValueChecker;
  private final NumberValueChecker numberValueChecker;
  private final DateTimeValueChecker dateTimeValueChecker;
  private final DateValueChecker dateValueChecker;
  private final TimeValueChecker timeValueChecker;

  public ValueTypeEvaluator(@Nonnull StringValueChecker stringValueChecker,
                            @Nonnull NumberValueChecker numberValueChecker,
                            @Nonnull DateTimeValueChecker dateTimeValueChecker,
                            @Nonnull DateValueChecker dateValueChecker,
                            @Nonnull TimeValueChecker timeValueChecker) {
    this.stringValueChecker = checkNotNull(stringValueChecker);
    this.numberValueChecker = checkNotNull(numberValueChecker);
    this.dateTimeValueChecker = checkNotNull(dateTimeValueChecker);
    this.dateValueChecker = checkNotNull(dateValueChecker);
    this.timeValueChecker = checkNotNull(timeValueChecker);
  }

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldInfoMap,
                                                     ImmutableMap<String, TemplateField> templateFieldMap,
                                                     List<FieldAlignment> fieldAlignments) throws HttpException, IOException {
    var reportItems = Lists.<EvaluationReportItem>newArrayList();
    for (var fieldAlignment : fieldAlignments) {
      if (metadataFieldInfoMap.containsKey(fieldAlignment.getMetadataFieldPath())
          && templateFieldMap.containsKey(fieldAlignment.getTemplateFieldPath())) {
        var metadataFieldInfo = metadataFieldInfoMap.get(fieldAlignment.getMetadataFieldPath());
        var templateField = templateFieldMap.get(fieldAlignment.getTemplateFieldPath());
        var templateFieldSpecification = templateField.valueField();
        var valueType = templateFieldSpecification.getValueType();
        switch (valueType) {
          case STRING:
            stringValueChecker.checkValue(metadataFieldInfo).ifPresent(reportItems::add);
            break;
          case NUMBER:
            numberValueChecker.checkValue(metadataFieldInfo).ifPresent(reportItems::add);
            break;
          case DATE_TIME:
            dateTimeValueChecker.checkValue(metadataFieldInfo).ifPresent(reportItems::add);
            break;
          case DATE:
            dateValueChecker.checkValue(metadataFieldInfo).ifPresent(reportItems::add);
            break;
          case TIME:
            timeValueChecker.checkValue(metadataFieldInfo).ifPresent(reportItems::add);
            break;
        }
      }
    }
    return reportItems;
  }
}
