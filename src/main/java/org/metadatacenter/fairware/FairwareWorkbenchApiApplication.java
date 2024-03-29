package org.metadatacenter.fairware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import in.vectorpro.dropwizard.swagger.SwaggerBundle;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.metadatacenter.fairware.core.services.FairwareService;
import org.metadatacenter.fairware.core.services.HttpJsonFileParser;
import org.metadatacenter.fairware.core.services.HttpJsonFileService;
import org.metadatacenter.fairware.core.services.HttpRequestHandler;
import org.metadatacenter.fairware.core.services.MetadataService;
import org.metadatacenter.fairware.core.services.TemplateService;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.biosample.BioSampleDataParser;
import org.metadatacenter.fairware.core.services.biosample.BioSampleService;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.core.services.datacite.DataCiteService;
import org.metadatacenter.fairware.core.services.evaluation.ControlledTermEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.DateTimeValueChecker;
import org.metadatacenter.fairware.core.services.evaluation.DateValueChecker;
import org.metadatacenter.fairware.core.services.evaluation.ExtraFieldsEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.NumberValueChecker;
import org.metadatacenter.fairware.core.services.evaluation.OptionalValuesEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.RequiredValuesEvaluator;
import org.metadatacenter.fairware.core.services.evaluation.StringValueChecker;
import org.metadatacenter.fairware.core.services.evaluation.TimeValueChecker;
import org.metadatacenter.fairware.core.services.evaluation.ValueFromOntologyChecker;
import org.metadatacenter.fairware.core.services.evaluation.ValueFromOntologyClassesChecker;
import org.metadatacenter.fairware.core.services.evaluation.DataTypeEvaluator;
import org.metadatacenter.fairware.core.util.MapBasedMetadataContentExtractor;
import org.metadatacenter.fairware.core.util.MetadataContentExtractor;
import org.metadatacenter.fairware.core.domain.CedarTemplateFieldsExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateInstanceContentExtractor;
import org.metadatacenter.fairware.resources.CommonApiDocumentationResource;
import org.metadatacenter.fairware.resources.FairwareWorkbenchResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class FairwareWorkbenchApiApplication extends Application<FairwareWorkbenchApiConfiguration> {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchApiApplication.class);
  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());
  private static final XmlMapper xmlMapper = new XmlMapper();

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
    bootstrap.addBundle(new SwaggerBundle<>() {
      @Override
      protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(FairwareWorkbenchApiConfiguration configuration) {
        return configuration.getSwaggerBundleConfiguration();
      }
    });
  }

  @Override
  public void run(final FairwareWorkbenchApiConfiguration configuration, final Environment environment) {

    // Enable CORS headers
    final var cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    // Register resources
    final var commonApiDocumentationResource = new CommonApiDocumentationResource();
    environment.jersey().register(commonApiDocumentationResource);

    // TODO: Use Dagger for dependency injection
    var requestHandler = new HttpRequestHandler(objectMapper);
    var cedarTemplateFieldsExtractor = new CedarTemplateFieldsExtractor();
    var coreConfig = configuration.getCoreConfig();
    var cedarConfig = configuration.getCedarConfig();
    var bioportalConfig = configuration.getBioportalConfig();
    var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler, cedarTemplateFieldsExtractor);
    var bioportalService = new BioportalService(bioportalConfig);
    var templateService = new TemplateService(cedarService);
    var citationServiceProviders = ImmutableList.of(
        cedarService,
        new DataCiteService(configuration.getMetadataServicesConfig().getDatacite()),
        new BioSampleService(configuration.getMetadataServicesConfig().getNcbi(), new BioSampleDataParser(xmlMapper)),
        new HttpJsonFileService(new HttpJsonFileParser(objectMapper)));
    var metadataService = new MetadataService(citationServiceProviders);
    var mapBasedMetadataContentExtractor = new MapBasedMetadataContentExtractor();
    var cedarTemplateInstanceContentExtractor = new CedarTemplateInstanceContentExtractor();
    var metadataContentExtractor = new MetadataContentExtractor(mapBasedMetadataContentExtractor, cedarTemplateInstanceContentExtractor);
    var requiredValuesEvaluator = new RequiredValuesEvaluator();
    var optionalValuesEvaluator = new OptionalValuesEvaluator();
    var stringValueChecker = new StringValueChecker();
    var numberValueChecker = new NumberValueChecker();
    var dateTimeValueChecker = new DateTimeValueChecker();
    var dateValueChecker = new DateValueChecker();
    var timeValueChecker = new TimeValueChecker();
    var valueTypeEvaluator = new DataTypeEvaluator(stringValueChecker, numberValueChecker,
        dateTimeValueChecker, dateValueChecker, timeValueChecker);
    var extraFieldsEvaluator = new ExtraFieldsEvaluator(bioportalService, coreConfig);
    var valueFromOntologyChecker = new ValueFromOntologyChecker(coreConfig, bioportalService);
    var valueFromOntologyClassesChecker = new ValueFromOntologyClassesChecker();
    var controlledTermEvaluator = new ControlledTermEvaluator(valueFromOntologyChecker, valueFromOntologyClassesChecker);
    var fairwareService = new FairwareService(
        configuration.getCoreConfig(),
        metadataContentExtractor,
        requiredValuesEvaluator,
        optionalValuesEvaluator,
        extraFieldsEvaluator,
        valueTypeEvaluator,
        controlledTermEvaluator);
    final var fairwareWorkbenchResource = new FairwareWorkbenchResource(metadataService, templateService, fairwareService);
    environment.jersey().register(fairwareWorkbenchResource);
  }
}
