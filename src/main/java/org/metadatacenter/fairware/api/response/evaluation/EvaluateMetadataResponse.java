package org.metadatacenter.fairware.api.response.evaluation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.shared.MetadataSpecification;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluateMetadataResponse {

  private static final String METADATA_ID = "metadataId";
  private static final String METADATA_NAME = "metadataName";
  private static final String METADATA_RECORD = "metadataRecord";
  private static final String METADATA_SPECIFICATION = "metadataSpecification";
  private static final String EVALUATION_REPORT = "evaluationReport";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataResponse create(@Nonnull @JsonProperty(METADATA_ID) String metadataId,
                                                @Nonnull @JsonProperty(METADATA_NAME) String metadataName,
                                                @Nonnull @JsonProperty(METADATA_RECORD) ImmutableMap<String, Object> metadataRecord,
                                                @Nonnull @JsonProperty(METADATA_SPECIFICATION) MetadataSpecification metadataSpecification,
                                                @Nonnull @JsonProperty(EVALUATION_REPORT) EvaluationReport evaluationReport) {
    return new AutoValue_EvaluateMetadataResponse(metadataId, metadataName, metadataRecord,
        metadataSpecification, evaluationReport);
  }

  @Nonnull
  @JsonProperty(METADATA_ID)
  public abstract String getMetadataId();

  @Nonnull
  @JsonProperty(METADATA_NAME)
  public abstract String getMetadataName();

  @Nonnull
  @JsonProperty(METADATA_RECORD)
  public abstract ImmutableMap<String, Object> getMetadataRecord();

  @Nonnull
  @JsonProperty(METADATA_SPECIFICATION)
  public abstract MetadataSpecification getMetadataSpecification();

  @Nonnull
  @JsonProperty(EVALUATION_REPORT)
  public abstract EvaluationReport getEvaluationReport();
}
