package org.metadatacenter.fairware;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
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
