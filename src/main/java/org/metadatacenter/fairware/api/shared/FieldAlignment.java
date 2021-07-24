package org.metadatacenter.fairware.api.shared;

public class FieldAlignment {

  private String metadataFieldPath;
  private String metadataFieldName;
  private String metadataFieldId;
  private String templateFieldPath;
  private String templateFieldName;
  private String templateFieldLabel;
  private double similarityScore;

  public FieldAlignment() { }

  public FieldAlignment(String metadataFieldPath, String metadataFieldName, String metadataFieldId,
                        String templateFieldPath, String templateFieldName, String templateFieldLabel,
                        double similarityScore) {
    this.metadataFieldPath = metadataFieldPath;
    this.metadataFieldName = metadataFieldName;
    this.metadataFieldId = metadataFieldId;
    this.templateFieldPath = templateFieldPath;
    this.templateFieldName = templateFieldName;
    this.templateFieldLabel = templateFieldLabel;
    this.similarityScore = similarityScore;
  }

  public String getMetadataFieldPath() {
    return metadataFieldPath;
  }

  public String getMetadataFieldName() {
    return metadataFieldName;
  }

  public String getMetadataFieldId() {
    return metadataFieldId;
  }

  public String getTemplateFieldPath() {
    return templateFieldPath;
  }

  public String getTemplateFieldName() {
    return templateFieldName;
  }

  public String getTemplateFieldLabel() {
    return templateFieldLabel;
  }

  public double getSimilarityScore() {
    return similarityScore;
  }
}
