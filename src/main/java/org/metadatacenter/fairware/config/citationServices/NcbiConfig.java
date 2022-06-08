package org.metadatacenter.fairware.config.citationServices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class NcbiConfig {

  private static final String ROOT_URL = "rootUrl";

  @Nonnull
  @JsonCreator
  public static NcbiConfig create(@Nonnull @JsonProperty(ROOT_URL) String rootUrl) {
    return new AutoValue_NcbiConfig(rootUrl);
  }

  @Nonnull
  @JsonProperty(ROOT_URL)
  public abstract String getRootUrl();
}
