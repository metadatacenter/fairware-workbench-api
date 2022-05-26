package org.metadatacenter.fairware.core.services.citation;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.io.IOException;

public interface CitationServiceProvider {

  boolean isCompatible(@Nonnull String metadataRecordId);

  @Nonnull
  ImmutableMap<String, Object> retrieveMetadata(@Nonnull String metadataRecordId) throws IOException;
}
