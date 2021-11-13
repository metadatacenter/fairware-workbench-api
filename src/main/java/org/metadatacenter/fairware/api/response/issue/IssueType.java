package org.metadatacenter.fairware.api.response.issue;

public enum IssueType {

  MISSING_REQUIRED_VALUE("Missing required value"),
  FIELD_NOT_FOUND_IN_TEMPLATE("Metadata field not found in template");

  private final String description;

  IssueType(String description) {
    this.description = description;
  }

}
