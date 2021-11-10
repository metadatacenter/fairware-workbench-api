package org.metadatacenter.fairware.config.bioportal;

public class BioportalConfig {

  private String apiKey;
  private String searchUrl;
  private int connectTimeout;
  private int socketTimeout;
  private int pageSize;

  public BioportalConfig() {
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getSearchUrl() {
    return searchUrl;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public int getSocketTimeout() {
    return socketTimeout;
  }

  public int getPageSize() {
    return pageSize;
  }
}
