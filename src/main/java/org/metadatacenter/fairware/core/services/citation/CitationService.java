package org.metadatacenter.fairware.core.services.citation;

import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.api.response.search.MetadataIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class CitationService {

  private static final Logger logger = LoggerFactory.getLogger(CitationService.class);

  private final ImmutableList<CitationServiceProvider> citationServiceProviders;

  public CitationService(@Nonnull ImmutableList<CitationServiceProvider> citationServiceProviders) {
    this.citationServiceProviders = checkNotNull(citationServiceProviders);
  }

  @Nonnull
  public MetadataIndex getMetadataIndexById(String metadataRecordId) throws IOException {
    for (var citationServiceProvider : citationServiceProviders) {
      if (citationServiceProvider.isCompatible(metadataRecordId)) {
        return citationServiceProvider.getMetadataIndex(metadataRecordId);
      }
    }
    throw new BadRequestException("Metadata record is not yet supported to handle: " + metadataRecordId);
  }
}
