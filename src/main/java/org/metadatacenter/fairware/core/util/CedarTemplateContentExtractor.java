package org.metadatacenter.fairware.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.fairware.core.domain.CedarArtifactType;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.metadatacenter.fairware.constants.CedarModelConstants.*;

/**
 * Utilities to extract information from CEDAR Templates/Elements
 */
public class CedarTemplateContentExtractor {

  private static final Logger log = LoggerFactory.getLogger(CedarTemplateContentExtractor.class);

  public static List<TemplateNodeInfo> getTemplateNodes(JsonNode node, CedarArtifactType resourceType) {
    // If it's a field, we nest it in a JsonNode to make the getSchemaNodes method work
    if (resourceType.equals(CedarArtifactType.FIELD)) {
      node = new ObjectMapper().createObjectNode().set("field", node);
    }
    return getTemplateNodes(node, null, null);
  }

  // If the resource type is not specified, it assumes that it will be a Template/Element. There is no need to nest
  // the JsonNode inside another node
  public static List<TemplateNodeInfo> getTemplateNodes(JsonNode schema) {
    return getTemplateNodes(schema, null, null);
  }

  public static List<TemplateNodeInfo> getTemplateNodes(Map<String, Object> schema) {
    return getTemplateNodes((JsonNode) new ObjectMapper().valueToTree(schema));
  }

  /**
   * Returns summary information of all template nodes in the template.
   *
   * @param node Template/Element/Field in JSON
   * @param currentPath Used internally to store the current node path
   * @param results     Used internally to store the results
   * @return A list of the template elements and fields in the template, represented using the TemplateNode class
   */
  private static List<TemplateNodeInfo> getTemplateNodes(JsonNode node, List<String> currentPath, List<TemplateNodeInfo> results) {
    if (currentPath == null) {
      currentPath = new ArrayList<>();
    }
    if (results == null) {
      results = new ArrayList<>();
    }
    Iterator<Map.Entry<String, JsonNode>> jsonFieldsIterator = node.fields();
    while (jsonFieldsIterator.hasNext()) {
      Map.Entry<String, JsonNode> jsonField = jsonFieldsIterator.next();
      final String jsonFieldKey = jsonField.getKey();
      if (jsonField.getValue().isContainerNode()) {
        JsonNode jsonFieldNode;
        boolean isArray;
        // Single-instance node
        if (!jsonField.getValue().has(JSON_SCHEMA_ITEMS)) {
          jsonFieldNode = jsonField.getValue();
          isArray = false;
        }
        // Multi-instance node
        else {
          jsonFieldNode = jsonField.getValue().get(JSON_SCHEMA_ITEMS);
          isArray = true;
        }
        // Field or Element
        if (isTemplateFieldNode(jsonFieldNode) || isTemplateElementNode(jsonFieldNode)) {

          // Get field/element identifier
          String id;
          if ((jsonFieldNode.get(JSON_LD_ID) != null) && (jsonFieldNode.get(JSON_LD_ID).asText().length() > 0)) {
            id = jsonFieldNode.get(JSON_LD_ID).asText();
          } else {
            throw (new IllegalArgumentException(JSON_LD_ID + " not found for template field"));
          }

          // Get name
          String name = null;
          if ((jsonFieldNode.get(SCHEMA_ORG_NAME) != null) && (jsonFieldNode.get(SCHEMA_ORG_NAME).asText().length() > 0)) {
            name = jsonFieldNode.get(SCHEMA_ORG_NAME).asText();
          }  // Else, do nothing. This field is not required.


          // Get preferred label
          String prefLabel = null;
          if ((jsonFieldNode.get(SKOS_PREFLABEL) != null) && (jsonFieldNode.get(SKOS_PREFLABEL).asText().length() > 0)) {
            prefLabel = jsonFieldNode.get(SKOS_PREFLABEL).asText();
          }  // Do nothing. This field is not required.


          // Add json field path to the results. I create a new list to not modify currentPath
          List<String> jsonFieldPath = new ArrayList<>(currentPath);
          jsonFieldPath.add(jsonFieldKey);

          // Field
          if (isTemplateFieldNode(jsonFieldNode)) {
            results.add(new TemplateNodeInfo(id, name, prefLabel, jsonFieldPath.subList(0, jsonFieldPath.size()-1), CedarArtifactType.FIELD, isArray));
          }
          // Element
          else if (isTemplateElementNode(jsonFieldNode)) {
            results.add(new TemplateNodeInfo(id, name, prefLabel, jsonFieldPath, CedarArtifactType.ELEMENT, isArray));
            getTemplateNodes(jsonFieldNode, jsonFieldPath, results);
          }
        }
        // All other nodes
        else {
          getTemplateNodes(jsonFieldNode, currentPath, results);
        }
      }
    }
    return results;
  }

  /**
   * Checks if a Json node corresponds to a CEDAR template field
   *
   * @param node a Json node
   * @return true if the node corresponds to a template field, or false otherwise
   */
  private static boolean isTemplateFieldNode(JsonNode node) {
    return node.get(JSON_LD_TYPE) != null && node.get(JSON_LD_TYPE).asText().equals(CedarArtifactType.FIELD.getAtType());
  }

  /**
   * Checks if a Json node corresponds to a CEDAR template element
   *
   * @param node a Json node
   * @return true if the node corresponds to a template element, or false otherwise
   */
  private static boolean isTemplateElementNode(JsonNode node) {
    return node.get(JSON_LD_TYPE) != null && node.get(JSON_LD_TYPE).asText().equals(CedarArtifactType.ELEMENT.getAtType());
  }

}

