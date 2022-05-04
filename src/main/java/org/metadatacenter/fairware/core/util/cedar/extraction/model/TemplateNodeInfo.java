package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.core.domain.CedarArtifactType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class stores information about JSON nodes in a CEDAR template. A template node may contain:
 *
 * 1) A template element, or
 * 2) A template field, or
 * 3) An array of template elements, or
 * 4) An array of template fields
 *
 */
@AutoValue
public abstract class TemplateNodeInfo {

  public static TemplateNodeInfo create(@JsonProperty("id") @Nonnull String id,
                                        @JsonProperty("name") @Nonnull String name,
                                        @JsonProperty("prefLabel") @Nullable String prefLabel,
                                        @JsonProperty("path") @Nonnull ImmutableList<String> path,
                                        @Nonnull CedarArtifactType type,
                                        @Nonnull boolean isArray,
                                        @Nonnull boolean valueRequired) {
    if (type.equals(CedarArtifactType.ELEMENT) || type.equals(CedarArtifactType.FIELD)) {
      return new AutoValue_TemplateNodeInfo(id, name, prefLabel, path, type, isArray, valueRequired);
    } else {
      throw new IllegalArgumentException("Invalid node type: " + type.name());
    }
  }

  /**
   * Get the artifact id. It corresponds to the '@id' JSON field.
   */
  @JsonProperty("id")
  @Nonnull
  public abstract String getId();

  /**
   * Get the artifact name. It corresponds to the 'schema:name' JSON field.
   */
  @JsonProperty("name")
  @Nonnull
  public abstract String getName();

  /**
   * Get the artifact preferred label. It corresponds to the 'skos:prefLabel' JSON field.
   */
  @JsonProperty("prefLabel")
  @Nullable
  public abstract String getPrefLabel();

  /**
   * Get the of JSON keys from the root, including the key of the current JSON node.
   */
  @JsonProperty("path")
  @Nonnull
  public abstract ImmutableList<String> getPath();

  /**
   * Get the artifact type, that is, 'FIELD' or 'ELEMENT'.
   */
  @JsonIgnore
  @Nonnull
  public abstract CedarArtifactType getType();

  /**
   * Specifies if the JSON node contains just one artifact or an array of artifacts.
   */
  @JsonIgnore
  @Nonnull
  public abstract boolean isArray();

  @JsonIgnore
  @Nonnull
  public abstract boolean isValueRequired();

  @JsonIgnore
  @Nonnull
  public boolean isTemplateFieldNode() {
    return getType().equals(CedarArtifactType.FIELD);
  }

  @JsonIgnore
  @Nonnull
  public boolean isTemplateElementNode() {
    return getType().equals(CedarArtifactType.ELEMENT);
  }

  /**
   * Generates the field path in dot notation. Dots are first removed from the field keys to avoid confusion.
   * Example: level1.level2.level3
   */
  @Nonnull
  public String generatePathDotNotation() {
    return getPath().stream()
        .map(s -> s.replaceAll(".", ""))
        .map(s -> s.trim())
        .collect(Collectors.joining("."));
  }

  /**
   * Generates the full path using dot notation.
   */
  @Nonnull
  public String generateFullPathDotNotation() {
    String fieldName = generatePathDotNotation();
    return fieldName + "." + getName();
  }

  /**
   * Generates the field path in brackets notation. Example: ['level1']['level2']['level3']
   */
  @Nonnull
  public String generatePathBracketNotation() {
    return getPath().stream()
        .map(s -> s.replaceAll(".", ""))
        .map(s -> "[" + s.trim() + "]")
        .collect(Collectors.joining());
  }
}
