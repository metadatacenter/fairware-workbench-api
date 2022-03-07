package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import java.util.List;
import java.util.Objects;

public class InfoField {

  private String fieldName;
  private String fieldPrefLabel;
  private List<String> fieldPath;
  private Object fieldValue;
  private String fieldValueUri;

  public InfoField() {}

  public InfoField(String fieldName, String fieldPrefLabel, List<String> fieldPath, Object fieldValue, String fieldValueUri) {
    this.fieldName = fieldName;
    this.fieldPrefLabel = fieldPrefLabel;
    this.fieldPath = fieldPath;
    this.fieldValue = fieldValue;
    this.fieldValueUri = fieldValueUri;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getFieldPrefLabel() {
    return fieldPrefLabel;
  }

  public List<String> getFieldPath() {
    return fieldPath;
  }

  public Object getFieldValue() {
    return fieldValue;
  }

  public String getFieldValueUri() {
    return fieldValueUri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfoField infoField = (InfoField) o;
    return Objects.equals(getFieldName(), infoField.getFieldName()) &&
        Objects.equals(getFieldPrefLabel(), infoField.getFieldPrefLabel()) &&
        Objects.equals(getFieldPath(), infoField.getFieldPath()) &&
        Objects.equals(getFieldValue(), infoField.getFieldValue()) &&
        Objects.equals(getFieldValueUri(), infoField.getFieldValueUri());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getFieldName(), getFieldPrefLabel(), getFieldPath(), getFieldValue(), getFieldValueUri());
  }

}
