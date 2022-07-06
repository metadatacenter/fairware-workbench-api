package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Optional;

@AutoValue
public abstract class EvaluateMetadataResponse {

  private static final String METADATA_RECORD_ID = "metadataRecordId";
  private static final String METADATA_RECORD_NAME = "metadataRecordName";
  private static final String METADATA_RECORD = "metadataRecord";
  private static final String METADATA_SPECIFICATION = "metadataSpecification";
  private static final String EVALUATION_REPORT = "evaluationReport";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataResponse create(@Nonnull @JsonProperty(METADATA_RECORD_ID) String metadataRecordId,
                                                @Nonnull @JsonProperty(METADATA_RECORD_NAME) String metadataRecordName,
                                                @Nonnull @JsonProperty(METADATA_RECORD) ImmutableMap<String, Object> metadataRecord,
                                                @Nonnull @JsonProperty(METADATA_SPECIFICATION) MetadataSpecification metadataSpecification,
                                                @Nonnull @JsonProperty(EVALUATION_REPORT) EvaluationReport evaluationReport) {
    return new AutoValue_EvaluateMetadataResponse(metadataRecordId, metadataRecordName, metadataRecord,
        metadataSpecification, evaluationReport);
  }

  @Nonnull
  @JsonProperty(METADATA_RECORD_ID)
  public abstract String getMetadataRecordId();

  @Nonnull
  @JsonProperty(METADATA_RECORD_NAME)
  public abstract String getMetadataRecordName();

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
