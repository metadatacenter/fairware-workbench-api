package org.metadatacenter.fairware.config.citationServices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.metadatacenter.fairware.config.citationServices.datacite.DataCiteConfig;

import javax.annotation.Nonnull;

@AutoValue
public abstract class CitationServicesConfig {

  private static final String DATA_CITE_CONFIG = "dataCite";

  @Nonnull
  @JsonCreator
  public static CitationServicesConfig create(@Nonnull @JsonProperty(DATA_CITE_CONFIG) DataCiteConfig dataCiteConfig) {
    return new AutoValue_CitationServicesConfig(dataCiteConfig);
  }

  @Nonnull
  @JsonProperty(DATA_CITE_CONFIG)
  public abstract DataCiteConfig getDatacite();
}
