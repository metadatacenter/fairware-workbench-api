package org.metadatacenter.fairware.api.response.alignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.shared.Metadata;
import org.metadatacenter.fairware.shared.MetadataSpecification;

import javax.annotation.Nonnull;

@AutoValue
public abstract class AlignMetadataResponse {

  private static final String METADATA_ARTIFACT = "metadataArtifact";
  private static final String METADATA_SPECIFICATION = "metadataSpecification";
  private static final String ALIGNMENT_REPORT = "alignmentReport";

  @Nonnull
  public static AlignMetadataResponse create(@Nonnull @JsonProperty(METADATA_ARTIFACT) Metadata metadata,
                                             @Nonnull @JsonProperty(METADATA_SPECIFICATION) MetadataSpecification metadataSpecification,
                                             @Nonnull @JsonProperty(ALIGNMENT_REPORT) AlignmentReport alignmentReport) {
    return new AutoValue_AlignMetadataResponse(metadata, metadataSpecification, alignmentReport);
  }

  @Nonnull
  @JsonProperty(METADATA_ARTIFACT)
  public abstract Metadata getMetadataArtifact();

  @Nonnull
  @JsonIgnore
  public ImmutableSet<String> getMetadataFields() {
    return getMetadataArtifact().getFields();
  }

  @Nonnull
  @JsonProperty(METADATA_SPECIFICATION)
  public abstract MetadataSpecification getMetadataSpecification();

  @Nonnull
  @JsonIgnore
  public ImmutableMap<String, String> getTemplateFields() {
    return getMetadataSpecification().getTemplateFields();
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
