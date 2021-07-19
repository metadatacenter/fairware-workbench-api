package org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarTemplateExtract {

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
  @JsonProperty("ownedByUserName")
  private String ownedByCedarUserName;
  @JsonProperty("createdByUserName")
  private String createdByCedarUserName;
  @JsonProperty("lastUpdatedByUserName")
  private String lastUpdatedByCedarUserName;
  @JsonProperty("pav:createdOn")
  private Date createdOn;
  @JsonProperty("pav:lastUpdatedOn")
  private Date lastUpdatedOn;

  public CedarTemplateExtract() { }

  public CedarTemplateExtract(String cedarId, String id, String name, String description, String version,
                              String status, String ownedByCedarUserName, String createdByCedarUserName,
                              String lastUpdatedByCedarUserName, Date createdOn, Date lastUpdatedOn) {
    this.cedarId = cedarId;
    this.id = id;
    this.name = name;
    this.description = description;
    this.version = version;
    this.status = status;
    this.ownedByCedarUserName = ownedByCedarUserName;
    this.createdByCedarUserName = createdByCedarUserName;
    this.lastUpdatedByCedarUserName = lastUpdatedByCedarUserName;
    this.createdOn = createdOn;
    this.lastUpdatedOn = lastUpdatedOn;
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

  public String getOwnedByCedarUserName() {
    return ownedByCedarUserName;
  }

  public String getCreatedByCedarUserName() {
    return createdByCedarUserName;
  }

  public String getLastUpdatedByCedarUserName() {
    return lastUpdatedByCedarUserName;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Date getLastUpdatedOn() {
    return lastUpdatedOn;
  }
}
