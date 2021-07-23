package org.metadatacenter.fairware.resources;

import com.codahale.metrics.annotation.Timed;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Produces(MediaType.APPLICATION_JSON)
public class FairwareWorkbenchResource {

  private static final Logger logger = LoggerFactory.getLogger(FairwareWorkbenchResource.class);
  private final CedarService cedarService;

  public FairwareWorkbenchResource(CedarService cedarService) {
    this.cedarService = cedarService;
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

  @GET
  @Path("/tos")
  @Operation(hidden = true, summary = "Terms of service")
  @Timed
  public Response tos() {
    String tos = "The FAIRware Workbench (\"the Service\") is provided by the Stanford Center for Biomedical Informatics " +
        "Research (\"the Provider\"). The Provider authorizes you to access and use the Service under the conditions " +
        "set forth below.\n" +
        "\n" +
        "YOU ACKNOWLEDGE THAT THE SERVICE IS EXPERIMENTAL AND ACADEMIC IN NATURE, AND IS NOT LICENSED BY ANY " +
        "REGULATORY BODY. IT IS PROVIDED \"AS-IS\" WITHOUT WARRANTY OF" +
        " ANY KIND. THE PROVIDER MAKES NO REPRESENTATIONS OR WARRANTIES CONCERNING THE SERVICE OR ANY OTHER " +
        "MATTER WHATSOEVER, INCLUDING WITHOUT LIMITATION ANY EXPRESS, IMPLIED OR STATUTORY WARRANTIES OF " +
        "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT OF THIRD PARTY RIGHTS, TITLE, " +
        "ACCURACY, COMPLETENESS OR ARISING OUT OF COURSE OF CONDUCT OR TRADE CUSTOM OR USAGE, AND DISCLAIMS ALL " +
        "SUCH EXPRESS, IMPLIED OR STATUTORY WARRANTIES. THE PROVIDER MAKES NO WARRANTY OR REPRESENTATION THAT " +
        "YOUR USE OF THE SERVICE WILL NOT INFRINGE UPON THE INTELLECTUAL PROPERTY OR OTHER RIGHTS OF ANY THIRD " +
        "PARTY. FURTHER, THE PROVIDER SHALL NOT BE LIABLE IN ANY MANNER WHATSOEVER FOR ANY DIRECT, INDIRECT, " +
        "INCIDENTAL, SPECIAL, CONSEQUENTIAL OR EXEMPLARY DAMAGES ARISING OUT OF OR IN ANY WAY RELATED TO THE " +
        "SERVICE, THE USE OF, OR INABILITY TO USE, ANY OF THE INFORMATION OR DATA CONTAINED OR REFERENCED IN THE " +
        "SERVICE OR ANY INFORMATION OR DATA THAT IS PROVIDED THROUGH LINKED WEBSITES, OR ANY OTHER MATTER. THE " +
        "FOREGOING EXCLUSIONS AND LIMITATIONS SHALL APPLY TO ALL CLAIMS AND ACTIONS OF ANY KIND AND ON ANY THEORY" +
        " OF LIABILITY, WHETHER BASED ON CONTRACT, TORT OR ANY OTHER GROUNDS, AND REGARDLESS OF WHETHER A PARTY " +
        "HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, AND NOTWITHSTANDING ANY FAILURE OF ESSENTIAL " +
        "PURPOSE OF ANY LIMITED REMEDY. BY USING THE SERVICE, YOU FURTHER AGREE THAT EACH WARRANTY DISCLAIMER, " +
        "EXCLUSION OF DAMAGES OR OTHER LIMITATION OF LIABILITY HEREIN IS INTENDED TO BE SEVERABLE AND INDEPENDENT" +
        " OF THE OTHER CLAUSES OR SENTENCES BECAUSE THEY EACH REPRESENT SEPARATE ELEMENTS OF RISK ALLOCATION " +
        "BETWEEN THE PARTIES.\n" +
        "\n" +
        "BY USING THE SERVICE, YOU ACKNOWLEDGE AND AGREE THAT YOU HAVE READ, UNDERSTOOD AND AGREE TO ALL OF THESE" +
        " TERMS OF USE.";
    return Response.ok(tos).build();
  }
}
