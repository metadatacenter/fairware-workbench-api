package org.metadatacenter.fairware;

import in.vectorpro.dropwizard.swagger.SwaggerBundle;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.metadatacenter.fairware.core.services.MetadataService;
import org.metadatacenter.fairware.core.services.TemplateService;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.resources.CommonApiDocumentationResource;
import org.metadatacenter.fairware.resources.FairwareWorkbenchResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class FairwareWorkbenchApiApplication extends Application<FairwareWorkbenchApiConfiguration> {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchApiApplication.class);

  public static void main(final String[] args) throws Exception {
    logger.info("Starting FAIRware Workbench API");
    new FairwareWorkbenchApiApplication().run(args);
  }

  @Override
  public String getName() {
    return "FAIRware Workbench API";
  }

  @Override
  public void initialize(final Bootstrap<FairwareWorkbenchApiConfiguration> bootstrap) {
    // Swagger initialization
    bootstrap.addBundle(new SwaggerBundle<FairwareWorkbenchApiConfiguration>() {
      @Override
      protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(FairwareWorkbenchApiConfiguration configuration) {
        return configuration.getSwaggerBundleConfiguration();
      }
    });
    // Serve static files
    //bootstrap.addBundle(new AssetsBundle("/examples/", "/examples/"));
  }

  @Override
  public void run(final FairwareWorkbenchApiConfiguration configuration, final Environment environment) {

    // Enable CORS headers
    final FilterRegistration.Dynamic cors =
        environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    // Register resources
    final CommonApiDocumentationResource commonApiDocumentationResource = new CommonApiDocumentationResource();
    environment.jersey().register(commonApiDocumentationResource);

    CedarService cedarService = new CedarService(configuration.getCedarConfig());
    BioportalService bioportalService = new BioportalService(configuration.getBioportalConfig());
    TemplateService templateService = new TemplateService(cedarService);
    MetadataService metadataService = new MetadataService(cedarService, bioportalService, configuration.getCoreConfig(), configuration.getBioportalConfig());
    final FairwareWorkbenchResource fairwareWorkbenchResource =
        new FairwareWorkbenchResource(templateService, metadataService);
    environment.jersey().register(fairwareWorkbenchResource);
  }
}
