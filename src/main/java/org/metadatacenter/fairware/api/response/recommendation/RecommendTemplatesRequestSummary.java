package org.metadatacenter.fairware.api.response.recommendation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RecommendTemplatesRequestSummary {

  private static final String SOURCE_FIELDS_COUNT = "sourceFieldsCount";

  @Nonnull
  @JsonCreator
  public static RecommendTemplatesRequestSummary create(@JsonProperty(SOURCE_FIELDS_COUNT) int sourceFieldsCount) {
    return new AutoValue_RecommendTemplatesRequestSummary(sourceFieldsCount);
  }

  @JsonProperty(SOURCE_FIELDS_COUNT)
  public abstract int getSourceFieldsCount();
}
