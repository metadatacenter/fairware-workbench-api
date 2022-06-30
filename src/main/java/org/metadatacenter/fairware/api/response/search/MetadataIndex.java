package org.metadatacenter.fairware.api.response.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@AutoValue
public abstract class MetadataIndex {

  private final static String METADATA_RECORD_ID = "metadataRecordId";
  private final static String METADATA_RECORD_NAME = "metadataRecordName";
  private final static String METADATA_RECORD = "metadataRecord";

  @Nonnull
  @JsonCreator
  public static MetadataIndex create(@Nonnull @JsonProperty(METADATA_RECORD_ID) String metadataRecordId,
                                     @Nonnull @JsonProperty(METADATA_RECORD_NAME) String metadataRecordName,
                                     @Nonnull @JsonProperty(METADATA_RECORD) ImmutableMap<String, Object> metadataRecord) {
    return new AutoValue_MetadataIndex(metadataRecordId, metadataRecordName, metadataRecord);
  }

  @Nonnull
  @JsonProperty(METADATA_RECORD_ID)
  public abstract String getMetadataRecordId();

  @Nonnull
  @JsonProperty(METADATA_RECORD_NAME)
  public abstract String getMetadataRecordName();

  @Nonnull
  @JsonProperty(METADATA_RECORD)
  public abstract ImmutableMap<String, Object> getMetadataRecord();
}
