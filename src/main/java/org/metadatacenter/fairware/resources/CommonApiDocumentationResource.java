package org.metadatacenter.fairware.resources;


import com.codahale.metrics.annotation.Timed;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(
    info = @Info(
        title = "FAIRware Workbench API",
        version = "prototype",
        description = "This API provides several endpoints to assess and enhance metadata quality based on <a href=\"https://www.go-fair.org/fair-principles/\">the FAIR guiding principles</a>.",
        termsOfService = "/tos",
        //license = @License(name = "Apache 2.0 License", url = "https://www.apache.org/licenses/LICENSE-2.0"),
        contact = @Contact(name = " ", email = "marcosmr@stanford.edu")
    )
)

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class CommonApiDocumentationResource {

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
