package org.metadatacenter.fairware.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.FairwareWorkbenchApiConfiguration;
import org.metadatacenter.fairware.api.recommendation.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.recommendation.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.core.MetadataEvaluationService;
import org.metadatacenter.fairware.core.util.CedarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
@Api("/")
@Produces(MediaType.APPLICATION_JSON)
public class FairwareWorkbenchResource {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchResource.class);
  private final FairwareWorkbenchApiConfiguration configuration;
  private final CedarService cedarService;

  public FairwareWorkbenchResource(FairwareWorkbenchApiConfiguration configuration, CedarService cedarService) {
    this.configuration = configuration;
    this.cedarService = cedarService;
  }

  @POST
  @ApiOperation("Searches the CEDAR template repository for templates that match the field names in an input metadata record.")
  @Path("/recommend-templates")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Timed
  public Response recommendTemplates(RecommendTemplatesRequest request) {

    MetadataEvaluationService service = new MetadataEvaluationService(cedarService);

    try {
      RecommendTemplatesResponse recommendations = service.recommendCedarTemplates(request);
      return Response.ok(recommendations).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (IOException | HttpException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }
}
