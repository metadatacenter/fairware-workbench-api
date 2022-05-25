package org.metadatacenter.fairware.config.citationServices.datacite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class DataCiteConfig {

  private static final String DOIS_URL = "doisUrl";

  @Nonnull
  @JsonCreator
  public static DataCiteConfig create(@Nonnull @JsonProperty(DOIS_URL) String doisUrl) {
    return new AutoValue_DataCiteConfig(doisUrl);
  }

  @Nonnull
  @JsonProperty(DOIS_URL)
  public abstract String getDoisUrl();
}
