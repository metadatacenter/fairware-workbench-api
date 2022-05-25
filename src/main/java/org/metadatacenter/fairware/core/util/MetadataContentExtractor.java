package org.metadatacenter.fairware.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateInstanceContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utilities to extract information from CEDAR Template Instances
 */
public class MetadataContentExtractor {

  @Nonnull
  private final MapBasedMetadataContentExtractor mapBasedMetadataContentExtractor;

  @Nonnull
  private final CedarTemplateInstanceContentExtractor cedarTemplateInstanceContentExtractor;

  public MetadataContentExtractor(@Nonnull MapBasedMetadataContentExtractor mapBasedMetadataContentExtractor,
                                  @Nonnull CedarTemplateInstanceContentExtractor cedarTemplateInstanceContentExtractor) {
    this.mapBasedMetadataContentExtractor = checkNotNull(mapBasedMetadataContentExtractor);
    this.cedarTemplateInstanceContentExtractor = checkNotNull(cedarTemplateInstanceContentExtractor);
  }

  @Nonnull
  public ImmutableList<MetadataFieldInfo> extractMetadataFieldsInfo(@Nonnull ImmutableMap<String, Object> metadataRecord) {
    return mapBasedMetadataContentExtractor.generateInfoFieldsFromMetadata(metadataRecord);
  }

  @Nonnull
  public ImmutableList<MetadataFieldInfo> extractMetadataFieldsInfo(@Nonnull ImmutableMap<String, Object> metadataRecord,
                                                                    @Nonnull ImmutableMap<String, Object> template) throws IOException {
    if (cedarTemplateInstanceContentExtractor.isCedarTemplateInstance(metadataRecord)) {
      return cedarTemplateInstanceContentExtractor.generateInfoFieldsFromInstance(metadataRecord, template);
    } else {
      return mapBasedMetadataContentExtractor.generateInfoFieldsFromMetadata(metadataRecord);
    }
  }
}

