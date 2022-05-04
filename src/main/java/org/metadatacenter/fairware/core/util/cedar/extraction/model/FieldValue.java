package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * This class stores information about CEDAR fields extracted from template instances.
 */
public class FieldValue {

  /**
   * Field JSON key.
   */
  private String fieldKey;

  /**
   * Field value. For textual values, it corresponds to the values of the '@value' property in template instances.
   * For ontology terms, it corresponds to the value of the 'rdfs:label' property.
   */
  private String fieldValue;

  /**
   * For ontology terms, it stores the value of the '@id' field in template instances.
   */
  private String fieldValueUri;

  /**
   * List of json keys from the root of the JSON document. It includes the key of the current node.
   */
  private ImmutableList<String> fieldPath;

  public FieldValue() {
  }

  public FieldValue(String fieldKey, String fieldValue, String fieldValueUri, ImmutableList<String> fieldPath) {
    this.fieldKey = fieldKey;
    this.fieldValue = fieldValue;
    this.fieldValueUri = fieldValueUri;
    this.fieldPath = fieldPath;
  }

  public String getFieldKey() {
    return fieldKey;
  }

  public void setFieldKey(String fieldKey) {
    this.fieldKey = fieldKey;
  }

  public String getFieldValue() {
    return fieldValue;
  }

  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }

  public String getFieldValueUri() {
    return fieldValueUri;
  }

  public void setFieldValueUri(String fieldValueUri) {
    this.fieldValueUri = fieldValueUri;
  }

  public ImmutableList<String> getFieldPath() {
    return fieldPath;
  }

  public void setFieldPath(ImmutableList<String> fieldPath) {
    this.fieldPath = fieldPath;
  }

  /**
   * Generates the field path in dot notation. Example: level1.level2.level3
   * @return
   */
  public String generatePathDotNotation() {
    return String.join(".", fieldPath);
  }

  /**
   * Generates the full path using dot notation. Dots are first removed from the field keys to avoid confusion
   * @return The full node path in dot notation (includes the node name)
   */
  public String generateFullPathDotNotation() {
    StringBuilder pathSb = new StringBuilder();
    for (int i=0; i<this.fieldPath.size(); i++) {
      String current = this.fieldPath.get(i);
      current.replaceAll(".", "");
      pathSb.append(current.trim());
      pathSb.append(".");
    }
    return pathSb.append(this.fieldKey).toString();
  }

  /**
   * Generates the field path in brackets notation. Example: ['level1']['level2']['level3']
   * @return
   */
  public String generatePathBracketNotation() {
    return "['" + String.join("']['", fieldPath) + "']";
  }

}
