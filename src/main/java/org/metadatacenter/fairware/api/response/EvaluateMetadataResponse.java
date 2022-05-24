package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.auto.value.AutoValue;
import org.checkerframework.checker.units.qual.N;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoValue
public abstract class EvaluateMetadataResponse {

  public static EvaluateMetadataResponse create(@Nonnull String metadataRecordId,
                                                @Nonnull Optional<String> metadataRecordName,
                                                @Nonnull String templateId,
                                                @Nonnull String templateName,
                                                @Nonnull Map<String, Object> metadataRecord,
                                                @Nonnull List<String> metadataFieldPaths,
                                                int totalIssuesCount,
                                                int warningsCount,
                                                int errorsCount,
                                                @Nonnull List<EvaluationReportItem> evaluationReportItems,
                                                @Nonnull LocalDateTime generatedOn) {
    return new AutoValue_EvaluateMetadataResponse(metadataRecordId, metadataRecordName, templateId,
        templateName, metadataRecord, metadataFieldPaths, totalIssuesCount, warningsCount, errorsCount,
        evaluationReportItems, generatedOn);
  }

  @Nonnull
  public abstract String getMetadataRecordId();

  @Nonnull
  public abstract Optional<String> getMetadataRecordName();

  @Nonnull
  public abstract String getTemplateId();

  @Nonnull
  public abstract String getTemplateName();

  @Nonnull
  public abstract Map<String, Object> getMetadataRecord();

  @Nonnull
  public abstract List<String> getMetadataFieldPaths();

  public abstract int getTotalIssuesCount();

  public abstract int getWarningsCount();

  public abstract int getErrorsCount();

  @Nonnull
  public abstract List<EvaluationReportItem> getEvaluationReportItems();

  @Nonnull
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") // TODO: specify timezone
  public abstract LocalDateTime getGeneratedOn();
}
