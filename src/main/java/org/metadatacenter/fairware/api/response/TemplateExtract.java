package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateExtract {

  @JsonProperty("@id")
  private String cedarId;
  @JsonProperty("schema:identifier")
  private String id;
  @JsonProperty("schema:name")
  private String name;
  @JsonProperty("schema:description")
  private String description;
  @JsonProperty("pav:version")
  private String version;
  @JsonProperty("bibo:status")
  private String status;

  public TemplateExtract() {
  }

  public TemplateExtract(String cedarId, String id, String name, String description, String version, String status) {
    this.cedarId = cedarId;
    this.id = id;
    this.name = name;
    this.description = description;
    this.version = version;
    this.status = status;
  }

  public String getCedarId() {
    return cedarId;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getVersion() {
    return version;
  }

  public String getStatus() {
    return status;
  }
}
