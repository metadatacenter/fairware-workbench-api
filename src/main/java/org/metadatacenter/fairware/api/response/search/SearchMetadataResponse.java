package org.metadatacenter.fairware.api.response.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@AutoValue
public abstract class SearchMetadataResponse {

  private static final String METADATA_RECORD = "metadataRecord";

  @Nonnull
  @JsonCreator
  public static SearchMetadataResponse create(@Nonnull @JsonProperty(METADATA_RECORD) ImmutableMap<String, Object> metadataRecord) {
    return new AutoValue_SearchMetadataResponse(metadataRecord);
  }

  @Nonnull
  @JsonProperty(METADATA_RECORD)
  public abstract ImmutableMap<String, Object> getMetadataRecord();
}
