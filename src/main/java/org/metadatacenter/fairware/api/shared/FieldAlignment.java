package org.metadatacenter.fairware.api.shared;

import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;

public class FieldAlignment {

  private double similarityScore;
  private MetadataFieldInfo metadataField;
  private TemplateNodeInfo templateField;

  public FieldAlignment() { }

  public FieldAlignment(double similarityScore, MetadataFieldInfo metadataField, TemplateNodeInfo templateField) {
    this.similarityScore = similarityScore;
    this.metadataField = metadataField;
    this.templateField = templateField;
  }

  public double getSimilarityScore() {
    return similarityScore;
  }

  public MetadataFieldInfo getMetadataField() {
    return metadataField;
  }

  public TemplateNodeInfo getTemplateField() {
    return templateField;
  }
}
