package org.metadatacenter.fairware.api.response.evaluationReport;

public class FieldReport {

  private String metadataFieldPath;
  private String templateId;
  private String templateName;
  private int fieldsCount;
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

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
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
