package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Optional;

@AutoValue
public abstract class EvaluateMetadataResponse {

  @Nonnull
  public static EvaluateMetadataResponse create(@Nonnull Optional<String> metadataRecordId,
                                                @Nonnull Optional<String> metadataRecordName,
                                                @Nonnull String templateId,
                                                @Nonnull String templateName,
                                                @Nonnull ImmutableMap<String, Object> metadataRecord,
                                                @Nonnull ImmutableList<String> metadataFieldPaths,
                                                int totalIssuesCount,
                                                int warningsCount,
                                                int errorsCount,
                                                @Nonnull ImmutableList<EvaluationReportItem> evaluationReportItems,
                                                @Nonnull LocalDateTime generatedOn) {
    return new AutoValue_EvaluateMetadataResponse(metadataRecordId, metadataRecordName, templateId,
        templateName, metadataRecord, metadataFieldPaths, totalIssuesCount, warningsCount, errorsCount,
        evaluationReportItems, generatedOn);
  }

  @Nonnull
  public abstract Optional<String> getMetadataRecordId();

  @Nonnull
  public abstract Optional<String> getMetadataRecordName();

  @Nonnull
  public abstract String getTemplateId();

  @Nonnull
  public abstract String getTemplateName();

  @Nonnull
  public abstract ImmutableMap<String, Object> getMetadataRecord();

  @Nonnull
  public abstract ImmutableList<String> getMetadataFieldPaths();

  public abstract int getTotalIssuesCount();

  public abstract int getWarningsCount();

  public abstract int getErrorsCount();

  @Nonnull
  public abstract ImmutableList<EvaluationReportItem> getEvaluationReportItems();

  @Nonnull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // TODO: specify timezone
  public abstract LocalDateTime getGeneratedOn();
}
