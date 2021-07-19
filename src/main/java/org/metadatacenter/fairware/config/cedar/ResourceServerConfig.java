package org.metadatacenter.fairware.config.cedar;

public class ResourceServerConfig {

  private String recommendTemplatesUrl;

  public ResourceServerConfig() { }

  public ResourceServerConfig(String recommendTemplatesUrl) {
    this.recommendTemplatesUrl = recommendTemplatesUrl;
  }

  public String getRecommendTemplatesUrl() {
    return recommendTemplatesUrl;
  }
}
