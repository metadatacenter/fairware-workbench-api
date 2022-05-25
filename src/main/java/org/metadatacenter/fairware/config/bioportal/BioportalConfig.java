package org.metadatacenter.fairware.config.bioportal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class BioportalConfig {

  private static final String API_KEY = "apiKey";
  private static final String SEARCH_URL = "searchUrl";
  private static final String CONNECT_TIMEOUT = "connectTimeout";
  private static final String SOCKET_TIMEOUT = "socketTimeout";
  private static final String PAGE_SIZE = "pageSize";

  @Nonnull
  @JsonCreator
  public static BioportalConfig create(@Nonnull @JsonProperty(API_KEY) String apiKey,
                                       @Nonnull @JsonProperty(SEARCH_URL) String searchUrl,
                                       @JsonProperty(CONNECT_TIMEOUT) int connectTimeout,
                                       @JsonProperty(SOCKET_TIMEOUT) int socketTimeout,
                                       @JsonProperty(PAGE_SIZE) int pageSize) {
    return new AutoValue_BioportalConfig(apiKey, searchUrl, connectTimeout, socketTimeout, pageSize);
  }

  @Nonnull
  @JsonProperty(API_KEY)
  public abstract String getApiKey();

  @Nonnull
  @JsonProperty(SEARCH_URL)
  public abstract String getSearchUrl();

  @JsonProperty(CONNECT_TIMEOUT)
  public abstract int getConnectTimeout();

  @JsonProperty(SOCKET_TIMEOUT)
  public abstract int getSocketTimeout();

  @JsonProperty(PAGE_SIZE)
  public abstract int getPageSize();
}
