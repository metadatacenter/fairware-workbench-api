package org.metadatacenter.fairware.api.evaluation.output;

public class TemplateInfo {

  private String id;
  private String name;
  private String description;
  private String createdByCedarUserId;
  private String createdByCedarUserName;

  public TemplateInfo() {};

  public TemplateInfo(String id, String name, String description, String createdByCedarUserId,
                      String createdByCedarUserName) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.createdByCedarUserId = createdByCedarUserId;
    this.createdByCedarUserName = createdByCedarUserName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreatedByCedarUserId() {
    return createdByCedarUserId;
  }

  public void setCreatedByCedarUserId(String createdByCedarUserId) {
    this.createdByCedarUserId = createdByCedarUserId;
  }

  public String getCreatedByCedarUserName() {
    return createdByCedarUserName;
  }

  public void setCreatedByCedarUserName(String createdByCedarUserName) {
    this.createdByCedarUserName = createdByCedarUserName;
  }
}
