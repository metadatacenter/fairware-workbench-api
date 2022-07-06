package org.metadatacenter.fairware.shared;

public enum IssueCategory {

  FIELD_ERROR("Field error"),

  VALUE_ERROR("Value error");

  private final String description;

  IssueCategory(String description) {
    this.description = description;
  }
}
