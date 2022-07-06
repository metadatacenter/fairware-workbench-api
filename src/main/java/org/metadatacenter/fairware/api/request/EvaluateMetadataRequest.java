package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluateMetadataRequest {

  private static final String METADATA_RECORD_ID = "metadataRecordId";
  private static final String TEMPLATE_ID = "templateId";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataRequest create(@Nonnull @JsonProperty(METADATA_RECORD_ID) String metadataRecordId,
                                               @Nonnull @JsonProperty(TEMPLATE_ID) String templateId) {
    return new AutoValue_EvaluateMetadataRequest(metadataRecordId, templateId);
  }

  @Nonnull
  @JsonProperty(METADATA_RECORD_ID)
  public abstract String getMetadataRecordId();

  @Nonnull
  @JsonProperty(TEMPLATE_ID)
  public abstract String getTemplateId();
}
