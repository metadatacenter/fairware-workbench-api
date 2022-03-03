package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.constants.CedarConstants;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateInstanceContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.InfoField;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utilities to extract information from CEDAR Template Instances
 */
public class MetadataContentExtractor {

  public static List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> metadataRecord) throws UnsupportedEncodingException {
    return extractMetadataFieldsInfo(metadataRecord, null);
  }

  public static List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> metadataRecord,
                                                                  Map<String, Object> template) throws UnsupportedEncodingException {
    if (isCedarTemplateInstance(metadataRecord)) {
      List<InfoField> infoFields = CedarTemplateInstanceContentExtractor.generateInfoFieldsFromInstance(metadataRecord, template);
      return extractCedarMetadataFieldsInfo(metadataRecord, new ArrayList<>(), new ArrayList<>());
    }
    else {
      return extractMetadataFieldsInfo(metadataRecord, new ArrayList<>(), new ArrayList<>());
    }
  }

  /**
   * Extracts fields and values from a generic JSON object
   *
   * @param current
   * @param currentPath
   * @param result
   * @return
   */
  private static List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> current,
                                                                   List<String> currentPath,
                                                                   List<MetadataFieldInfo> result) {

    for (Map.Entry<String, Object> entry : current.entrySet()) {

      if (entry.getValue() == null) {
        result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath), null));
      } else if (entry.getValue() instanceof String || entry.getValue() instanceof Number) { // String or Numeric
        result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath), entry.getValue().toString()));
      } else if (entry.getValue() instanceof Map<?, ?>) { // Another object
        currentPath.add(entry.getKey());
        extractMetadataFieldsInfo((Map<String, Object>) entry.getValue(), currentPath, result);
        currentPath.remove(entry.getKey());
      } else if (entry.getValue() instanceof List<?>) { // Array
        Object firstValue = ((List<?>) entry.getValue()).get(0);
        if (firstValue instanceof String || firstValue instanceof Number) { // non-primitive types, e.g., String[]
          StringBuilder valuesSb = new StringBuilder();
          for (Object o : (List<Object>) entry.getValue()) {
            valuesSb.append(o.toString());
          }
          result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath), valuesSb.toString()));
        } else if (firstValue instanceof Map<?, ?>) { // Another object
          currentPath.add(entry.getKey());
          extractMetadataFieldsInfo((Map<String, Object>) firstValue, currentPath, result);
          currentPath.remove(entry.getKey());
        }
        // else, do nothing. Any other relevant cases?
      }
    }
    return result;
  }

  /**
   * Extract fields and values from a CEDAR metadata instance
   *
   * @param current
   * @param currentPath
   * @param result
   * @return
   */
  private static List<MetadataFieldInfo> extractCedarMetadataFieldsInfo(Map<String, Object> current,
                                                                        List<String> currentPath,
                                                                        List<MetadataFieldInfo> result) {
    if (current == null) return result;
    for (Map.Entry<String, Object> entry : current.entrySet()) {
      if (isMetadataField(entry.getKey())) {
        if (entry.getValue() == null) {
          result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath), null));
        } else if (entry.getValue() instanceof String || entry.getValue() instanceof Number) { // String or Numeric
          result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath), entry.getValue().toString()));
        } else if (entry.getValue() instanceof Map<?, ?>) { // Another object
          currentPath.add(entry.getKey());
          extractMetadataFieldsInfo((Map<String, Object>) entry.getValue(), currentPath, result);
          currentPath.remove(entry.getKey());
        } else if (entry.getValue() instanceof List<?>) { // Array
          Object firstValue = ((List<?>) entry.getValue()).get(0);
          if (firstValue instanceof String || firstValue instanceof Number) { // non-primitive types, e.g., String[]
            StringBuilder valuesSb = new StringBuilder();
            for (Object o : (List<Object>) entry.getValue()) {
              valuesSb.append(o.toString());
            }
            result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath), valuesSb.toString()));
          } else if (firstValue instanceof Map<?, ?>) { // Another object
            currentPath.add(entry.getKey());
            extractMetadataFieldsInfo((Map<String, Object>) firstValue, currentPath, result);
            currentPath.remove(entry.getKey());
          }
          // else, do nothing. Any other relevant cases?
        }
      }
    }
    return result;
  }

  /**
   * Checks if the field key corresponds to a metadata field. Fields that are part of the CEDAR template model and that
   * are present in all template instances (e.g., @id, schema:name) are not considered metadata fields.
   * @param key
   * @return
   */
  private static boolean isMetadataField(String key) {
    if (key.equals(CedarModelConstants.JSON_LD_CONTEXT) ||
        key.equals(CedarModelConstants.IS_BASED_ON) ||
        key.equals(CedarModelConstants.SCHEMA_ORG_NAME) ||
        key.equals(CedarModelConstants.SCHEMA_ORG_DESCRIPTION) ||
        key.equals(CedarModelConstants.CREATED_ON) ||
        key.equals(CedarModelConstants.CREATED_BY) ||
        key.equals(CedarModelConstants.LAST_UPDATED_ON) ||
        key.equals(CedarModelConstants.MODIFIED_BY) ||
        key.equals(CedarModelConstants.JSON_LD_ID)) {
      return false;
    }
    return true;
  }

  private static boolean isCedarTemplateInstance(Map<String, Object> metadataRecord) {
    if (metadataRecord.containsKey(CedarModelConstants.IS_BASED_ON)) {
      return Pattern.matches(CedarConstants.CEDAR_TEMPLATE_URI_REGEX,
          (String)metadataRecord.get(CedarModelConstants.IS_BASED_ON));
    }
    return false;
  }

//  private List<InfoField> generateInfoFieldsFromInstance(FileSystemResource folderServerNode,
//                                                         CedarRequestContext requestContext, boolean
//                                                         isIndexRegenerationTask)
//      throws CedarProcessingException {
//
//    if (folderServerNode.getType().equals(CedarResourceType.INSTANCE)) {
//
//      List<InfoField> infoFields = new ArrayList<>();
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
//        List<TemplateNode> templateNodes = templateContentExtractor.getTemplateNodes(template, CedarResourceType
//        .TEMPLATE);
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
//        InfoField infoField = null;
//        try {
//          String fieldValueUri = fieldValue.getFieldValueUri();
//          if (fieldValueUri != null) {
//            fieldValueUri = URLEncoder.encode(fieldValueUri, StandardCharsets.UTF_8.toString());
//          }
//          infoField = new InfoField(fieldName, fieldPrefLabel, fieldValue.generatePathBracketNotation(),
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


}

