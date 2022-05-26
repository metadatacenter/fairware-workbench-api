package org.metadatacenter.fairware.resources;

import com.google.common.collect.ImmutableMap;
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
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
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
import java.util.Map;
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
              @ExampleObject(value = "{\"metadataRecord\":{\"study_id\":\"12811\",\"study title\":\"My Study\"," +
                  "\"contact " +
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
              @ExampleObject(value = "{\"totalCount\":2,\"requestSummary\":{\"sourceFieldsCount\":3}," +
                  "\"recommendations\":[{\"recommendationScore\":0.5,\"sourceFieldsMatched\":2," +
                  "\"targetFieldsCount\":3,\"templateExtract\":{\"@id\":\"https://repo.metadatacenter" +
                  ".orgx/templates/f76ad487-43a2-4693-97cd-0aaca90e85a8\",\"schema:identifier\":null," +
                  "\"schema:name\":\"Research Study\",\"schema:description\":\"\",\"pav:version\":\"0.0.1\"," +
                  "\"bibo:status\":\"bibo:draft\"}},{\"recommendationScore\":0.16666666666666666," +
                  "\"sourceFieldsMatched\":1,\"targetFieldsCount\":4,\"templateExtract\":{\"@id\":\"https://repo" +
                  ".metadatacenter.orgx/templates/82ceb37e-7edd-4dd1-b541-d55311de62bb\",\"schema:identifier\":null," +
                  "\"schema:name\":\"Study Template\",\"schema:description\":\"\",\"pav:version\":\"0.0.1\"," +
                  "\"bibo:status\":\"bibo:draft\"}}]}")
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
    } catch (IOException e) {
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
              @ExampleObject(value = "{\"totalCount\":3,\"fieldAlignments\":[{\"similarityScore\":0.91," +
                  "\"metadataFieldPath\":\"study_id\",\"templateFieldPath\":\"Study ID\"},{\"similarityScore\":0.85," +
                  "\"metadataFieldPath\":\"title of study\",\"templateFieldPath\":\"Study title\"}," +
                  "{\"similarityScore\":1,\"metadataFieldPath\":\"organism\",\"templateFieldPath\":\"Organism\"}]}")
          }
      ))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "422", description = "Unprocessable entity")
  @ApiResponse(responseCode = "500", description = "Internal Server Error")
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
  @Operation(
      summary = "Evaluate an input metadata record based on a given CEDAR template and a list of metadata-template " +
          "alignments.")
  @Path("/metadata/evaluate")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "Template identifier, metadata record, and field alignments", required = true,
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataRequest.class),
          examples = {
              @ExampleObject(value = "{\"templateId\":\"https://repo.metadatacenter" +
                  ".orgx/templates/262cac6c-4245-4ce3-90d2-122a488c36cd\",\"metadataRecord\":{\"study_id\":\"12811\"," +
                  "\"title of study\":\"\",\"contact e-mail\":\"john.doe@acme.com\",\"organism\":\"Homo " +
                  "sapiens\",\"age\":76,\"sex\":\"male\",\"tissue\":\"liver\",\"platform\":\"Illumina\"}," +
                  "\"fieldAlignments\":[{\"similarityScore\":0.91,\"metadataFieldPath\":\"study_id\"," +
                  "\"templateFieldPath\":\"Study ID\"},{\"similarityScore\":0.85,\"metadataFieldPath\":\"title of " +
                  "study\",\"templateFieldPath\":\"Study title\"},{\"similarityScore\":1," +
                  "\"metadataFieldPath\":\"organism\",\"templateFieldPath\":\"Organism\"}]}")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataResponse.class),
          examples = {
              @ExampleObject(value = "{\"templateId\":\"https://repo.metadatacenter" +
                  ".orgx/templates/262cac6c-4245-4ce3-90d2-122a488c36cd\",\"metadataRecord\":{\"study_id\":\"12811\"," +
                  "\"title of study\":\"\",\"contact e-mail\":\"john.doe@acme.com\",\"organism\":\"Homo sapiens\"," +
                  "\"age\":76,\"sex\":\"male\",\"tissue\":\"liver\",\"platform\":\"Illumina\"}," +
                  "\"fieldAlignments\":[{\"similarityScore\":0.91,\"metadataFieldPath\":\"study_id\"," +
                  "\"templateFieldPath\":\"Study ID\"},{\"similarityScore\":0.85,\"metadataFieldPath\":\"title of " +
                  "study\",\"templateFieldPath\":\"Study title\"},{\"similarityScore\":1," +
                  "\"metadataFieldPath\":\"organism\",\"templateFieldPath\":\"Organism\"}]," +
                  "\"items\":[{\"metadataFieldPath\":\"title of study\",\"issue\":\"MISSING_REQUIRED_VALUE\"}]," +
                  "\"generatedOn\":\"2021-10-06 15:13:16\"}")
          }
      ))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "422", description = "Unprocessable entity")
  @ApiResponse(responseCode = "500", description = "Internal Server Error")
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
  @Operation(
      summary = "Searches for publicly-available, doi-associated metadata.")
  @Path("/metadata/search")
  @Produces(MediaType.APPLICATION_JSON)
  @Tag(name = "Metadata")
  @RequestBody(description = "A metadata record identifier (e.g., DOI)", required = true,
      content = @Content(
          schema = @Schema(implementation = String.class),
          examples = {
              @ExampleObject(value = "10.15468/9vuieb")
          }
      ))
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(
          schema = @Schema(implementation = EvaluateMetadataResponse.class),
          examples = {
              @ExampleObject(value = "")
          }
      ))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "422", description = "Unprocessable entity")
  @ApiResponse(responseCode = "500", description = "Internal Server Error")
  public Response searchMetadata(@NotNull @Valid String metadataRecordId) {
    try {
      var results = metadataService.searchMetadata(metadataRecordId);
      return Response.ok(results).build();
    } catch (BadRequestException e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.BAD_REQUEST).build();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
    }
  }

//  @POST
//  @Operation(
//      summary = "")
//  @Path("/metadata/report")
//  @Consumes(MediaType.APPLICATION_JSON)
//  @Produces(MediaType.APPLICATION_JSON)
//  @Tag(name = "Metadata")
//  @RequestBody(description = "", required = true,
//      content = @Content(
//          schema = @Schema(implementation = EvaluateMetadataRequest.class),
//          examples = {
//              @ExampleObject(value = "")
//          }
//      ))
//  @ApiResponse(
//      responseCode = "200",
//      description = "OK",
//      content = @Content(
//          schema = @Schema(implementation = EvaluateMetadataResponse.class),
//          examples = {
//              @ExampleObject(value = "")
//          }
//      ))
//  @ApiResponse(responseCode = "400", description = "Bad request")
//  @ApiResponse(responseCode = "422", description = "Unprocessable entity")
//  @ApiResponse(responseCode = "500", description = "Internal Server Error")
//  public Response evaluationReport(@NotNull @Valid EvaluationReportRequest request) {
//    try {
//      // Evaluate record by record
//      var evaluationResponses = request.getEvaluateMetadataRequests()
//          .stream()
//          .map(this::getEvaluateMetadataResponse)
//          .collect(ImmutableList.toImmutableList());
//      var report = metadataService.generateEvaluationReport(evaluationResponses);
//      return Response.ok(report).build();
//    } catch (BadRequestException e) {
//      logger.error(e.getMessage());
//      return Response.status(Response.Status.BAD_REQUEST).build();
//    } catch (HttpException | IOException e) {
//      logger.error(e.getMessage());
//      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
//    }
//  }

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

  private Optional<String> getTemplateIdFromMetadataRecord(@Nonnull Map<String, Object> metadataRecord)
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
