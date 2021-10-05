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
import org.metadatacenter.fairware.api.request.EvaluateMetadataRequest;
import org.metadatacenter.fairware.api.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.response.AlignMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.services.MetadataService;
import org.metadatacenter.fairware.core.services.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
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
          @ExampleObject(value = "{\"metadataRecord\":{\"study_id\":\"12811\",\"study title\":\"My Study\",\"contact " +
              "e-mail\":\"john.doe@acme.com\",\"organism\":\"Homo sapiens\",\"age\":76,\"sex\":\"male\"," +
              "\"tissue\":\"liver\",\"platform\":\"Illumina\"}}")
      }
  ))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(
          schema = @Schema(implementation = RecommendTemplatesResponse.class),
          examples = {
              @ExampleObject(value = "{\"totalCount\":0,\"requestSummary\":{\"sourceFieldsCount\":0}," +
                  "\"recommendations\":[{\"recommendationScore\":0,\"sourceFieldsMatched\":0,\"targetFieldsCount\":0," +
                  "\"resourceExtract\":{\"@id\":\"string\",\"schema:identifier\":\"string\"," +
                  "\"schema:name\":\"string\",\"schema:description\":\"string\",\"pav:version\":\"string\"," +
                  "\"bibo:status\":\"string\"}}]}")
          }
      ))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "422", description = "Unprocessable entity")
  @ApiResponse(responseCode = "500", description = "Internal Server Error")
  public Response recommendTemplates(@NotNull @Valid RecommendTemplatesRequest request) {

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
              @ExampleObject(value = "{\"metadataRecord\":{\"study_id\":\"12811\",\"title of study\":\"My Study\"," +
                  "\"contact e-mail\":\"john.doe@acme.com\",\"organism\":\"Homo sapiens\",\"age\":76," +
                  "\"sex\":\"male\",\"tissue\":\"liver\",\"platform\":\"Illumina\"},\"templateId\":\"https://repo" +
                  ".metadatacenter.orgx/templates/262cac6c-4245-4ce3-90d2-122a488c36cd\"}")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(
          schema = @Schema(implementation = AlignMetadataResponse.class),
          examples = {
              @ExampleObject()
          }
      ))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "422", description = "Unprocessable entity")
  @ApiResponse(responseCode = "500", description = "Internal Server Error")
  public Response alignMetadata(@NotNull @Valid AlignMetadataRequest request) {

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

  @POST
  @Operation(
      summary = "Evaluate an input metadata record based on a given CEDAR template and a list of metadata-template alignments.")
  @Path("/metadata/evaluate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "Metadata record and CEDAR template identifier", required = true,
      content = @Content(
          schema = @Schema(implementation = FieldAlignment.class),
          examples = {
              @ExampleObject(value = "")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataResponse.class),
          examples = {
              @ExampleObject()
          }
      ))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "422", description = "Unprocessable entity")
  @ApiResponse(responseCode = "500", description = "Internal Server Error")
  public Response evaluateMetadata(@NotNull @Valid EvaluateMetadataRequest request) {

    try {

      List<EvaluationReportItem> reportItems =
          metadataService.evaluateMetadata(request.getTemplateId(), request.getMetadataRecord(), request.getFieldAlignments());
      EvaluateMetadataResponse report = new EvaluateMetadataResponse(request.getTemplateId(),
          LocalDateTime.now(), request.getMetadataRecord(), reportItems);
      return Response.ok(report).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (IOException | HttpException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }


}
