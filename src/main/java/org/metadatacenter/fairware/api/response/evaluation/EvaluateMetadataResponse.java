package org.metadatacenter.fairware.api.response.evaluation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.metadatacenter.fairware.api.response.alignment.AlignmentReport;
import org.metadatacenter.fairware.shared.Metadata;
import org.metadatacenter.fairware.shared.MetadataSpecification;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluateMetadataResponse {

  private static final String METADATA_ARTIFACT = "metadataArtifact";
  private static final String METADATA_SPECIFICATION = "metadataSpecification";
  private static final String ALIGNMENT_REPORT = "alignmentReport";
  private static final String EVALUATION_REPORT = "evaluationReport";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataResponse create(@Nonnull @JsonProperty(METADATA_ARTIFACT) Metadata metadata,
                                                @Nonnull @JsonProperty(METADATA_SPECIFICATION) MetadataSpecification metadataSpecification,
                                                @Nonnull @JsonProperty(ALIGNMENT_REPORT) AlignmentReport alignmentReport,
                                                @Nonnull @JsonProperty(EVALUATION_REPORT) EvaluationReport evaluationReport) {
    return new AutoValue_EvaluateMetadataResponse(metadata, metadataSpecification, alignmentReport, evaluationReport);
  }

  @Nonnull
  @JsonProperty(METADATA_ARTIFACT)
  public abstract Metadata getMetadataArtifact();

  @Nonnull
  @JsonProperty(METADATA_SPECIFICATION)
  public abstract MetadataSpecification getMetadataSpecification();

  @Nonnull
  @JsonProperty(ALIGNMENT_REPORT)
  public abstract AlignmentReport getAlignmentReport();

  @Nonnull
  @JsonProperty(EVALUATION_REPORT)
  public abstract EvaluationReport getEvaluationReport();
}
