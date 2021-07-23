package org.metadatacenter.fairware;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Configuration;
import org.metadatacenter.fairware.config.cedar.CedarConfig;

public class FairwareWorkbenchApiConfiguration extends Configuration {

  @JsonProperty("swagger")
  public SwaggerBundleConfiguration swaggerBundleConfiguration;

  @JsonProperty("cedar")
  public CedarConfig cedar;

  public CedarConfig getCedar() {
    return cedar;
  }
}
