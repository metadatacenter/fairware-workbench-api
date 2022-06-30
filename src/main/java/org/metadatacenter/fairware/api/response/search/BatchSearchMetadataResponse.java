package org.metadatacenter.fairware.api.response.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@AutoValue
public abstract class BatchSearchMetadataResponse {

  private static final String METADATA_INDEXES = "metadataIndexes";

  @Nonnull
  @JsonCreator
  public static BatchSearchMetadataResponse create(@Nonnull @JsonProperty(METADATA_INDEXES)
                                                       ImmutableList<MetadataIndex> metadataIndexes) {
    return new AutoValue_BatchSearchMetadataResponse(metadataIndexes);
  }

  @Nonnull
  @JsonProperty(METADATA_INDEXES)
  public abstract ImmutableList<MetadataIndex> getMetadataIndexes();
}
