package org.metadatacenter.fairware.core.util.cedar.extraction.model;
import org.metadatacenter.fairware.core.util.cedar.CedarResourceType;

import java.util.List;

/**
 * This class stores information about JSON nodes in a CEDAR template. A template node can contain:
 * 1) A template element, or
 * 2) A template field, or
 * 3) An array of template elements, or
 * 4) An array of template fields
 *
 */
public class TemplateNode {

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
   * List of URIs of value sets from the _valueConstraints.valueSets object in template fields
   */
  private List<String> valueSetURIs;

  /**
   * Artifact type, that is, 'FIELD' or 'ELEMENT'.
   */
  private CedarResourceType type; // Node type (e.g. field)
  
  /**
   * Specifies if the JSON node contains just one artifact or an array of artifacts.
   */
  private boolean isArray;

  public TemplateNode(String id, String name, String prefLabel, List<String> path,
                      CedarResourceType type, boolean isArray, List<String> valueSetURIs) {

    if (type.equals(CedarResourceType.ELEMENT) || type.equals(CedarResourceType.FIELD)) {
      this.id = id;
      this.name = name;
      this.prefLabel = prefLabel;
      this.path = path;
      this.type = type;
      this.isArray = isArray;
      this.valueSetURIs = valueSetURIs;
    }
    else {
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

  public List<String> getValueSetURIs() {
    return valueSetURIs;
  }

  public CedarResourceType getType() {
    return type;
  }

  public boolean isArray() {
    return isArray;
  }

  public String generatePathDotNotation() {
    return String.join(".", path);
  }

  public String generatePathBracketNotation() {
    return "['" + String.join("']['", path) + "']";
  }

  public boolean isTemplateFieldNode() {
    if (type.equals(CedarResourceType.FIELD)) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isTemplateElementNode() {
    if (type.equals(CedarResourceType.ELEMENT)) {
      return true;
    } else {
      return false;
    }
  }

}
