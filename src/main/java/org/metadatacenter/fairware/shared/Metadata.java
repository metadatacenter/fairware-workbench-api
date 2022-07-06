package org.metadatacenter.fairware.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;

@AutoValue
public abstract class Metadata {

  private final static String METADATA_ID = "metadataId";
  private final static String METADATA_NAME = "metadataName";
  private final static String METADATA_FIELDS = "metadataFields";
  private final static String METADATA_RECORD = "metadataRecord";

  @Nonnull
  @JsonCreator
  public static Metadata create(@Nonnull @JsonProperty(METADATA_ID) String metadataId,
                                @Nonnull @JsonProperty(METADATA_NAME) String metadataName,
                                @Nonnull @JsonProperty(METADATA_FIELDS) ImmutableSet<String> metadataFields,
                                @Nonnull @JsonProperty(METADATA_RECORD) ImmutableMap<String, Object> metadataRecord) {
    return new AutoValue_Metadata(metadataId, metadataName, metadataFields, metadataRecord);
  }

  @Nonnull
  @JsonProperty(METADATA_ID)
  public abstract String getMetadataId();

  @Nonnull
  @JsonProperty(METADATA_NAME)
  public abstract String getMetadataName();

  @Nonnull
  @JsonProperty(METADATA_FIELDS)
  public abstract ImmutableSet<String> getMetadataFields();

  @Nonnull
  @JsonProperty(METADATA_RECORD)
  public abstract ImmutableMap<String, Object> getMetadataRecord();
}
