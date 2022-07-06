package org.metadatacenter.fairware.api.response.recommendation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RecommendTemplatesResponse {

  private static final String TOTAL_COUNT = "totalCount";
  private static final String REQUEST_SUMMARY = "requestSummary";
  private static final String RECOMMENDATIONS = "recommendations";

  @Nonnull
  @JsonCreator
  public static RecommendTemplatesResponse create(@JsonProperty(TOTAL_COUNT) long totalCount,
                                                  @Nonnull @JsonProperty(REQUEST_SUMMARY) RecommendTemplatesRequestSummary requestSummary,
                                                  @Nonnull @JsonProperty(RECOMMENDATIONS) ImmutableList<TemplateRecommendation> recommendations) {
    return new AutoValue_RecommendTemplatesResponse(totalCount, requestSummary, recommendations);
  }

  @JsonProperty(TOTAL_COUNT)
  public abstract long getTotalCount();

  @Nonnull
  @JsonProperty(REQUEST_SUMMARY)
  public abstract RecommendTemplatesRequestSummary getRequestSummary();

  @Nonnull
  @JsonProperty(RECOMMENDATIONS)
  public abstract ImmutableList<TemplateRecommendation> getTemplateRecommendations();
}

