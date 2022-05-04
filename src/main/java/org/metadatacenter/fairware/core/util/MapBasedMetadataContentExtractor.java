package org.metadatacenter.fairware.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class MapBasedMetadataContentExtractor {

  public List<MetadataFieldInfo> generateInfoFieldsFromMetadata(Map<String, Object> metadata) {
    List<String> pathCollector = Lists.newArrayList();
    return generateInfoFieldsFromMetadata(metadata, pathCollector);
  }

  private List<MetadataFieldInfo> generateInfoFieldsFromMetadata(@Nonnull Map<String, Object> metadataObject,
                                                                 @Nonnull List<String> currentPath) {
    List<MetadataFieldInfo> result = Lists.newArrayList();
    for (Map.Entry<String, Object> entry : metadataObject.entrySet()) {
      String metadataField = entry.getKey();
      Object metadataValue = entry.getValue();
      if (metadataValue == null) {
        result.add(MetadataFieldInfo.create(metadataField, null, ImmutableList.copyOf(currentPath), null, null));
      }
      else if (metadataValue instanceof String || metadataValue instanceof Number) { // String or Numeric
        result.add(MetadataFieldInfo.create(metadataField, null, ImmutableList.copyOf(currentPath), metadataValue,null));
      }
      else if (metadataValue instanceof Map) { // Another object
        List<String> innerPath = Lists.newArrayList(currentPath);
        innerPath.add(metadataField);
        List<MetadataFieldInfo> innerResult = generateInfoFieldsFromMetadata((Map) metadataValue, innerPath);
        result.addAll(innerResult);
      }
      else if (metadataValue instanceof List) { // Array of homogenous values
        Object firstValue = ((List) metadataValue).get(0);
        if (firstValue instanceof String || firstValue instanceof Number) { // Array of primitive values
          result.add(MetadataFieldInfo.create(metadataField, null, ImmutableList.copyOf(currentPath), metadataValue, null));
        }
        else if (firstValue instanceof Map) { // Array of objects
          List<String> innerPath = Lists.newArrayList(currentPath);
          innerPath.add(metadataField);
          List<MetadataFieldInfo> innerResult = generateInfoFieldsFromMetadata((Map) firstValue, innerPath);
          result.addAll(innerResult);
        }
        // else, do nothing. Any other relevant cases?
      }
    }
    return result;
  }
}
