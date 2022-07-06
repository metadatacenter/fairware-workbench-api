package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.shared.FieldAlignment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class EvaluateMetadataRequest {

  private static final String METADATA_ID = "metadataId";
  private static final String TEMPLATE_ID = "templateId";
  private static final String FIELD_ALIGNMENTS = "fieldAlignments";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataRequest create(@Nonnull @JsonProperty(METADATA_ID) String metadataId,
                                               @Nonnull @JsonProperty(TEMPLATE_ID) String templateId,
                                               @Nullable @JsonProperty(FIELD_ALIGNMENTS) ImmutableList<FieldAlignment> fieldAlignments) {
    return new AutoValue_EvaluateMetadataRequest(metadataId, templateId, fieldAlignments);
  }

  @Nonnull
  @JsonProperty(METADATA_ID)
  public abstract String getMetadataId();

  @Nonnull
  @JsonProperty(TEMPLATE_ID)
  public abstract String getTemplateId();

  @Nullable
  @JsonProperty(FIELD_ALIGNMENTS)
  public abstract ImmutableList<FieldAlignment> getFieldAlignments();
}
