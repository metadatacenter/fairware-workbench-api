package org.metadatacenter.fairware.config.cedar;

public class ResourceServerConfig {

  private String templatesUrl;
  private String recommendTemplatesUrl;

  public ResourceServerConfig() { }

  public ResourceServerConfig(String templatesUrl, String recommendTemplatesUrl) {
    this.templatesUrl = templatesUrl;
    this.recommendTemplatesUrl = recommendTemplatesUrl;
  }

  public String getTemplatesUrl() { return templatesUrl; }

  public String getRecommendTemplatesUrl() {
    return recommendTemplatesUrl;
  }
}
