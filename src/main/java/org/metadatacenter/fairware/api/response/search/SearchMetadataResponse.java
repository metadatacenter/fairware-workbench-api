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

  private static final String METADATA_ARTIFACT = "metadataArtifact";

  @Nonnull
  @JsonCreator
  public static SearchMetadataResponse create(@Nonnull @JsonProperty(METADATA_ARTIFACT) Metadata metadata) {
    return new AutoValue_SearchMetadataResponse(metadata);
  }

  @Nonnull
  @JsonProperty(METADATA_ARTIFACT)
  public abstract Metadata getMetadataArtifact();

  @Nonnull
  @JsonIgnore
  public String getMetadataId() {
    return getMetadataArtifact().getId();
  }

  @Nonnull
  @JsonIgnore
  public String getMetadataName() {
    return getMetadataArtifact().getName();
  }

  @Nonnull
  @JsonIgnore
  public ImmutableMap<String, Object> getMetadataRecord() {
    return getMetadataArtifact().getMetadataRecord();
  }
}
