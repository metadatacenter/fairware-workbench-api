package org.metadatacenter.fairware.api.evaluation.output;

public class TemplateRecommendation {

  private TemplateInfo templateInformation;
  private int matchedFields;

  public TemplateRecommendation() {}

  public TemplateRecommendation(TemplateInfo templateInformation, int matchedFields) {
    this.templateInformation = templateInformation;
    this.matchedFields = matchedFields;
  }

  public TemplateInfo getTemplateInformation() {
    return templateInformation;
  }

  public void setTemplateInformation(TemplateInfo templateInformation) {
    this.templateInformation = templateInformation;
  }

  public int getMatchedFields() {
    return matchedFields;
  }

  public void setMatchedFields(int matchedFields) {
    this.matchedFields = matchedFields;
  }

}
