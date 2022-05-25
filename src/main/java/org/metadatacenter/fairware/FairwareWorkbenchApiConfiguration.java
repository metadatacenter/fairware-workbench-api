package org.metadatacenter.fairware;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Configuration;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.config.citationServices.CitationServicesConfig;

import javax.annotation.Nonnull;

@AutoValue
public abstract class FairwareWorkbenchApiConfiguration extends Configuration {

  private static final String SWAGGER = "swagger";
  private static final String CORE = "core";
  private static final String CEDAR = "cedar";
  private static final String BIOPORTAL = "bioportal";
  private static final String CITATION_SERVICES = "citationServices";

  @Nonnull
  @JsonCreator
  public static FairwareWorkbenchApiConfiguration create(@Nonnull @JsonProperty(SWAGGER) SwaggerBundleConfiguration swaggerBundleConfiguration,
                                                         @Nonnull @JsonProperty(CORE) CoreConfig coreConfig,
                                                         @Nonnull @JsonProperty(CEDAR) CedarConfig cedarConfig,
                                                         @Nonnull @JsonProperty(BIOPORTAL) BioportalConfig bioportalConfig,
                                                         @Nonnull @JsonProperty(CITATION_SERVICES) CitationServicesConfig citationServicesConfig) {
    return new AutoValue_FairwareWorkbenchApiConfiguration(swaggerBundleConfiguration, coreConfig,
        cedarConfig, bioportalConfig, citationServicesConfig);
  }

  @Nonnull
  @JsonProperty(SWAGGER)
  public abstract SwaggerBundleConfiguration getSwaggerBundleConfiguration();

  @Nonnull
  @JsonProperty(CORE)
  public abstract CoreConfig getCoreConfig();

  @Nonnull
  @JsonProperty(CEDAR)
  public abstract CedarConfig getCedarConfig();

  @Nonnull
  @JsonProperty(BIOPORTAL)
  public abstract BioportalConfig getBioportalConfig();

  @Nonnull
  @JsonProperty(CITATION_SERVICES)
  public abstract CitationServicesConfig getMetadataServicesConfig();
}
