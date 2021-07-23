package org.metadatacenter.fairware.resources;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

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
public class OpenApiDefinitionResource { }
