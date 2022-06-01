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
import org.metadatacenter.fairware.api.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.response.AlignMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.response.evaluationReport.EvaluationReportResponse;
import org.metadatacenter.fairware.core.services.MetadataService;
import org.metadatacenter.fairware.core.services.TemplateService;
import org.metadatacenter.fairware.core.util.CedarUtil;
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
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FairwareWorkbenchResource {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchResource.class);

  private final TemplateService templateService;
  private final MetadataService metadataService;

  public FairwareWorkbenchResource(@Nonnull TemplateService templateService,
                                   @Nonnull MetadataService metadataService) {
    this.templateService = checkNotNull(templateService);
    this.metadataService = checkNotNull(metadataService);
  }

  @POST
  @Operation(summary = "Search CEDAR templates that closely match with the given metadata record.")
  @Path("/templates/recommend")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Templates")
  @RequestBody(description = "A JSON object containing the metadata record", required = true,
      content = @Content(
          schema = @Schema(implementation = RecommendTemplatesRequest.class)
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
  public Response recommendTemplates(@NotNull @Valid RecommendTemplatesRequest request) {

    try {
      RecommendTemplatesResponse recommendations = templateService.recommendCedarTemplates(request.getMetadataRecord());
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
  @Operation(summary = "Align the metadata fields in the input metadata record with those in the CEDAR template.")
  @Path("/metadata/align")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A JSON object containing: 1) CEDAR template id and 2) metadata record object. " +
      "The template fields are collected from the CEDAR template and the metadata fields are collected from " +
      "the input metadata record.", required = true,
      content = @Content(
          schema = @Schema(implementation = AlignMetadataRequest.class)
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
      var fieldAlignments = metadataService.alignMetadata(
          request.getTemplateId(),
          request.getMetadataRecord());
      AlignMetadataResponse results = AlignMetadataResponse.create(fieldAlignments);
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
  @Operation(summary = "Evaluate the given metadata record based on the specification in the CEDAR template.")
  @Path("/metadata/evaluate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A JSON object containing: 1) CEDAR template id and 2a) CEDAR template instance id or " +
      "2b) metadata record object", required = true,
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataRequest.class)
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

  @POST
  @Operation(summary = "Search for a publicly-available, DOI-associated metadata record.")
  @Path("/metadata/search")
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A list of DOI strings", required = true,
      content = @Content(
          schema = @Schema(implementation = List.class),
          examples = {
              @ExampleObject(value = "[\"10.5061/dryad.rm2n805\",\"10.4230/lipics.iclp.2011.16\"]")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "Response showing the metadata records that are associated with the given DOI strings",
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataResponse.class)
      ))
  @ApiResponse(responseCode = "400", description = "The request could not be understood by the server due to " +
      "malformed syntax in the request body.")
  @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition that prevented " +
      "it from fulfilling the request.")
  public Response searchMetadata(@NotNull @Valid ImmutableList<String> metadataRecordIds) {
    try {
      var results = metadataService.searchMetadata(metadataRecordIds);
      return Response.ok(results).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }

  private EvaluateMetadataResponse getEvaluateMetadataResponse(EvaluateMetadataRequest request)
      throws IOException, HttpException, BadRequestException {
    var metadataRecordId = request.getMetadataRecordId();
    var metadataRecord = getMetadataRecord(request);
    var templateId = getTemplateId(request, metadataRecord);
    var fieldAlignments = metadataService.alignMetadata(templateId, metadataRecord);
    return metadataService.evaluateMetadata(
        metadataRecordId,
        metadataRecord,
        templateId,
        fieldAlignments);
  }

  private ImmutableMap<String, Object> getMetadataRecord(EvaluateMetadataRequest request) throws IOException {
    var metadataRecordId = request.getMetadataRecordId();
    if (metadataRecordId.isPresent()) {
      return getMetadataRecordFromId(metadataRecordId.get());
    }
    var metadataRecord = request.getMetadataRecord();
    if (!metadataRecord.isPresent()) {
      throw new BadRequestException("Metadata record is not provided or not found");
    }
    return metadataRecord.get();
  }

  private String getTemplateId(EvaluateMetadataRequest request, ImmutableMap<String, Object> metadataRecord)
      throws IOException {
    var templateId = request.getTemplateId();
    if (!templateId.isPresent()) {
      templateId = getTemplateIdFromMetadataRecord(metadataRecord);
      if (!templateId.isPresent()) {
        throw new BadRequestException("Template ID is not provide or not found");
      }
    }
    return templateId.get();
  }

  /* Helper functions */
  public ImmutableMap<String, Object> getMetadataRecordFromId(String metadataRecordId) throws IOException {
    return metadataService.searchMetadata(metadataRecordId).getMetadataRecord();
  }

  private Optional<String> getTemplateIdFromMetadataRecord(@Nonnull ImmutableMap<String, Object> metadataRecord)
      throws IOException {
    // First attempt is by getting it from the metadata object
    var templateId = CedarUtil.getTemplateId(metadataRecord);
    if (templateId.isPresent()) {
      return templateId;
    }
    // Second attempt is by getting it from the template recommendation service
    var recommendTemplatesResponse = templateService.recommendCedarTemplates(metadataRecord);
    var candidateTemplates = recommendTemplatesResponse.getRecommendations();
    if (!candidateTemplates.isEmpty()) {
      var cedarId = candidateTemplates.get(0).getTemplateExtract().getCedarId();  // return top-ranked template
      return Optional.of(cedarId);
    }
    // Give up!
    return Optional.empty();
  }
}
