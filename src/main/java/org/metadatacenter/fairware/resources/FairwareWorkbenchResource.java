package org.metadatacenter.fairware.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.request.AlignMetadataRequest;
import org.metadatacenter.fairware.api.request.EvaluateMetadataRequest;
import org.metadatacenter.fairware.api.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.request.SearchMetadataRequest;
import org.metadatacenter.fairware.api.response.AlignMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.response.issue.IssueLevel;
import org.metadatacenter.fairware.api.response.search.SearchMetadataResponse;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.services.MetadataService;
import org.metadatacenter.fairware.core.services.TemplateService;
import org.metadatacenter.fairware.core.services.citation.CitationService;
import org.metadatacenter.fairware.core.services.citation.DataCiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.*;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FairwareWorkbenchResource {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchResource.class);
  private final TemplateService templateService;
  private final MetadataService metadataService;

  public FairwareWorkbenchResource(TemplateService templateService,
                                   MetadataService metadataService) {
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
      // Input validation
      if (StringUtils.isEmpty(request.getMetadataRecordId()) && request.getMetadataRecord() == null) {
        logger.error("You must provide a metadataRecordId or a metadataRecord");
        return Response.status(Response.Status.BAD_REQUEST).build();
      }
      // If the metadataRecordId is provided, find the metadata record
      Map<String, Object> metadataRecord;
      if (!StringUtils.isEmpty(request.getMetadataRecordId())) {
        SearchMetadataResponse searchResults =
            metadataService.searchMetadata(Arrays.asList(request.getMetadataRecordId()));
        if (searchResults.getItems().size() == 0) {
          logger.error("Couldn't find metadata record: metadataRecordId=" + request.getMetadataRecordId());
          return Response.status(Response.Status.NOT_FOUND).build();
        }
        metadataRecord = searchResults.getItems().get(0).getMetadata();
      }
      else {
        metadataRecord = request.getMetadataRecord();
      }
      // Read field alignments from the request, if provided
      List<FieldAlignment> fieldAlignments = new ArrayList<>();
      if (request.getFieldAlignments() != null && request.getFieldAlignments().size() > 0) {
        fieldAlignments = request.getFieldAlignments();
      }

      List<EvaluationReportItem> reportItems =
          metadataService.evaluateMetadata(request.getTemplateId(), metadataRecord, fieldAlignments);
      // Count errors and warnings
      int warningsCount = 0;
      int errorsCount = 0;
      for (EvaluationReportItem item : reportItems) {
        if (item.getIssue().getIssueLevel().equals(IssueLevel.WARNING)) { warningsCount++; }
        else if (item.getIssue().getIssueLevel().equals(IssueLevel.ERROR)) { errorsCount++; }
        else throw new InvalidParameterException("Invalid issue type");
      }

      EvaluateMetadataResponse report = new EvaluateMetadataResponse(request.getMetadataRecordId(),
          request.getTemplateId(), request.getMetadataRecord(), reportItems.size(), warningsCount, errorsCount,
          reportItems, LocalDateTime.now());
         
      return Response.ok(report).build();
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
  @RequestBody(description = "List of DOIs", required = true,
      content = @Content(
          schema = @Schema(implementation = List.class),
          examples = {
              @ExampleObject(value = "[\"10.15468/9vuieb\"]")
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
  public Response searchMetadata(@NotNull @Valid List<String> uris) {
    try {
      SearchMetadataResponse results = metadataService.searchMetadata(uris);
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
