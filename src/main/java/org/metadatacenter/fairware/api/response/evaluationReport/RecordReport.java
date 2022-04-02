package org.metadatacenter.fairware.api.response.evaluationReport;

public class RecordReport {

  private String metadataRecordId;
  private int missingRequiredValues;
  private int missingOptionalValues;

  public RecordReport() {}

  public String getMetadataRecordId() {
    return metadataRecordId;
  }

  public void setMetadataRecordId(String metadataRecordId) {
    this.metadataRecordId = metadataRecordId;
  }

  public int getMissingRequiredValues() {
    return missingRequiredValues;
  }

  public void setMissingRequiredValues(int missingRequiredValues) {
    this.missingRequiredValues = missingRequiredValues;
  }

  public int getMissingOptionalValues() {
    return missingOptionalValues;
  }

  public void setMissingOptionalValues(int missingOptionalValues) {
    this.missingOptionalValues = missingOptionalValues;
  }
}
