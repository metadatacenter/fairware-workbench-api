package org.metadatacenter.fairware.api.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class FieldAlignment {

  private static final String SIMILARITY_SCORE = "similarityScore";
  private static final String METADATA_FIELD_PATH = "metadataFieldPath";
  private static final String TEMPLATE_FIELD_PATH = "templateFieldPath";

  @Nonnull
  @JsonCreator
  public static FieldAlignment create(@JsonProperty(SIMILARITY_SCORE) double similarityScore,
                                      @Nonnull @JsonProperty(METADATA_FIELD_PATH) String metadataFieldPath,
                                      @Nonnull @JsonProperty(TEMPLATE_FIELD_PATH) String templateFieldPath) {
    return new AutoValue_FieldAlignment(similarityScore, metadataFieldPath, templateFieldPath);
  }

  @JsonProperty(SIMILARITY_SCORE)
  public abstract double getSimilarityScore();

  @Nonnull
  @JsonProperty(METADATA_FIELD_PATH)
  public abstract String getMetadataFieldPath();

  @Nonnull
  @JsonProperty(TEMPLATE_FIELD_PATH)
  public abstract String getTemplateFieldPath();
}
