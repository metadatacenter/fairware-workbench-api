package org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CedarTemplateRecommendation {

  double recommendationScore;
  int sourceFieldsMatched;
  int targetFieldsCount;
  @JsonProperty("resourceExtract")
  CedarTemplateExtract templateExtract;

  public CedarTemplateRecommendation() {
  }

  public CedarTemplateRecommendation(double recommendationScore, int sourceFieldsMatched, int targetFieldsCount, CedarTemplateExtract templateExtract) {
    this.recommendationScore = recommendationScore;
    this.sourceFieldsMatched = sourceFieldsMatched;
    this.targetFieldsCount = targetFieldsCount;
    this.templateExtract = templateExtract;
  }

  public double getRecommendationScore() {
    return recommendationScore;
  }

  public void setRecommendationScore(double recommendationScore) {
    this.recommendationScore = recommendationScore;
  }

  public int getSourceFieldsMatched() {
    return sourceFieldsMatched;
  }

  public void setSourceFieldsMatched(int sourceFieldsMatched) {
    this.sourceFieldsMatched = sourceFieldsMatched;
  }

  public int getTargetFieldsCount() {
    return targetFieldsCount;
  }

  public void setTargetFieldsCount(int targetFieldsCount) {
    this.targetFieldsCount = targetFieldsCount;
  }

  public CedarTemplateExtract getTemplateExtract() {
    return templateExtract;
  }

  public void setTemplateExtract(CedarTemplateExtract templateExtract) {
    this.templateExtract = templateExtract;
  }
}


