package org.metadatacenter.fairware.core.services.citation;

import org.metadatacenter.fairware.shared.MetadataIndex;

import javax.annotation.Nonnull;
import java.io.IOException;

public interface CitationServiceProvider {

  boolean isCompatible(@Nonnull String metadataRecordId);

  @Nonnull
  MetadataIndex getMetadataIndex(@Nonnull String metadataRecordId) throws IOException;
}
