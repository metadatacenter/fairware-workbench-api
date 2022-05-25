package org.metadatacenter.fairware.core.util.cedar.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.metadatacenter.fairware.constants.CedarConstants;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.FieldValue;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateNodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

/**
 * Utilities to extract information from CEDAR Template Instances
 */
public class CedarTemplateInstanceContentExtractor {

  private static final Logger log = LoggerFactory.getLogger(CedarTemplateInstanceContentExtractor.class);
  private static final ObjectMapper mapper = new ObjectMapper();

  @Nonnull
  public ImmutableList<MetadataFieldInfo> generateInfoFieldsFromInstance(ImmutableMap<String, Object> templateInstance,
                                                                         ImmutableMap<String, Object> template) {
    return generateInfoFieldsFromInstance(
        mapper.convertValue(templateInstance, JsonNode.class),
        mapper.convertValue(template, JsonNode.class));
  }

  public boolean isCedarTemplateInstance(ImmutableMap<String, Object> metadataRecord) {
    if (metadataRecord.containsKey(CedarModelConstants.IS_BASED_ON)) {
      return Pattern.matches(CedarConstants.CEDAR_TEMPLATE_URI_REGEX,
          (String) metadataRecord.get(CedarModelConstants.IS_BASED_ON));
    } else {
      return false;
    }
  }

  /**
   * Generates a list of InfoField objects from a template instance. These InfoFields objects contain information
   * about the template fields and the values entered for the template instance
   */
  public ImmutableList<MetadataFieldInfo> generateInfoFieldsFromInstance(JsonNode templateInstance, JsonNode template) {
    var infoFields = Lists.<MetadataFieldInfo>newArrayList();
    var templateNodes = CedarTemplateContentExtractor.getTemplateNodes(template);
    var templateNodesMap = templateNodes.stream()
        .collect(collectingAndThen(
            toMap(node -> node.generateFullPathDotNotation(),
                node -> node),
            ImmutableMap::copyOf));

    var fieldValues = getFieldValues(templateInstance,
        templateNodesMap,
        Lists.<String>newArrayList(),
        Lists.<FieldValue>newArrayList());

    for (var fieldValue : fieldValues) {
      var fieldFullPath = fieldValue.generateFullPathDotNotation();
      if (!templateNodesMap.containsKey(fieldFullPath)) {
        log.error("Field path not found in templateNodesMap: " + fieldFullPath);
        throw new IllegalArgumentException("Field path not found in templateNodesMap: " + fieldFullPath);
      }
      var templateNode = templateNodesMap.get(fieldFullPath);
      var fieldName = templateNode.getName();
      var fieldPrefLabel = templateNode.getPrefLabel();
      var infoField = MetadataFieldInfo.create(fieldName,
          fieldPrefLabel,
          fieldValue.getFieldPath(),
          Optional.ofNullable(fieldValue.getFieldValue().get()),
          fieldValue.getFieldValueUri());
      if (!infoFields.contains(infoField)) {
        infoFields.add(infoField);
      }
    }
    return ImmutableList.copyOf(infoFields);
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
   * @return an immutable list of field values
   */
  public ImmutableList<FieldValue> getFieldValues(JsonNode currentNode,
                                                  ImmutableMap<String, TemplateNodeInfo> templateNodesMap,
                                                  List<String> currentPath,
                                                  List<FieldValue> results) {

    var fieldNodesIter = currentNode.fields();
    while (fieldNodesIter.hasNext()) {
      var fieldNode = fieldNodesIter.next();
      var fieldName = fieldNode.getKey();
      var fieldAddress = GeneralUtil.generateFullPathDotNotation(currentPath, fieldName);
      if (templateNodesMap.containsKey(fieldAddress)) {
        currentPath.add(fieldName);
        var templateNode = templateNodesMap.get(fieldAddress);
        if (!templateNode.isArray()) {   // Not an array
          var fieldValueNode = fieldNode.getValue();
          if (templateNode.isTemplateElementNode()) {
            getFieldValues(fieldValueNode, templateNodesMap, currentPath, results);
          } else if (templateNode.isTemplateFieldNode()) {
            var fieldValue = generateFieldValue(fieldValueNode, ImmutableList.copyOf(currentPath));
            results.add(fieldValue);
          } else {
            throw new IllegalArgumentException("Unrecognized node type. The template node must be either a " +
                "Template Field or a Template Element. Node type: " + templateNode.getName());
          }
        } else {
          if (templateNode.isTemplateElementNode()) {
            for (var fieldValueNode : fieldNode.getValue()) {
              getFieldValues(fieldValueNode, templateNodesMap, currentPath, results);
            }
          } else if (templateNode.isTemplateFieldNode()) {
            for (var fieldValueNode : fieldNode.getValue()) {
              var fieldValue = generateFieldValue(fieldValueNode, ImmutableList.copyOf(currentPath));
              results.add(fieldValue);
            }
          } else {
            throw new IllegalArgumentException("Unrecognized node type. The template node must be either a " +
                "Template Field or a Template Element. Node type: " + templateNode.getName());
          }
        }
        currentPath.remove(currentPath.size() - 1);
      }
    }
    return ImmutableList.copyOf(results);
  }

  @Nonnull
  private FieldValue generateFieldValue(JsonNode fieldValueNode, ImmutableList<String> fieldPath) {
    String fieldName = fieldPath.get(fieldPath.size() - 1);  // the last element is the field name
    if (fieldValueNode.hasNonNull(CedarModelConstants.JSON_LD_VALUE)) {
      var fieldValue = fieldValueNode.get(CedarModelConstants.JSON_LD_VALUE).asText();
      return FieldValue.create(fieldName, Optional.of(fieldValue), Optional.empty(), fieldPath);
    } else if (fieldValueNode.hasNonNull(CedarModelConstants.JSON_LD_ID)) {
      var fieldValueAsUri = Optional.of(fieldValueNode.get(CedarModelConstants.JSON_LD_ID).asText());
      var fieldValueAsLabel = Optional.<String>empty();
      if (fieldValueNode.hasNonNull(CedarModelConstants.RDFS_LABEL)) {
        fieldValueAsLabel = Optional.of(fieldValueNode.get(CedarModelConstants.RDFS_LABEL).asText());
      }
      return FieldValue.create(fieldName, fieldValueAsLabel, fieldValueAsUri, fieldPath);
    } else {
      throw new IllegalArgumentException("Invalid CEDAR node: " + fieldValueNode.asText());
    }
  }
}
