package org.metadatacenter.fairware.config.cedar;

public class ResourceServerConfig {

  private String templatesUrl;
  private String recommendTemplatesUrl;

  public ResourceServerConfig() { }
  
  public String getTemplatesUrl() { return templatesUrl; }

  public String getRecommendTemplatesUrl() {
    return recommendTemplatesUrl;
  }
}
