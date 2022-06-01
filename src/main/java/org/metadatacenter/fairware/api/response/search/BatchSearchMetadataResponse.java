package org.metadatacenter.fairware.api.response.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@AutoValue
public abstract class BatchSearchMetadataResponse {

  private static final String METADATA_RECORDS = "metadataRecords";

  @Nonnull
  @JsonCreator
  public static BatchSearchMetadataResponse create(@Nonnull @JsonProperty(METADATA_RECORDS)
                                                       ImmutableList<ImmutableMap<String, Object>> metadataRecords) {
    return new AutoValue_BatchSearchMetadataResponse(metadataRecords);
  }

  @Nonnull
  @JsonProperty(METADATA_RECORDS)
  public abstract ImmutableList<ImmutableMap<String, Object>> getMetadataRecords();
}
