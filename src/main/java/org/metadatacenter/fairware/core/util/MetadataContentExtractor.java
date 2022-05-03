package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.constants.CedarConstants;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateInstanceContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utilities to extract information from CEDAR Template Instances
 */
public class MetadataContentExtractor {

  public List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> metadataRecord) throws UnsupportedEncodingException {
    return extractMetadataFieldsInfo(metadataRecord, null);
  }

  public List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> metadataRecord,
                                                           Map<String, Object> template) throws UnsupportedEncodingException {
    if (isCedarTemplateInstance(metadataRecord)) {
      return CedarTemplateInstanceContentExtractor.generateInfoFieldsFromInstance(metadataRecord, template);
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
  private List<MetadataFieldInfo> extractMetadataFieldsInfo(Map<String, Object> current,
                                                            List<String> currentPath,
                                                            List<MetadataFieldInfo> result) {
    for (Map.Entry<String, Object> entry : current.entrySet()) {
      if (entry.getValue() == null) {
        result.add(new MetadataFieldInfo(entry.getKey(), null, new ArrayList<>(currentPath), null, null));
      }
      else if (entry.getValue() instanceof String || entry.getValue() instanceof Number) { // String or Numeric
        result.add(new MetadataFieldInfo(entry.getKey(), null, new ArrayList<>(currentPath), entry.getValue(),null));
      }
      else if (entry.getValue() instanceof Map<?, ?>) { // Another object
        currentPath.add(entry.getKey());
        extractMetadataFieldsInfo((Map<String, Object>) entry.getValue(), currentPath, result);
        currentPath.remove(entry.getKey());
      }
      else if (entry.getValue() instanceof List<?>) { // Array
        Object firstValue = ((List<?>) entry.getValue()).get(0);
        if (firstValue instanceof String || firstValue instanceof Number) { // non-primitive types, e.g., String[]
          result.add(new MetadataFieldInfo(entry.getKey(), null, new ArrayList<>(currentPath), entry.getValue(), null));
        }
        else if (firstValue instanceof Map<?, ?>) { // Another object
          currentPath.add(entry.getKey());
          extractMetadataFieldsInfo((Map<String, Object>) firstValue, currentPath, result);
          currentPath.remove(entry.getKey());
        }
        // else, do nothing. Any other relevant cases?
      }
    }
    return result;
  }

  private boolean isCedarTemplateInstance(Map<String, Object> metadataRecord) {
    if (metadataRecord.containsKey(CedarModelConstants.IS_BASED_ON)) {
      return Pattern.matches(CedarConstants.CEDAR_TEMPLATE_URI_REGEX,
          (String) metadataRecord.get(CedarModelConstants.IS_BASED_ON));
    } else {
      return false;
    }
  }
}

