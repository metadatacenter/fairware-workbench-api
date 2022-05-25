package org.metadatacenter.fairware.config.cedar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class CedarConfig {

  private static final String API_KEY = "apiKey";
  private static final String BASE_URL = "baseUrl";

  @Nonnull
  @JsonCreator
  public static CedarConfig create(@Nonnull @JsonProperty(API_KEY) String apiKey,
                                   @Nonnull @JsonProperty(BASE_URL) String baseUrl) {
    return new AutoValue_CedarConfig(apiKey, baseUrl);
  }

  @Nonnull
  @JsonProperty(API_KEY)
  public abstract String getApiKey();

  @Nonnull
  @JsonProperty(BASE_URL)
  public abstract String getBaseUrl();
}
