package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluationReportRequest {

  private static final String METADATA_LIST = "metadataList";

  @Nonnull
  @JsonCreator
  public static EvaluationReportRequest create(@Nonnull @JsonProperty(METADATA_LIST) ImmutableList<EvaluateMetadataRequest> metadataList) {
    return new AutoValue_EvaluationReportRequest(metadataList);
  }

  @Nonnull
  @JsonProperty(METADATA_LIST)
  public abstract ImmutableList<EvaluateMetadataRequest> getEvaluateMetadataRequests();
}
