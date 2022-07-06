package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.shared.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class MetadataService {

  private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);

  private final ImmutableList<MetadataServiceProvider> citationServiceProviders;

  public MetadataService(@Nonnull ImmutableList<MetadataServiceProvider> citationServiceProviders) {
    this.citationServiceProviders = checkNotNull(citationServiceProviders);
  }

  @Nonnull
  public Metadata getMetadataById(String metadataId) throws IOException {
    for (var citationServiceProvider : citationServiceProviders) {
      if (citationServiceProvider.isCompatible(metadataId)) {
        return citationServiceProvider.getMetadataById(metadataId);
      }
    }
    throw new BadRequestException("Metadata record is not yet supported to handle: " + metadataId);
  }
}
