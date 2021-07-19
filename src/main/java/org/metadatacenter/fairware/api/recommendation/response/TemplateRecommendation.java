package org.metadatacenter.fairware.api.recommendation.response;

public class TemplateRecommendation {

  double recommendationScore;
  int sourceFieldsMatched;
  int targetFieldsCount;
  TemplateExtract templateExtract;

  public TemplateRecommendation() {
  }

  public TemplateRecommendation(double recommendationScore, int sourceFieldsMatched, int targetFieldsCount,
                                     TemplateExtract templateExtract) {
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

  public TemplateExtract getTemplateExtract() {
    return templateExtract;
  }

  public void setTemplateExtract(TemplateExtract templateExtract) {
    this.templateExtract = templateExtract;
  }
}


