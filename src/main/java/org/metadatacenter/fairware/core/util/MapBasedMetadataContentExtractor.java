package org.metadatacenter.fairware.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class MapBasedMetadataContentExtractor {

  @Nonnull
  public ImmutableList<MetadataFieldInfo> generateInfoFieldsFromMetadata(ImmutableMap<String, Object> metadata) {
    return generateInfoFieldsFromMetadata(metadata, Lists.newArrayList());
  }

  @Nonnull
  private ImmutableList<MetadataFieldInfo> generateInfoFieldsFromMetadata(@Nonnull Map<String, Object> metadataObject,
                                                                          @Nonnull List<String> currentPath) {
    var result = Lists.<MetadataFieldInfo>newArrayList();
    for (var entry : metadataObject.entrySet()) {
      var metadataField = entry.getKey();
      var metadataValue = entry.getValue();
      if (metadataValue == null || metadataValue.equals(Optional.empty())) {
        result.add(MetadataFieldInfo.create(metadataField,
            Optional.empty(),
            ImmutableList.copyOf(currentPath),
            Optional.empty(),
            Optional.empty()));
      } else if (metadataValue instanceof String || metadataValue instanceof Number) {
        result.add(MetadataFieldInfo.create(metadataField,
            Optional.empty(),
            ImmutableList.copyOf(currentPath),
            Optional.of(metadataValue),
            Optional.empty()));
      } else if (metadataValue instanceof Map) {
        var innerPath = Lists.newArrayList(currentPath);
        innerPath.add(metadataField);
        var innerResult = generateInfoFieldsFromMetadata(
            (Map<String, Object>) metadataValue,
            innerPath);
        result.addAll(innerResult);
      } else if (metadataValue instanceof List) { // Array of homogenous values
        var valueList = ((List) metadataValue);
        var firstValue = valueList.get(0);
        if (firstValue instanceof Map) { // Array of objects
          for (Object o : valueList) {
            var innerPath = Lists.newArrayList(currentPath);
            innerPath.add(metadataField);
            var innerResult = generateInfoFieldsFromMetadata(
                (Map<String, Object>) o,
                innerPath);
            result.addAll(innerResult);
          }
        } else {
          result.add(MetadataFieldInfo.create(metadataField,
              Optional.empty(),
              ImmutableList.copyOf(currentPath),
              Optional.of(valueList),
              Optional.empty()));
        }
      }
    }
    return ImmutableList.copyOf(result);
  }
}
