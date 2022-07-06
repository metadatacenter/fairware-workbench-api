package org.metadatacenter.fairware.core.services;

import org.metadatacenter.fairware.shared.Metadata;

import javax.annotation.Nonnull;
import java.io.IOException;

public interface MetadataServiceProvider {

  boolean isCompatible(@Nonnull String metadataId);

  @Nonnull
  Metadata getMetadataById(@Nonnull String metadataId) throws IOException;
}
