package org.metadatacenter.fairware.api.response.alignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.api.shared.FieldAlignment;

import javax.annotation.Nonnull;

@AutoValue
public abstract class AlignMetadataResponse {

  private static final String FIELD_ALIGNMENTS = "fieldAlignments";

  @Nonnull
  public static AlignMetadataResponse create(@Nonnull @JsonProperty(FIELD_ALIGNMENTS) ImmutableList<FieldAlignment> fieldAlignments) {
    return new AutoValue_AlignMetadataResponse(fieldAlignments);
  }

  @Nonnull
  @JsonProperty(FIELD_ALIGNMENTS)
  public abstract ImmutableList<FieldAlignment> getFieldAlignments();

  @JsonIgnore
  public int getTotalCount() {
    return getFieldAlignments().size();
  }
}
