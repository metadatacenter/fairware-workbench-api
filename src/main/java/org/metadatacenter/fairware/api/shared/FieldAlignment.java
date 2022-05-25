package org.metadatacenter.fairware.api.shared;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class FieldAlignment {

  public static FieldAlignment create(double similarityScore,
                                      @Nonnull String metadataFieldPath,
                                      @Nonnull String templateFieldPath) {
    return new AutoValue_FieldAlignment(similarityScore, metadataFieldPath, templateFieldPath);
  }

  public abstract double getSimilarityScore();

  @Nonnull
  public abstract String getMetadataFieldPath();

  @Nonnull
  public abstract String getTemplateFieldPath();
}
