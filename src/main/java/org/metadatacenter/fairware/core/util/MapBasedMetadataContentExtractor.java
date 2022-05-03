package org.metadatacenter.fairware.core.util;

import com.google.common.collect.Lists;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class MapBasedMetadataContentExtractor {

  public List<MetadataFieldInfo> generateInfoFieldsFromMetadata(Map<String, Object> metadata) {
    List<String> pathCollector = Lists.newArrayList();
    List<MetadataFieldInfo> resultCollector = Lists.newArrayList();
    generateInfoFieldsFromMetadata(metadata, pathCollector, resultCollector);
    return Lists.newArrayList(resultCollector);
  }

  private void generateInfoFieldsFromMetadata(@Nonnull Map<String, Object> metadataObject,
                                              @Nonnull List<String> pathCollector,
                                              @Nonnull List<MetadataFieldInfo> resultCollector) {
    for (Map.Entry<String, Object> entry : metadataObject.entrySet()) {
      if (entry.getValue() == null) {
        resultCollector.add(new MetadataFieldInfo(entry.getKey(), null, new ArrayList<>(pathCollector), null, null));
      }
      else if (entry.getValue() instanceof String || entry.getValue() instanceof Number) { // String or Numeric
        resultCollector.add(new MetadataFieldInfo(entry.getKey(), null, new ArrayList<>(pathCollector), entry.getValue(),null));
      }
      else if (entry.getValue() instanceof Map<?, ?>) { // Another object
        pathCollector.add(entry.getKey());
        generateInfoFieldsFromMetadata((Map<String, Object>) entry.getValue(), pathCollector, resultCollector);
        pathCollector.remove(entry.getKey());
      }
      else if (entry.getValue() instanceof List<?>) { // Array
        Object firstValue = ((List<?>) entry.getValue()).get(0);
        if (firstValue instanceof String || firstValue instanceof Number) { // non-primitive types, e.g., String[]
          resultCollector.add(new MetadataFieldInfo(entry.getKey(), null, new ArrayList<>(pathCollector), entry.getValue(), null));
        }
        else if (firstValue instanceof Map<?, ?>) { // Another object
          pathCollector.add(entry.getKey());
          generateInfoFieldsFromMetadata((Map<String, Object>) firstValue, pathCollector, resultCollector);
          pathCollector.remove(entry.getKey());
        }
        // else, do nothing. Any other relevant cases?
      }
    }
  }
}
