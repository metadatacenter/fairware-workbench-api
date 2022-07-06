package org.metadatacenter.fairware.api.response.alignment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.shared.FieldAlignment;

import javax.annotation.Nonnull;

@AutoValue
public abstract class AlignmentReport {

  private static final String FIELD_ALIGNMENTS = "fieldAlignments";

  @Nonnull
  @JsonCreator
  public static AlignmentReport create(@Nonnull @JsonProperty(FIELD_ALIGNMENTS) ImmutableList<FieldAlignment> fieldAlignments) {
    return new AutoValue_AlignmentReport(fieldAlignments);
  }

  @Nonnull
  @JsonProperty(FIELD_ALIGNMENTS)
  public abstract ImmutableList<FieldAlignment> getFieldAlignments();
}
