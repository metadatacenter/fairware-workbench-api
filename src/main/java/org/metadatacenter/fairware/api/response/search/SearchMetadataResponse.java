package org.metadatacenter.fairware.api.response.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.shared.Metadata;

import javax.annotation.Nonnull;

@AutoValue
public abstract class SearchMetadataResponse {

  private static final String METADATA = "metadata";

  @Nonnull
  @JsonCreator
  public static SearchMetadataResponse create(@Nonnull @JsonProperty(METADATA) Metadata metadata) {
    return new AutoValue_SearchMetadataResponse(metadata);
  }

  @Nonnull
  @JsonProperty(METADATA)
  public abstract Metadata getMetadata();

  @Nonnull
  @JsonIgnore
  public String getMetadataRecordId() {
    return getMetadata().getMetadataRecordId();
  }

  @Nonnull
  @JsonIgnore
  public String getMetadataRecordName() {
    return getMetadata().getMetadataRecordName();
  }

  @Nonnull
  @JsonIgnore
  public ImmutableMap<String, Object> getMetadataRecord() {
    return getMetadata().getMetadataRecord();
  }
}
