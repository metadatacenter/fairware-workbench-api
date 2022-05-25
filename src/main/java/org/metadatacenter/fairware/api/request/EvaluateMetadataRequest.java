package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.api.shared.FieldAlignment;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
public abstract class EvaluateMetadataRequest {

  private static final String TEMPLATE_ID = "templateId";
  private static final String METADATA_RECORD_ID = "metadataRecordId";
  private static final String METADATA_RECORD = "metadataRecord";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataRequest create(@Nonnull @JsonProperty(TEMPLATE_ID) Optional<String> templateId,
                                               @Nonnull @JsonProperty(METADATA_RECORD_ID) Optional<String> metadataRecordId,
                                               @Nonnull @JsonProperty(METADATA_RECORD) Optional<ImmutableMap<String, Object>> metadataRecord) {
    return new AutoValue_EvaluateMetadataRequest(templateId, metadataRecordId, metadataRecord);
  }

  @Nonnull
  @JsonProperty(TEMPLATE_ID)
  public abstract Optional<String> getTemplateId();

  @Nonnull
  @JsonProperty(METADATA_RECORD_ID)
  public abstract Optional<String> getMetadataRecordId();

  @Nonnull
  @JsonProperty(METADATA_RECORD)
  public abstract Optional<ImmutableMap<String, Object>> getMetadataRecord();
}
