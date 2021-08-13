package org.metadatacenter.fairware.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * This class stores information about JSON nodes in a CEDAR template. A template node can contain:
 * 1) A template element, or
 * 2) A template field, or
 * 3) An array of template elements, or
 * 4) An array of template fields
 *
 */
public class TemplateNodeInfo {

  /**
   * Artifact id. It corresponds to the '@id' JSON field.
   */
  private String id;

  /**
   * Artifact name. It corresponds to the 'schema:name' JSON field.
   */
  private String name;

  /**
   * Artifact preferred label. It corresponds to the 'skos:prefLabel' JSON field.
   */
  private String prefLabel;

  /**
   * List of JSON keys from the root, including the key of the current JSON node.
   */
  private List<String> path;

  /**
   * Artifact type, that is, 'FIELD' or 'ELEMENT'.
   */
  @JsonIgnore
  private CedarArtifactType type; // Node type (e.g. field)

  /**
   * Specifies if the JSON node contains just one artifact or an array of artifacts.
   */
  @JsonIgnore
  private boolean isArray;

  public TemplateNodeInfo(String id, String name, String prefLabel, List<String> path,
                      CedarArtifactType type, boolean isArray) {

    if (type.equals(CedarArtifactType.ELEMENT) || type.equals(CedarArtifactType.FIELD)) {
      this.id = id;
      this.name = name;
      this.prefLabel = prefLabel;
      this.path = path;
      this.type = type;
      this.isArray = isArray;
    } else {
      throw new IllegalArgumentException("Invalid node type: " + type.name());
    }
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPrefLabel() {
    return prefLabel;
  }

  public List<String> getPath() {
    return path;
  }

  public CedarArtifactType getType() {
    return type;
  }

  @JsonIgnore
  public boolean isArray() {
    return isArray;
  }

  public String generatePathDotNotation() {
    return String.join(".", path);
  }

  public String generatePathBracketNotation() {
    return "['" + String.join("']['", path) + "']";
  }

  @JsonIgnore
  public boolean isTemplateFieldNode() {
    if (type.equals(CedarArtifactType.FIELD)) {
      return true;
    } else {
      return false;
    }
  }

  @JsonIgnore
  public boolean isTemplateElementNode() {
    if (type.equals(CedarArtifactType.ELEMENT)) {
      return true;
    } else {
      return false;
    }
  }
}
