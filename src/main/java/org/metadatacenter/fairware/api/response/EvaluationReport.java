package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.api.response.issue.IssueLevel;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@AutoValue
public abstract class EvaluationReport {

  private static final String TOTAL_ISSUES_COUNT = "totalIssuesCount";
  private static final String WARNINGS_COUNT = "warningsCount";
  private static final String ERRORS_COUNT = "errorsCount";
  private static final String EVALUATION_REPORT_ITEMS = "evaluationReportItems";
  private static final String GENERATED_ON = "generatedOn";

  @Nonnull
  @JsonCreator
  public static EvaluationReport create(@Nonnull @JsonProperty(EVALUATION_REPORT_ITEMS) ImmutableList<EvaluationReportItem> evaluationReportItems,
                                        @Nonnull @JsonProperty(GENERATED_ON) LocalDateTime generatedOn) {
    return new AutoValue_EvaluationReport(evaluationReportItems, generatedOn);
  }

  @JsonProperty(TOTAL_ISSUES_COUNT)
  public int getTotalIssuesCount() {
    return getAllIssueReports().size();
  }

  @JsonProperty(WARNINGS_COUNT)
  public int getWarningsCount() {
    return getWarningReports().size();
  }

  @Nonnull
  @JsonIgnore
  public ImmutableList<EvaluationReportItem> getWarningReports() {
    return getAllIssueReports().stream()
        .filter(reportItem -> reportItem.getMetadataIssue().getIssueLevel().equals(IssueLevel.WARNING))
        .collect(ImmutableList.toImmutableList());
  }

  @JsonProperty(ERRORS_COUNT)
  public int getErrorsCount() {
    return getErrorReports().size();
  }

  @Nonnull
  @JsonIgnore
  private ImmutableList<Object> getErrorReports() {
    return getAllIssueReports().stream()
        .filter(reportItem -> reportItem.getMetadataIssue().getIssueLevel().equals(IssueLevel.ERROR))
        .collect(ImmutableList.toImmutableList());
  }

  @Nonnull
  @JsonProperty(EVALUATION_REPORT_ITEMS)
  public abstract ImmutableList<EvaluationReportItem> getAllIssueReports();

  @Nonnull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // TODO: specify timezone
  @JsonProperty(GENERATED_ON)
  public abstract LocalDateTime getGeneratedOn();
}
