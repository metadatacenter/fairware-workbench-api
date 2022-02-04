package org.metadatacenter.fairware;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Configuration;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.config.citationServices.CitationServicesConfig;

public class FairwareWorkbenchApiConfiguration extends Configuration {

  @JsonProperty("swagger")
  private SwaggerBundleConfiguration swaggerBundleConfiguration;
  @JsonProperty("core")
  private CoreConfig coreConfig;
  @JsonProperty("cedar")
  private CedarConfig cedarConfig;
  @JsonProperty("bioportal")
  private BioportalConfig bioportalConfig;
  @JsonProperty("citationServices")
  private CitationServicesConfig metadataServicesConfig;


  public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
    return swaggerBundleConfiguration;
  }

  public CoreConfig getCoreConfig() {
    return coreConfig;
  }

  public CedarConfig getCedarConfig() {
    return cedarConfig;
  }

  public BioportalConfig getBioportalConfig() { return bioportalConfig; }

  public CitationServicesConfig getMetadataServicesConfig() { return metadataServicesConfig; }
}
