package org.metadatacenter.fairware.core.util.cedar.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.util.cedar.CedarResourceType;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.FieldValue;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.InfoField;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateNode;
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

  public static List<InfoField> generateInfoFieldsFromInstance(Map<String, Object> templateInstance, Map<String,
      Object> template) throws UnsupportedEncodingException {
    return generateInfoFieldsFromInstance(mapper.convertValue(templateInstance, JsonNode.class),
        mapper.convertValue(template, JsonNode.class));
  }

  /**
   * Generates a list of InfoField objects from a template instance. These InfoFields objects contain information
   * about the template fields and the values entered for the template instance
   */
  public static List<InfoField> generateInfoFieldsFromInstance(JsonNode templateInstance, JsonNode template) throws UnsupportedEncodingException {

    List<InfoField> infoFields = new ArrayList<>();
    List<TemplateNode> templateNodes = CedarTemplateContentExtractor.getTemplateNodes(template,
        CedarResourceType.TEMPLATE);

    HashMap<String, TemplateNode> nodesMap = nodesMap = new HashMap<>();
    for (TemplateNode node : templateNodes) {
      nodesMap.put(node.generatePathDotNotation(), node);
    }

    List<FieldValue> fieldValues = getFieldValues(templateInstance, nodesMap, null, null);

    for (FieldValue fieldValue : fieldValues) {
      String fieldName = null;
      String fieldPrefLabel = null;
      if (nodesMap.containsKey(fieldValue.generatePathDotNotation())) {
        TemplateNode templateNode = nodesMap.get(fieldValue.generatePathDotNotation());
        fieldName = templateNode.getName();
        fieldPrefLabel = templateNode.getPrefLabel();
      } else {
        throw new IllegalArgumentException("Field path not found in nodesMap: " +
            fieldValue.generatePathDotNotation());
      }
      // Add to the list if it's not already there
      InfoField infoField = null;
      try {
        String fieldValueUri = fieldValue.getFieldValueUri();
        if (fieldValueUri != null) {
          fieldValueUri = URLEncoder.encode(fieldValueUri, StandardCharsets.UTF_8.toString());
        }
        infoField = new InfoField(fieldName, fieldPrefLabel, fieldValue.generatePathBracketNotation(),
            fieldValue.getFieldValue(), fieldValueUri);
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
   * Generates a list of InfoField objects from a template or an element.
   *
   * @param folderServerNode
   * @param requestContext
   * @return
   */
//  private List<InfoField> generateInfoFieldsFromSchema(FileSystemResource folderServerNode,
//                                                       CedarRequestContext requestContext) throws
//                                                       CedarProcessingException {
//
//    if (folderServerNode.getType().equals(CedarResourceType.TEMPLATE) ||
//        folderServerNode.getType().equals(CedarResourceType.ELEMENT) ||
//        folderServerNode.getType().equals(CedarResourceType.FIELD)) {
//
//      List<InfoField> infoFields = new ArrayList<>();
//      // Retrieve the template/element/field and parse it to extract its nodes
//      JsonNode schema = extractionUtils.getArtifactById(folderServerNode.getId(), folderServerNode.getType(),
//      requestContext);
//      List<TemplateNode> schemaNodes = templateContentExtractor.getTemplateNodes(schema, folderServerNode.getType());
//
//      for (TemplateNode node : schemaNodes) {
//        if (node.getType().equals(CedarResourceType.FIELD)) {
//          List<String> fieldValues = node.getValueSetURIs();
//          infoFields.add(new InfoField(node.getName(), node.getPrefLabel(), node.generatePathBracketNotation(),
//          null, null));
//        }
//      }
//      return infoFields;
//
//    } else {
//      throw new CedarProcessingException("The artifact must be a Template, an Element, or a Field, but it is a "
//          + folderServerNode.getType().name());
//    }
//  }

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
  public static List<FieldValue> getFieldValues(JsonNode currentNode, HashMap<String, TemplateNode> templateNodesMap,
                                                List<String> currentPath, List<FieldValue> results) {

    if (currentPath == null) {
      currentPath = new ArrayList<>();
    }
    if (results == null) {
      results = new ArrayList();
    }

    Iterator<Map.Entry<String, JsonNode>> jsonNodesIterator = currentNode.fields();
    while (jsonNodesIterator.hasNext()) {
      Map.Entry<String, JsonNode> currentNodeMap = jsonNodesIterator.next();
      List<String> tmpPath = new ArrayList<>();
      tmpPath.addAll(currentPath);
      tmpPath.add(currentNodeMap.getKey());
      String tmpPathDotNotation = getPathDotNotation(tmpPath);
      TemplateNode templateNode = null;
      if (templateNodesMap.containsKey(tmpPathDotNotation)) {
        templateNode = templateNodesMap.get(tmpPathDotNotation);
        // Not an array
        if (!templateNode.isArray()) {
          // Template Element
          if (templateNode.isTemplateElementNode()) {
            getFieldValues(currentNodeMap.getValue(), templateNodesMap, tmpPath, results);
          }
          // Template Field
          else if (templateNode.isTemplateFieldNode()) {
            // Extract value and save it to the results
            results.add(generateFieldValue(currentNodeMap.getValue(), tmpPath));
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
              getFieldValues(node, templateNodesMap, tmpPath, results);
            }
          }
          // Array of template fields
          else if (templateNode.isTemplateFieldNode()) {
            for (JsonNode node : currentNodeMap.getValue()) {
              // Extract value and save it to the results
              results.add(generateFieldValue(node, tmpPath));
            }
          } else {
            throw new IllegalArgumentException("Unrecognized node type. The template node must be either a " +
                "Template Field or a Template Element. Node type: " + templateNode.getName());
          }
        }
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
