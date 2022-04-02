package org.metadatacenter.fairware.api.response.evaluationReport;

import java.util.List;

public class FieldsCompletenessReport {

  private int metadataFieldPath;
  private int completeFieldsCount;
  private int fieldsWithMissingRequiredValuesCount;
  private int fieldsWithmissingOptionalValuesCount;
  private List<FieldReport> items;

  public FieldsCompletenessReport() {
  }

  public int getMetadataFieldPath() {
    return metadataFieldPath;
  }

  public void setMetadataFieldPath(int metadataFieldPath) {
    this.metadataFieldPath = metadataFieldPath;
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

  public int getFieldsWithmissingOptionalValuesCount() {
    return fieldsWithmissingOptionalValuesCount;
  }

  public void setFieldsWithmissingOptionalValuesCount(int fieldsWithmissingOptionalValuesCount) {
    this.fieldsWithmissingOptionalValuesCount = fieldsWithmissingOptionalValuesCount;
  }

  public List<FieldReport> getItems() {
    return items;
  }

  public void setItems(List<FieldReport> items) {
    this.items = items;
  }
}
