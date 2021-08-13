package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath)));
      }
      else if (entry.getValue() instanceof Map<?, ?>) { // Another object
        currentPath.add(entry.getKey());
        extractMetadataFieldsInfo((Map<String, Object>) entry.getValue(), currentPath, result);
      }
      else if (entry.getValue() instanceof List<?>) { // Array
        Object firstValue = ((List<?>)entry.getValue()).get(0);
        if (firstValue instanceof String || firstValue instanceof Number) { // non-primitive types, e.g., String[]
          result.add(new MetadataFieldInfo(entry.getKey(), new ArrayList<>(currentPath)));
        }
        else if (firstValue instanceof Map<?, ?>) { // Another object
          currentPath.add(entry.getKey());
          extractMetadataFieldsInfo((Map<String, Object>) firstValue, currentPath, result);
        }
        // else, do nothing. Any other relevant cases?

      }

    }
    return result;
  }
}
