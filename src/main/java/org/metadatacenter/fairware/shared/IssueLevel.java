package org.metadatacenter.fairware.shared;

public enum IssueLevel {

  ERROR("Error"),
  WARNING("Warning");

  private final String description;

  IssueLevel(String description) {
    this.description = description;
  }

}
