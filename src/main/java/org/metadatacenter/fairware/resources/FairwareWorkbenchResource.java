package org.metadatacenter.fairware.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
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
import org.metadatacenter.fairware.api.request.EvaluationReportRequest;
import org.metadatacenter.fairware.api.response.alignment.AlignMetadataResponse;
import org.metadatacenter.fairware.api.response.alignment.AlignmentReport;
import org.metadatacenter.fairware.api.response.evaluation.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.recommendation.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.response.search.SearchMetadataResponse;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
import org.metadatacenter.fairware.core.services.FairwareService;
import org.metadatacenter.fairware.core.services.MetadataService;
import org.metadatacenter.fairware.core.services.TemplateService;
import org.metadatacenter.fairware.shared.MetadataSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FairwareWorkbenchResource {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchResource.class);

  private final MetadataService metadataService;
  private final TemplateService templateService;
  private final FairwareService fairwareService;

  public FairwareWorkbenchResource(@Nonnull MetadataService metadataService,
                                   @Nonnull TemplateService templateService,
                                   @Nonnull FairwareService fairwareService) {
    this.metadataService = checkNotNull(metadataService);
    this.templateService = checkNotNull(templateService);
    this.fairwareService = checkNotNull(fairwareService);
  }

  @POST
  @Operation(summary = "Search CEDAR templates that closely match with the metadata record given its identifier.")
  @Path("/template/recommend")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Template")
  @RequestBody(description = "A JSON object containing the metadata record", required = true,
      content = @Content(
          schema = @Schema(implementation = String.class),
          examples = {
              @ExampleObject(value = "SAMN01821557")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "Response showing a list of recommended CEDAR templates sorted from the highest recommendation " +
          "score to the lowest.",
      content = @Content(
          schema = @Schema(implementation = RecommendTemplatesResponse.class)
      ))
  @ApiResponse(responseCode = "400", description = "The request could not be understood by the server due to " +
      "malformed syntax in the request body.")
  @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented " +
      "it from fulfilling the request.")
  public Response recommendTemplatesByMetadataId(@NotNull @Valid String metadataId) {
    try {
      var metadata = metadataService.getMetadataById(metadataId);
      var metadataRecord = metadata.getMetadataRecord();
      var recommendations = templateService.recommendCedarTemplates(metadataRecord);
      return Response.ok(recommendations).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (IOException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }

  @POST
  @Operation(summary = "Align the field names found in the metadata record and in the CEDAR template.")
  @Path("/metadata/align")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A JSON object containing: 1) CEDAR template id and 2) metadata record id. " +
      "The template fields are collected from the CEDAR template and the metadata fields are collected from " +
      "the metadata record id.", required = true,
      content = @Content(
          schema = @Schema(implementation = AlignMetadataRequest.class),
          examples = {
              @ExampleObject(value = "{" +
                  "\"templateId\": \"https://repo.metadatacenter.org/templates/db57119c-7860-4569-a3c0-2ced0e0364d1\"," +
                  "\"metadataId\": \"SAMN01821557\""
              )
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "Response showing a list of aligned fields between the source template and target metadata. " +
          "A similarity score of 0.0 indicates that no alignment was found whilst a score of 1.0 means that a perfect " +
          "alignment was found.",
      content = @Content(
          schema = @Schema(implementation = AlignMetadataResponse.class)
      ))
  @ApiResponse(responseCode = "400", description = "The request could not be understood by the server due to " +
      "malformed syntax in the request body.")
  @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented " +
      "it from fulfilling the request.")
  public Response alignMetadata(@NotNull @Valid AlignMetadataRequest request) {
    try {
      var metadata = metadataService.getMetadataById(request.getMetadataId());
      var template = templateService.getCedarTemplateById(request.getTemplateId());
      var alignments = fairwareService.alignMetadata(metadata, template);
      return Response.ok(alignments).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (IOException | HttpException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }

  @POST
  @Operation(summary = "Evaluate the given metadata record based on the specification in the CEDAR template.")
  @Path("/metadata/evaluate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A JSON object containing: 1) CEDAR template id and 2a) CEDAR template instance id or " +
      "2b) metadata record object", required = true,
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataRequest.class),
          examples = {
              @ExampleObject(value = "{" +
                  "\"templateId\": \"https://repo.metadatacenter.org/templates/db57119c-7860-4569-a3c0-2ced0e0364d1\"," +
                  "\"metadataId\": \"SAMN01821557\""
              )
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "Response showing the input metadata record, the metadata specification and the evaluation " +
          "reports. The CEDAR template is the metadata specification where it defines how to construct the metadata" +
          "record correctly. The evaluation report describes the issue location, the type of the issue, and the " +
          "action to fix the issue.",
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataResponse.class)
      ))
  @ApiResponse(responseCode = "400", description = "The request could not be understood by the server due to " +
      "malformed syntax in the request body.")
  @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented " +
      "it from fulfilling the request.")
  public Response evaluateMetadata(@NotNull @Valid EvaluateMetadataRequest request) {
    try {
      var response = getEvaluateMetadataResponse(request);
      return Response.ok(response).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (IOException | HttpException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }

  private EvaluateMetadataResponse getEvaluateMetadataResponse(EvaluateMetadataRequest request)
      throws IOException, HttpException, BadRequestException {
    var metadata = metadataService.getMetadataById(request.getMetadataId());
    var template = templateService.getCedarTemplateById(request.getTemplateId());
    var fieldAlignments = request.getFieldAlignments();
    if (fieldAlignments == null) {
      return fairwareService.evaluateMetadata(metadata, template);
    } else {
      return fairwareService.evaluateMetadata(metadata, template, fieldAlignments);
    }
  }

  @POST
  @Operation(summary = "Search for a publicly-available metadata record.")
  @Path("/metadata/search")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A string of metadata indentifier", required = true,
      content = @Content(
          schema = @Schema(implementation = String.class),
          examples = {
              @ExampleObject(value = "10.5061/dryad.rm2n805")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "Response showing the metadata records that are associated with the given identifier.",
      content = @Content(
          schema = @Schema(implementation = SearchMetadataResponse.class)
      ))
  @ApiResponse(responseCode = "400", description = "The request could not be understood by the server due to " +
      "malformed syntax in the request body.")
  @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented " +
      "it from fulfilling the request.")
  public Response searchMetadata(@NotNull @Valid String metadataId) {
    try {
      var metadata = metadataService.getMetadataById(metadataId);
      var response = fairwareService.searchMetadata(metadata);
      return Response.ok(response).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }

  @POST
  @Operation(summary = "Evaluate the metadata in batch and produce the summary report.")
  @Path("/metadata/report")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A list of metadata record and its associated template to evaluate.", required = true,
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataRequest.class),
          examples = {
              @ExampleObject(value = "{" +
                  "\"metadataList\": [{" +
                  "\"templateId\": \"https://repo.metadatacenter.org/templates/6d9f4a83-a7ba-42be-a6af-f3cad7b2f7e3\"," +
                  "\"metadataRecord\": {" +
                  "\"biosample_accession\": \"1234\"," +
                  "\"organism\": \"Homo sapiens\"," +
                  "\"disease\": \"Diabetes\"," +
                  "\"tissue\": \"liver\"," +
                  "\"platform\": \"Illumina\"," +
                  "\"cell_line\": \"\"," +
                  "\"cell_type\": \"\"," +
                  "\"sex\": \"male\"," +
                  "\"age\": \"52 yo\"" +
                  "}" +
                  "}, {" +
                  "\"templateId\": \"https://repo.metadatacenter.org/templates/6d9f4a83-a7ba-42be-a6af-f3cad7b2f7e3\"," +
                  "\"metadataRecord\": {" +
                  "\"biosample_accession\": \"1235\"," +
                  "\"organism\": \"Homo sapiens\"," +
                  "\"disease\": \"Melanoma\"," +
                  "\"tissue\": \"skin\"," +
                  "\"platform\": \"Illumina\"," +
                  "\"cell_line\": \"\"," +
                  "\"cell_type\": \"\"," +
                  "\"sex\": \"\"," +
                  "\"age\": 35" +
                  "}" +
                  "}]" +
                  "}")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "Response showing the the evaluation summary report.",
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataResponse.class)
      ))
  @ApiResponse(responseCode = "400", description = "The request could not be understood by the server due to " +
      "malformed syntax in the request body.")
  @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented " +
      "it from fulfilling the request.")
  public Response evaluationReport(@NotNull @Valid EvaluationReportRequest request) {
    try {
      // Evaluate record by record
      var evaluationResponses = Lists.<EvaluateMetadataResponse>newArrayList();
      for (var evaluationRequest : request.getEvaluateMetadataRequests()) {
        var evaluationResponse = getEvaluateMetadataResponse(evaluationRequest);
        evaluationResponses.add(evaluationResponse);
      }
      var report = fairwareService.generateEvaluationReport(ImmutableList.copyOf(evaluationResponses));
      return Response.ok(report).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (HttpException | IOException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }
}
