package org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response;

public class CedarRecommendTemplatesRequestSummary {

  private int sourceFieldsCount;

  public CedarRecommendTemplatesRequestSummary() { }

  public CedarRecommendTemplatesRequestSummary(int sourceFieldsCount) {
    this.sourceFieldsCount = sourceFieldsCount;
  }

  public int getSourceFieldsCount() {
    return sourceFieldsCount;
  }

  public void setSourceFieldsCount(int sourceFieldsCount) {
    this.sourceFieldsCount = sourceFieldsCount;
  }
}
