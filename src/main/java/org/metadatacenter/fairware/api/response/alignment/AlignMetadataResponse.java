package org.metadatacenter.fairware.api.response.alignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.shared.FieldAlignment;

import javax.annotation.Nonnull;

@AutoValue
public abstract class AlignMetadataResponse {

  private static final String ALIGNMENT_REPORT = "alignmentReport";

  @Nonnull
  public static AlignMetadataResponse create(@Nonnull @JsonProperty(ALIGNMENT_REPORT) AlignmentReport alignmentReport) {
    return new AutoValue_AlignMetadataResponse(alignmentReport);
  }

  @Nonnull
  @JsonProperty(ALIGNMENT_REPORT)
  public abstract AlignmentReport getAlignmentReport();

  @Nonnull
  @JsonIgnore
  public ImmutableList<FieldAlignment> getFieldAlignments() {
    return getAlignmentReport().getFieldAlignments();
  }
}
