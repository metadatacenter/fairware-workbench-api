package org.metadatacenter.fairware.api.response.recommendation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class TemplateRecommendation {

  private static final String RECOMMENDATION_SCORE = "recommendationScore";
  private static final String SOURCE_FIELDS_MATCHED = "sourceFieldsMatched";
  private static final String TARGET_FIELDS_COUNT = "targetFieldsCount";
  private static final String RESOURCE_EXTRACT = "resourceExtract";

  @Nonnull
  @JsonCreator
  public static TemplateRecommendation create(@JsonProperty(RECOMMENDATION_SCORE) double recommendationScore,
                                              @JsonProperty(SOURCE_FIELDS_MATCHED) int sourceFieldsMatched,
                                              @JsonProperty(TARGET_FIELDS_COUNT) int targetFieldsCount,
                                              @Nonnull @JsonProperty(RESOURCE_EXTRACT) ResourceExtract resourceExtract) {
    return new AutoValue_TemplateRecommendation(recommendationScore, sourceFieldsMatched, targetFieldsCount, resourceExtract);
  }

  @JsonProperty(RECOMMENDATION_SCORE)
  public abstract double getRecommendationScore();

  @JsonProperty(SOURCE_FIELDS_MATCHED)
  public abstract int getSourceFieldsMatched();

  @JsonProperty(TARGET_FIELDS_COUNT)
  public abstract int getTargetFieldsCount();

  @JsonProperty(RESOURCE_EXTRACT)
  public abstract ResourceExtract getResourceExtract();
}


