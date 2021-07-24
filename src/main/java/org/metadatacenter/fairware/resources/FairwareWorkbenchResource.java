package org.metadatacenter.fairware.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.request.AlignMetadataRequest;
import org.metadatacenter.fairware.api.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.response.AlignMetadataResponse;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.services.MetadataService;
import org.metadatacenter.fairware.core.services.TemplateService;
import org.metadatacenter.fairware.core.services.external.CedarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FairwareWorkbenchResource {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchResource.class);
  private final TemplateService templateService;
  private final MetadataService metadataService;

  public FairwareWorkbenchResource(TemplateService templateService, MetadataService metadataService) {
    this.templateService = templateService;
    this.metadataService = metadataService;
  }

  @POST
  @Operation(
      summary = "Searches the CEDAR repository for templates that match the field names in an input metadata record.")
  @Path("/templates/recommend")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Templates")
  @RequestBody(description = "Metadata record", required = true,
      content = @Content(
          schema = @Schema(implementation = RecommendTemplatesRequest.class),
      examples = {
          @ExampleObject(value = "{\"metadataRecord\":{" +
              "\"disease\":\"influenza\"," +
              "\"tissue\":\"lung\"}}")
      }
  ))
  @ApiResponse(
      responseCode = "200",
      description = "voila!",
      content = @Content(
          schema = @Schema(implementation = RecommendTemplatesResponse.class),
          examples = {
              @ExampleObject(name = "boo", value = "example",
                  summary = "example of boo", externalValue = "example of external value")
          }
      ))
  public Response recommendTemplates(RecommendTemplatesRequest request) {

    try {
      RecommendTemplatesResponse recommendations = templateService.recommendCedarTemplates(request.getMetadataRecord());
      return Response.ok(recommendations).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (IOException | HttpException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }

  @POST
  @Operation(
      summary = "Align the metadata field names in an input metadata record with those of a CEDAR metadata template.")
  @Path("/metadata/align")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "Metadata record and CEDAR template identifier", required = true,
      content = @Content(
          schema = @Schema(implementation = AlignMetadataRequest.class),
          examples = {
              @ExampleObject(value = "{\"templateId\":\"https://repo.metadatacenter" +
                  ".orgx/templates/2f56d1cd-b0c9-4103-9cc5-18888dbf5594\"," +
                  "\"metadataRecord\":{\"disease\":\"influenza\",\"tissue\":\"lung\"}}")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "voila!",
      content = @Content(
          schema = @Schema(implementation = RecommendTemplatesResponse.class),
          examples = {
              @ExampleObject(name = "boo", value = "example",
                  summary = "example of boo", externalValue = "example of external value")
          }
      ))
  public Response alignMetadata(AlignMetadataRequest request) {

    try {
      List<FieldAlignment> fieldAlignments =
          metadataService.alignMetadata(request.getTemplateId(), request.getMetadataRecord());
      AlignMetadataResponse results = new AlignMetadataResponse(fieldAlignments.size(), fieldAlignments);
      return Response.ok(results).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (IOException | HttpException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }
}
