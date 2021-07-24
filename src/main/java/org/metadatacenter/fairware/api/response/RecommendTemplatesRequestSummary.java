package org.metadatacenter.fairware.api.response;

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
