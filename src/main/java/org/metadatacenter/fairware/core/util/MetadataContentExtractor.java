package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateInstanceContentExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
  public List<MetadataFieldInfo> extractMetadataFieldsInfo(@Nonnull Map<String, Object> metadataRecord) {
    return mapBasedMetadataContentExtractor.generateInfoFieldsFromMetadata(metadataRecord);
  }

  @Nonnull
  public List<MetadataFieldInfo> extractMetadataFieldsInfo(@Nonnull Map<String, Object> metadataRecord,
                                                           @Nonnull Map<String, Object> template) throws IOException {
    if (cedarTemplateInstanceContentExtractor.isCedarTemplateInstance(metadataRecord)) {
      return cedarTemplateInstanceContentExtractor.generateInfoFieldsFromInstance(metadataRecord, template);
    } else {
      throw new IOException("Metadata is not a CEDAR template instance");
    }
  }
}

