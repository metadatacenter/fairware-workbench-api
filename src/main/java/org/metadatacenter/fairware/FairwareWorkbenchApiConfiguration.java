package org.metadatacenter.fairware;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Configuration;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.metadatacenter.fairware.config.cedar.CoreConfig;

public class FairwareWorkbenchApiConfiguration extends Configuration {

  @JsonProperty("swagger")
  private SwaggerBundleConfiguration swaggerBundleConfiguration;
  @JsonProperty("cedar")
  private CedarConfig cedarConfig;
  @JsonProperty("core")
  private CoreConfig coreConfig;

  public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
    return swaggerBundleConfiguration;
  }

  public CedarConfig getCedarConfig() {
    return cedarConfig;
  }

  public CoreConfig getCoreConfig() {
    return coreConfig;
  }
}
