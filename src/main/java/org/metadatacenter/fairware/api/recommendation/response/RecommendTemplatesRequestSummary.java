package org.metadatacenter.fairware.api.recommendation.response;

public class RecommendTemplatesRequestSummary {

  private int sourceFieldsCount;

  public RecommendTemplatesRequestSummary() {
  }

  public RecommendTemplatesRequestSummary(int sourceFieldsCount) {
    this.sourceFieldsCount = sourceFieldsCount;
  }

  public int getSourceFieldsCount() {
    return sourceFieldsCount;
  }
}
