package org.metadatacenter.fairware.api.response.evaluationReport;

import java.util.List;

public class FieldsCompletenessReport {

  private int completeFieldsCount;
  private int fieldsWithMissingRequiredValuesCount;
  private int fieldsWithMissingOptionalValuesCount;
  private List<FieldReport> items;

  public FieldsCompletenessReport() {
  }

  public int getCompleteFieldsCount() {
    return completeFieldsCount;
  }

  public void setCompleteFieldsCount(int completeFieldsCount) {
    this.completeFieldsCount = completeFieldsCount;
  }

  public int getFieldsWithMissingRequiredValuesCount() {
    return fieldsWithMissingRequiredValuesCount;
  }

  public void setFieldsWithMissingRequiredValuesCount(int fieldsWithMissingRequiredValuesCount) {
    this.fieldsWithMissingRequiredValuesCount = fieldsWithMissingRequiredValuesCount;
  }

  public int getFieldsWithMissingOptionalValuesCount() {
    return fieldsWithMissingOptionalValuesCount;
  }

  public void setFieldsWithMissingOptionalValuesCount(int fieldsWithMissingOptionalValuesCount) {
    this.fieldsWithMissingOptionalValuesCount = fieldsWithMissingOptionalValuesCount;
  }

  public List<FieldReport> getItems() {
    return items;
  }

  public void setItems(List<FieldReport> items) {
    this.items = items;
  }
}
