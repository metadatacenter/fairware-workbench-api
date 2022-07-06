package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluateMetadataRequest {

  private static final String METADATA_ID = "metadataId";
  private static final String TEMPLATE_ID = "templateId";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataRequest create(@Nonnull @JsonProperty(METADATA_ID) String metadataId,
                                               @Nonnull @JsonProperty(TEMPLATE_ID) String templateId) {
    return new AutoValue_EvaluateMetadataRequest(metadataId, templateId);
  }

  @Nonnull
  @JsonProperty(METADATA_ID)
  public abstract String getMetadataId();

  @Nonnull
  @JsonProperty(TEMPLATE_ID)
  public abstract String getTemplateId();
}
