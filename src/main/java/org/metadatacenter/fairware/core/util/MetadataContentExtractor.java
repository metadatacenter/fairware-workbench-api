package org.metadatacenter.fairware.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utilities to extract information from CEDAR Template Instances
 */
public class MetadataContentExtractor {

  public static List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> metadataRecord) {
    return extractMetadataFieldsInfo(metadataRecord, new ArrayList<>(), new ArrayList<>());
  }

  private static List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> current,
                                                            List<String> currentPath, List<MetadataFieldInfo> result) {

    for (Map.Entry<String, Object> entry : current.entrySet()) {

      if (entry.getValue() instanceof String || entry.getValue() instanceof Number) { // String or Numeric
        result.add(new MetadataFieldInfo(entry.getKey(), getPathDotNotation(currentPath)));
      }
      else if (entry.getValue() instanceof Map<?, ?>) { // Another object
        currentPath.add(entry.getKey());
        extractMetadataFieldsInfo((Map<String, Object>) entry.getValue(), currentPath, result);
      }
      else if (entry.getValue() instanceof List<?>) { // Array
        Object firstValue = ((List<?>)entry.getValue()).get(0);
        if (firstValue instanceof String || firstValue instanceof Number) { // non-primitive types, e.g., String[]
          result.add(new MetadataFieldInfo(entry.getKey(), getPathDotNotation(currentPath)));
        }
        else if (firstValue instanceof Map<?, ?>) { // Another object
          currentPath.add(entry.getKey());
          extractMetadataFieldsInfo((Map<String, Object>) firstValue, currentPath, result);
        }
        else {
          // any other relevant cases?
        }
      }

    }
    return result;

  }

  private static String getPathDotNotation(List<String> path) {
    if (path.size() == 0) return "";
    if (path.size() == 1) return path.get(0);
    return String.join(".", path);
  }

  public static boolean isPrimitiveArray(Object obj) {
    return obj != null
        && obj.getClass().isArray()
        && obj.getClass().getComponentType() != null
        && obj.getClass().getComponentType().isPrimitive();
  }



  /**
   * Generates a list of InfoField objects from a template instance. These InfoFields objects contain information
   * about the template fields and the values entered for the template instance
   */
//  private List<MetadataFieldInfo> extractFields1() {
//
//    if (folderServerNode.getType().equals(CedarResourceType.INSTANCE)) {
//
//      List<MetadataFieldInfo> infoFields = new ArrayList<>();
//      JsonNode templateInstance = extractionUtils.getArtifactById(folderServerNode.getId(),
//          folderServerNode.getType(), requestContext);
//      String templateId = templateInstance.get(SCHEMA_IS_BASED_ON).asText();
//
//      HashMap<String, TemplateNode> nodesMap = null;
//      // If it's an index regeneration task the cache will be needed to avoid retrieving and parsing the same
//      // template multiple times (once per template instance). If the cache contains the template nodes, return them
//      if (isIndexRegenerationTask && templateNodesCache.containsKey(templateId)) {
//        nodesMap = templateNodesCache.get(templateId);
//      }
//      // Otherwise, retrieve the template and parse it
//      else {
//        JsonNode template = extractionUtils.getArtifactById(templateId, CedarResourceType.TEMPLATE, requestContext);
//        List<TemplateNode> templateNodes = templateContentExtractor.getTemplateNodes(template, CedarResourceType.TEMPLATE);
//        nodesMap = new HashMap<>();
//        for (TemplateNode node : templateNodes) {
//          nodesMap.put(node.generatePathDotNotation(), node);
//        }
//        if (isIndexRegenerationTask) {
//          templateNodesCache.put(templateId, nodesMap);
//        }
//      }
//
//      List<FieldValue> fieldValues = getFieldValues(templateInstance, nodesMap, null, null);
//
//      for (FieldValue fieldValue : fieldValues) {
//        String fieldName = null;
//        String fieldPrefLabel = null;
//        if (nodesMap.containsKey(fieldValue.generatePathDotNotation())) {
//          TemplateNode templateNode = nodesMap.get(fieldValue.generatePathDotNotation());
//          fieldName = templateNode.getName();
//          fieldPrefLabel = templateNode.getPrefLabel();
//        } else {
//          throw new CedarProcessingException("Field path not found in nodesMap: " +
//              fieldValue.generatePathDotNotation());
//        }
//        // Add to the list if it's not already there
//        MetadataFieldInfo infoField = null;
//        try {
//          String fieldValueUri = fieldValue.getFieldValueUri();
//          if (fieldValueUri != null) {
//            fieldValueUri = URLEncoder.encode(fieldValueUri, StandardCharsets.UTF_8.toString());
//          }
//          infoField = new MetadataFieldInfo(fieldName, fieldPrefLabel, fieldValue.generatePathBracketNotation(),
//              fieldValue.getFieldValue(), fieldValueUri);
//        } catch (UnsupportedEncodingException e) {
//          throw new CedarProcessingException("Encoding error", e);
//        }
//        if (!infoFields.contains(infoField)) {
//          infoFields.add(infoField);
//        }
//      }
//      return infoFields;
//    } else {
//      throw new CedarProcessingException("The artifact must be an Instance but it is a "
//          + folderServerNode.getType().name());
//    }
//  }

  /**
   * Generates a list of InfoField objects from a template or an element.
   *
   * @param folderServerNode
   * @param requestContext
   * @return
   */
//  private List<MetadataFieldInfo> generateInfoFieldsFromSchema(FileSystemResource folderServerNode,
//                                                               CedarRequestContext requestContext) throws CedarProcessingException {
//
//    if (folderServerNode.getType().equals(CedarResourceType.TEMPLATE) ||
//        folderServerNode.getType().equals(CedarResourceType.ELEMENT) ||
//        folderServerNode.getType().equals(CedarResourceType.FIELD)) {
//
//      List<MetadataFieldInfo> infoFields = new ArrayList<>();
//      // Retrieve the template/element/field and parse it to extract its nodes
//      JsonNode schema = extractionUtils.getArtifactById(folderServerNode.getId(), folderServerNode.getType(), requestContext);
//      List<TemplateNode> schemaNodes = templateContentExtractor.getTemplateNodes(schema, folderServerNode.getType());
//
//      for (TemplateNode node : schemaNodes) {
//        if (node.getType().equals(CedarResourceType.FIELD)) {
//          infoFields.add(new MetadataFieldInfo(node.getName(), node.getPrefLabel(), node.generatePathBracketNotation(), null, null));
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
   * @param currentPath Used internally
   * @param results Used internally
   * @return
   * @throws CedarProcessingException
   */
//  private List<FieldValue> getFieldValues(JsonNode currentNode, HashMap<String, TemplateNode> templateNodesMap,
//                                          List<String> currentPath, List<FieldValue> results) throws CedarProcessingException {
//
//    if (currentPath == null) {
//      currentPath = new ArrayList<>();
//    }
//    if (results == null) {
//      results = new ArrayList();
//    }
//
//    Iterator<Map.Entry<String, JsonNode>> jsonNodesIterator = currentNode.fields();
//    while (jsonNodesIterator.hasNext()) {
//      Map.Entry<String, JsonNode> currentNodeMap = jsonNodesIterator.next();
//      List<String> tmpPath = new ArrayList<>();
//      tmpPath.addAll(currentPath);
//      tmpPath.add(currentNodeMap.getKey());
//      String tmpPathDotNotation = getPathDotNotation(tmpPath);
//      TemplateNode templateNode = null;
//      if (templateNodesMap.containsKey(tmpPathDotNotation)) {
//        templateNode = templateNodesMap.get(tmpPathDotNotation);
//        // Not an array
//        if (!templateNode.isArray()) {
//          // Template Element
//          if (templateNode.isTemplateElementNode()) {
//            getFieldValues(currentNodeMap.getValue(), templateNodesMap, tmpPath, results);
//          }
//          // Template Field
//          else if (templateNode.isTemplateFieldNode()) {
//            // Extract value and save it to the results
//            results.add(generateFieldValue(currentNodeMap.getValue(), tmpPath));
//          } else {
//            throw new CedarProcessingException("Unrecognized node type. The template node must be either a " +
//                "Template Field or a Template Element. Node type: " + templateNode.getName());
//          }
//        }
//        // Array
//        else {
//          // Array of template elements
//          if (templateNode.isTemplateElementNode()) {
//            for (JsonNode node : currentNodeMap.getValue()) {
//              getFieldValues(node, templateNodesMap, tmpPath, results);
//            }
//          }
//          // Array of template fields
//          else if (templateNode.isTemplateFieldNode()) {
//            for (JsonNode node : currentNodeMap.getValue()) {
//              // Extract value and save it to the results
//              results.add(generateFieldValue(node, tmpPath));
//            }
//          } else {
//            throw new CedarProcessingException("Unrecognized node type. The template node must be either a " +
//                "Template Field or a Template Element. Node type: " + templateNode.getName());
//          }
//        }
//      } else {
//        // Node not found in the map of template nodes. It is not a relevant node (e.g. @context) so we ignore it.
//      }
//    }
//    return results;
//  }

//  private FieldValue generateFieldValue(JsonNode fieldNode, List<String> fieldPath) {
//    FieldValue fieldValue = new FieldValue();
//    fieldValue.setFieldKey(fieldPath.get(fieldPath.size() - 1));
//    fieldValue.setFieldPath(fieldPath);
//
//    // Regular value
//    if (fieldNode.hasNonNull(JSON_LD_VALUE) && !fieldNode.get(JSON_LD_VALUE).asText().isEmpty()) {
//      fieldValue.setFieldValue(fieldNode.get(JSON_LD_VALUE).asText());
//    }
//    // Ontology term
//    else {
//      if (fieldNode.hasNonNull(RDFS_LABEL) && !fieldNode.get(RDFS_LABEL).asText().isEmpty()) {
//        fieldValue.setFieldValue(fieldNode.get(RDFS_LABEL).asText());
//      }
//      if (fieldNode.hasNonNull(JSON_LD_ID) && !fieldNode.get(JSON_LD_ID).asText().isEmpty()) {
//        fieldValue.setFieldValueUri(fieldNode.get(JSON_LD_ID).asText());
//      }
//    }
//    return fieldValue;
//  }

}

