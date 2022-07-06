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
  public static Metadata create(@Nonnull @JsonProperty(METADATA_ID) String id,
                                @Nonnull @JsonProperty(METADATA_NAME) String name,
                                @Nonnull @JsonProperty(METADATA_FIELDS) ImmutableSet<String> fields,
                                @Nonnull @JsonProperty(METADATA_RECORD) ImmutableMap<String, Object> metadataRecord) {
    return new AutoValue_Metadata(id, name, fields, metadataRecord);
  }

  @Nonnull
  @JsonProperty(METADATA_ID)
  public abstract String getId();

  @Nonnull
  @JsonProperty(METADATA_NAME)
  public abstract String getName();

  @Nonnull
  @JsonProperty(METADATA_FIELDS)
  public abstract ImmutableSet<String> getFields();

  @Nonnull
  @JsonProperty(METADATA_RECORD)
  public abstract ImmutableMap<String, Object> getMetadataRecord();
}
