package org.metadatacenter.fairware.api.shared;

public class FieldAlignment {

  private double similarityScore;
  private String metadataFieldPath;
  private String templateFieldPath;

  public FieldAlignment() { }

  public FieldAlignment(double similarityScore, String metadataFieldPath, String templateFieldPath) {
    this.similarityScore = similarityScore;
    this.metadataFieldPath = metadataFieldPath;
    this.templateFieldPath = templateFieldPath;
  }

  public double getSimilarityScore() {
    return similarityScore;
  }

  public String getMetadataFieldPath() {
    return metadataFieldPath;
  }

  public String getTemplateFieldPath() {
    return templateFieldPath;
  }
}
