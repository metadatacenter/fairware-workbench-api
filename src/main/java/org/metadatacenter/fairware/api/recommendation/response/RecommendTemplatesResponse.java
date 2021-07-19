package org.metadatacenter.fairware.api.recommendation.response;

import java.util.List;

public class RecommendTemplatesResponse {

  private long totalCount;
  private RecommendTemplatesRequestSummary requestSummary;
  List<TemplateRecommendation> recommendations;

  public RecommendTemplatesResponse() {
  }

  public RecommendTemplatesResponse(long totalCount, RecommendTemplatesRequestSummary requestSummary,
                                    List<TemplateRecommendation> recommendations) {
    this.totalCount = totalCount;
    this.requestSummary = requestSummary;
    this.recommendations = recommendations;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public RecommendTemplatesRequestSummary getRequestSummary() {
    return requestSummary;
  }

  public void setRequestSummary(RecommendTemplatesRequestSummary requestSummary) {
    this.requestSummary = requestSummary;
  }

  public List<TemplateRecommendation> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<TemplateRecommendation> recommendations) {
    this.recommendations = recommendations;
  }
}

