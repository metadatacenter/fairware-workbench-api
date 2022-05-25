package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class stores information about CEDAR fields extracted from template instances.
 */
@AutoValue
public abstract class FieldValue {

  public static FieldValue create(@Nonnull String fieldName,
                                  @Nonnull Optional<String> fieldValue,
                                  @Nonnull Optional<String> fieldValueUri,
                                  @Nonnull ImmutableList<String> fieldPath) {
    return new AutoValue_FieldValue(fieldName, fieldValue, fieldValueUri, fieldPath);
  }

  /**
   * Get the field name.
   */
  @Nonnull
  public abstract String getFieldName();

  /**
   * Get the field value. For text-based values, it corresponds to the values of the '@value' property
   * in template instances. For ontology terms, it corresponds to the value of the 'rdfs:label' property.
   */
  @Nonnull
  public abstract Optional<String> getFieldValue();

  /**
   * Get the ontology terms, it stores the value of the '@id' field in template instances.
   */
  @Nonnull
  public abstract Optional<String> getFieldValueUri();

  /**
   * Get the list of field names from the JSON object. It includes the field name of the current node.
   */
  @Nonnull
  public abstract ImmutableList<String> getFieldPath();

  /**
   * Generates the field path in dot notation. Dots are first removed from the field keys to avoid confusion.
   * Example: level1.level2.level3
   */
  public String generateFullPathDotNotation() {
    var fullPath = generatePathDotNotation();
    if (fullPath.isEmpty()) {
      return getFieldName();
    } else {
      return fullPath + "." + getFieldName();
    }
  }

  private String generatePathDotNotation() {
    return getFieldPath().stream()
        .map(s -> s.replaceAll(".", ""))
        .map(s -> s.trim())
        .collect(Collectors.joining("."));
  }

  /**
   * Generates the field path in brackets notation. Example: ['level1']['level2']['level3']
   */
  public String generateFullPathBracketNotation() {
    var fullPath = generatePathBracketNotation();
    if (fullPath.isEmpty()) {
      return getFieldName();
    } else {
      return fullPath + "[" + getFieldName() + "]";
    }
  }

  private String generatePathBracketNotation() {
    return getFieldPath().stream()
        .map(s -> s.replaceAll(".", ""))
        .map(s -> "[" + s.trim() + "]")
        .collect(Collectors.joining());
  }
}
