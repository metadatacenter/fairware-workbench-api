package org.metadatacenter.fairware.config.cedar;

public class CedarConfig {

  private String apiKey;
  private ResourceServerConfig resourceServer;

  public CedarConfig() {
  }

  public String getApiKey() {
    return apiKey;
  }

  public ResourceServerConfig getResourceServer() {
    return resourceServer;
  }
}
