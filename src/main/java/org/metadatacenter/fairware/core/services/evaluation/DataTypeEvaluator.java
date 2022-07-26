package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class DataTypeEvaluator implements IMetadataEvaluator {

  private final StringValueChecker stringValueChecker;
  private final NumberValueChecker numberValueChecker;
  private final DateTimeValueChecker dateTimeValueChecker;
  private final DateValueChecker dateValueChecker;
  private final TimeValueChecker timeValueChecker;

  public DataTypeEvaluator(@Nonnull StringValueChecker stringValueChecker,
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
                                                     ImmutableMap<String, CedarTemplateField> templateFieldMap,
                                                     List<FieldAlignment> fieldAlignments) {
    var reportItems = Lists.<EvaluationReportItem>newArrayList();
    for (var fieldAlignment : fieldAlignments) {
      if (metadataFieldInfoMap.containsKey(fieldAlignment.getMetadataFieldPath())
          && templateFieldMap.containsKey(fieldAlignment.getTemplateFieldPath())) {
        var metadataFieldInfo = metadataFieldInfoMap.get(fieldAlignment.getMetadataFieldPath());
        var templateField = templateFieldMap.get(fieldAlignment.getTemplateFieldPath());
        var fieldSpecification = templateField.valueField();
        var dataType = fieldSpecification.getDataType();
        switch (dataType) {
          case "xsd:string":
            stringValueChecker.checkValue(metadataFieldInfo, fieldSpecification).ifPresent(reportItems::add);
            break;
          case "xsd:int":
          case "xsd:long":
          case "xsd:float":
          case "xsd:double":
          case "xsd:decimal":
            numberValueChecker.checkValue(metadataFieldInfo, fieldSpecification).ifPresent(reportItems::add);
            break;
          case "xsd:dateTime":
            dateTimeValueChecker.checkValue(metadataFieldInfo, fieldSpecification).ifPresent(reportItems::add);
            break;
          case "xsd:date":
            dateValueChecker.checkValue(metadataFieldInfo, fieldSpecification).ifPresent(reportItems::add);
            break;
          case "xsd:time":
            timeValueChecker.checkValue(metadataFieldInfo, fieldSpecification).ifPresent(reportItems::add);
            break;
        }
      }
    }
    return reportItems;
  }
}
