package org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response;

import java.util.List;

public class CedarRecommendTemplatesResponse {

  private int totalCount;
  private CedarRecommendTemplatesRequestSummary requestSummary;
  List<CedarTemplateRecommendation> recommendations;

  public CedarRecommendTemplatesResponse() { }

  public CedarRecommendTemplatesResponse(int totalCount,
                                         CedarRecommendTemplatesRequestSummary requestSummary,
                                         List<CedarTemplateRecommendation> recommendations) {
    this.totalCount = totalCount;
    this.requestSummary = requestSummary;
    this.recommendations = recommendations;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public CedarRecommendTemplatesRequestSummary getRequestSummary() {
    return requestSummary;
  }

  public List<CedarTemplateRecommendation> getRecommendations() {
    return recommendations;
  }
}
