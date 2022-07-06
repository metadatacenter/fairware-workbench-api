package org.metadatacenter.fairware.api.response.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@AutoValue
public abstract class SearchMetadataResponse {

  private static final String METADATA_INDEX = "metadataIndex";

  @Nonnull
  @JsonCreator
  public static SearchMetadataResponse create(@Nonnull @JsonProperty(METADATA_INDEX) MetadataIndex metadataIndex) {
    return new AutoValue_SearchMetadataResponse(metadataIndex);
  }

  @Nonnull
  @JsonProperty(METADATA_INDEX)
  public abstract MetadataIndex getMetadataIndex();

  @Nonnull
  @JsonIgnore
  public String getMetadataRecordId() {
    return getMetadataIndex().getMetadataRecordId();
  }

  @Nonnull
  @JsonIgnore
  public String getMetadataRecordName() {
    return getMetadataIndex().getMetadataRecordName();
  }

  @Nonnull
  @JsonIgnore
  public ImmutableMap<String, Object> getMetadataRecord() {
    return getMetadataIndex().getMetadataRecord();
  }
}
