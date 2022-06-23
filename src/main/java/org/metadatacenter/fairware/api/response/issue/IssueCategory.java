package org.metadatacenter.fairware.api.response.issue;

public enum IssueCategory {

  FIELD_ERROR("Field error"),

  VALUE_ERROR("Value error");

  private final String description;

  IssueCategory(String description) {
    this.description = description;
  }
}
