package org.metadatacenter.fairware.api.response.evaluationReport;

public class RecordReport {

  private String metadataRecordId;
  private int fieldsCount;
  private int completeCount;
  private int missingRequiredValuesCount;
  private int missingOptionalValuesCount;

  public RecordReport() {}

  public String getMetadataRecordId() {
    return metadataRecordId;
  }

  public void setMetadataRecordId(String metadataRecordId) {
    this.metadataRecordId = metadataRecordId;
  }

  public int getFieldsCount() {
    return fieldsCount;
  }

  public void setFieldsCount(int fieldsCount) {
    this.fieldsCount = fieldsCount;
  }

  public int getCompleteCount() {
    return completeCount;
  }

  public void setCompleteCount(int completeCount) {
    this.completeCount = completeCount;
  }

  public int getMissingRequiredValuesCount() {
    return missingRequiredValuesCount;
  }

  public void setMissingRequiredValuesCount(int missingRequiredValuesCount) {
    this.missingRequiredValuesCount = missingRequiredValuesCount;
  }

  public int getMissingOptionalValuesCount() {
    return missingOptionalValuesCount;
  }

  public void setMissingOptionalValuesCount(int missingOptionalValuesCount) {
    this.missingOptionalValuesCount = missingOptionalValuesCount;
  }
}
