package org.metadatacenter.fairware.api.response.evaluationReport;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluationReportResponse {

  CompletenessReport completenessReport;

  @Nonnull
  public static EvaluationReportResponse create(@Nonnull CompletenessReport completenessReport) {
    return new AutoValue_EvaluationReportResponse(completenessReport);
  }

  @Nonnull
  public abstract CompletenessReport getCompletenessReport();
}
