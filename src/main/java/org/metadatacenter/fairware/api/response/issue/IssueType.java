package org.metadatacenter.fairware.api.response.issue;

public enum IssueType {

  MISSING_REQUIRED_VALUE("Missing required value"),
  MISSING_OPTIONAL_VALUE("Missing optional value"),
  INVALID_VALUE_REPRESENTATION("Invalid value representation in JSON document"),
  INVALID_DATE_TIME_FORMAT("Invalid date-time format"),
  INVALID_DATE_FORMAT("Invalid date format"),
  INVALID_TIME_FORMAT("Invalid time format"),
  UNABLE_PARSE_STRING_TO_NUMBER("Unable parse string to number"),
  VALUE_NOT_ONTOLOGY_TERM("Value is not an ontology term"),
  FIELD_NOT_FOUND_IN_TEMPLATE("Metadata field not found in template");

  private final String description;

  IssueType(String description) {
    this.description = description;
  }
}
