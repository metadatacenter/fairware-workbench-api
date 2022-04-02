package org.metadatacenter.fairware.api.response.evaluationReport;

public class FieldReport {

  private String metadataFieldPath;
  private int completeCount;
  private int missingRequiredValuesCount;
  private int missingOptionalValuesCount;

  public FieldReport() {}

  public String getMetadataFieldPath() {
    return metadataFieldPath;
  }

  public void setMetadataFieldPath(String metadataFieldPath) {
    this.metadataFieldPath = metadataFieldPath;
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
