package org.metadatacenter.fairware.core.util.cedar.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateNodeInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.FieldValue;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utilities to extract information from CEDAR Template Instances
 */
public class CedarTemplateInstanceContentExtractor {

  private static final Logger log = LoggerFactory.getLogger(CedarTemplateInstanceContentExtractor.class);
  private static final ObjectMapper mapper = new ObjectMapper();

  public static List<MetadataFieldInfo> generateInfoFieldsFromInstance(Map<String, Object> templateInstance,
                                                                       Map<String, Object> template) throws UnsupportedEncodingException {
    return generateInfoFieldsFromInstance(mapper.convertValue(templateInstance, JsonNode.class),
        mapper.convertValue(template, JsonNode.class));
  }

  /**
   * Generates a list of InfoField objects from a template instance. These InfoFields objects contain information
   * about the template fields and the values entered for the template instance
   */
  public static List<MetadataFieldInfo> generateInfoFieldsFromInstance(JsonNode templateInstance, JsonNode template) throws UnsupportedEncodingException {

    List<MetadataFieldInfo> infoFields = new ArrayList<>();
    List<TemplateNodeInfo> templateNodes = CedarTemplateContentExtractor.getTemplateNodes(template);

    HashMap<String, TemplateNodeInfo> nodesMap = new HashMap<>();
    for (TemplateNodeInfo node : templateNodes) {
      nodesMap.put(node.generateFullPathDotNotation(), node);
    }

    List<FieldValue> fieldValues = getFieldValues(templateInstance, nodesMap, new ArrayList<>(), new ArrayList());

    for (FieldValue fieldValue : fieldValues) {
      String fieldName = null;
      String fieldPrefLabel = null;
      String fieldFullPath = fieldValue.generateFullPathDotNotation();
      if (nodesMap.containsKey(fieldFullPath)) {
        TemplateNodeInfo templateNode = nodesMap.get(fieldFullPath);
        fieldName = templateNode.getName();
        fieldPrefLabel = templateNode.getPrefLabel();
      } else {
        throw new IllegalArgumentException("Field path not found in nodesMap: " +
            fieldValue.generatePathDotNotation());
      }
      // Add to the list if it's not already there
      MetadataFieldInfo infoField = null;
      try {
        String fieldValueUri = fieldValue.getFieldValueUri();
        if (fieldValueUri != null) {
          fieldValueUri = URLEncoder.encode(fieldValueUri, StandardCharsets.UTF_8.toString());
        }
        infoField = new MetadataFieldInfo(fieldName, fieldPrefLabel, fieldValue.getFieldPath(), fieldValue.getFieldValue(), fieldValueUri);
      } catch (UnsupportedEncodingException e) {
        throw e;
      }
      if (!infoFields.contains(infoField)) {
        infoFields.add(infoField);
      }
    }
    return infoFields;

  }

  /**
   * Extracts field and field values from a template instance. Note that some information, such as the field name,
   * cannot be extracted from the instance because it's not available there. The field names will be extracted from
   * the template in the method 'generateInfoFields'.
   *
   * @param currentNode
   * @param templateNodesMap
   * @param currentPath      Used internally
   * @param results          Used internally
   * @return
   */
  public static List<FieldValue> getFieldValues(JsonNode currentNode, HashMap<String, TemplateNodeInfo> templateNodesMap,
                                                List<String> currentPath, List<FieldValue> results) {

    Iterator<Map.Entry<String, JsonNode>> jsonNodesIterator = currentNode.fields();
    while (jsonNodesIterator.hasNext()) {
      Map.Entry<String, JsonNode> currentNodeMap = jsonNodesIterator.next();

//      List<String> tmpPath = new ArrayList<>();
//      tmpPath.addAll(currentPath);
//      tmpPath.add(currentNodeMap.getKey());
//      String tmpPathDotNotation = getPathDotNotation(tmpPath);

      TemplateNodeInfo templateNode = null;

      String fullPathDotNotation = GeneralUtil.generateFullPathDotNotation(currentPath, currentNodeMap.getKey());

      if (templateNodesMap.containsKey(fullPathDotNotation)) {

        currentPath.add(currentNodeMap.getKey());

        templateNode = templateNodesMap.get(fullPathDotNotation);
        // Not an array
        if (!templateNode.isArray()) {
          // Template Element
          if (templateNode.isTemplateElementNode()) {
            getFieldValues(currentNodeMap.getValue(), templateNodesMap, currentPath, results);
          }
          // Template Field
          else if (templateNode.isTemplateFieldNode()) {
            // Extract value and save it to the results
            results.add(generateFieldValue(currentNodeMap.getValue(), currentPath));
          } else {
            throw new IllegalArgumentException("Unrecognized node type. The template node must be either a " +
                "Template Field or a Template Element. Node type: " + templateNode.getName());
          }
        }
        // Array
        else {
          // Array of template elements
          if (templateNode.isTemplateElementNode()) {
            for (JsonNode node : currentNodeMap.getValue()) {
              getFieldValues(node, templateNodesMap, currentPath, results);
            }
          }
          // Array of template fields
          else if (templateNode.isTemplateFieldNode()) {
            for (JsonNode node : currentNodeMap.getValue()) {
              // Extract value and save it to the results
              results.add(generateFieldValue(node, currentPath));
            }
          } else {
            throw new IllegalArgumentException("Unrecognized node type. The template node must be either a " +
                "Template Field or a Template Element. Node type: " + templateNode.getName());
          }
        }

        currentPath.remove(currentPath.size()-1);
        
      } else {
        // Node not found in the map of template nodes. It is not a relevant node (e.g. @context) so we ignore it.
      }
    }
    return results;
  }

  public static String getPathDotNotation(List<String> path) {
    return String.join(".", path);
  }

  private static FieldValue generateFieldValue(JsonNode fieldNode, List<String> fieldPath) {
    FieldValue fieldValue = new FieldValue();
    fieldValue.setFieldKey(fieldPath.get(fieldPath.size() - 1));
    fieldValue.setFieldPath(fieldPath);

    // Regular value
    if (fieldNode.hasNonNull(CedarModelConstants.JSON_LD_VALUE) && !fieldNode.get(CedarModelConstants.JSON_LD_VALUE).asText().isEmpty()) {
      fieldValue.setFieldValue(fieldNode.get(CedarModelConstants.JSON_LD_VALUE).asText());
    }
    // Ontology term
    else {
      if (fieldNode.hasNonNull(CedarModelConstants.RDFS_LABEL) && !fieldNode.get(CedarModelConstants.RDFS_LABEL).asText().isEmpty()) {
        fieldValue.setFieldValue(fieldNode.get(CedarModelConstants.RDFS_LABEL).asText());
      }
      if (fieldNode.hasNonNull(CedarModelConstants.JSON_LD_ID) && !fieldNode.get(CedarModelConstants.JSON_LD_ID).asText().isEmpty()) {
        fieldValue.setFieldValueUri(fieldNode.get(CedarModelConstants.JSON_LD_ID).asText());
      }
    }
    return fieldValue;
  }

}
