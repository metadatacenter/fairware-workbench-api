package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class AlignMetadataRequest {

  private static final String TEMPLATE_ID = "templateId";
  private static final String METADATA_ID = "metadataId";

  @Nonnull
  @JsonCreator
  public static AlignMetadataRequest create(@Nonnull @JsonProperty(TEMPLATE_ID) String templateId,
                                            @Nonnull @JsonProperty(METADATA_ID) String metadataId) {
    return new AutoValue_AlignMetadataRequest(templateId, metadataId);
  }

  @Nonnull
  @JsonProperty(TEMPLATE_ID)
  public abstract String getTemplateId();

  @Nonnull
  @JsonProperty(METADATA_ID)
  public abstract String getMetadataId();
}
