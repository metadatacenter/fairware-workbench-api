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
  private static final String TEMPLATE_ID = "templateId";
  private static final String TEMPLATE_NAME = "templateName";
  private static final String TEMPLATE_FIELD_NAMES = "templateFieldNames";
  private static final String TOTAL_ISSUES_COUNT = "totalIssuesCount";
  private static final String WARNINGS_COUNT = "warningsCount";
  private static final String ERRORS_COUNT = "errorsCount";
  private static final String EVALUATION_REPORT_ITEMS = "evaluationReportItems";
  private static final String GENERATED_ON = "generatedOn";

  @Nonnull
  @JsonCreator
  public static EvaluateMetadataResponse create(@Nonnull @JsonProperty(METADATA_RECORD_ID) Optional<String> metadataRecordId,
                                                @Nonnull @JsonProperty(METADATA_RECORD_NAME) Optional<String> metadataRecordName,
                                                @Nonnull @JsonProperty(METADATA_RECORD) ImmutableMap<String, Object> metadataRecord,
                                                @Nonnull @JsonProperty(TEMPLATE_ID) String templateId,
                                                @Nonnull @JsonProperty(TEMPLATE_NAME) String templateName,
                                                @Nonnull @JsonProperty(TEMPLATE_FIELD_NAMES) ImmutableList<String> templateFieldNames,
                                                @JsonProperty(TOTAL_ISSUES_COUNT)int totalIssuesCount,
                                                @JsonProperty(WARNINGS_COUNT)int warningsCount,
                                                @JsonProperty(ERRORS_COUNT) int errorsCount,
                                                @Nonnull @JsonProperty(EVALUATION_REPORT_ITEMS) ImmutableList<EvaluationReportItem> evaluationReportItems,
                                                @Nonnull @JsonProperty(GENERATED_ON) LocalDateTime generatedOn) {
    return new AutoValue_EvaluateMetadataResponse(metadataRecordId, metadataRecordName, metadataRecord,
        templateId, templateName, templateFieldNames, totalIssuesCount, warningsCount, errorsCount,
        evaluationReportItems, generatedOn);
  }

  @Nonnull
  @JsonProperty(METADATA_RECORD_ID)
  public abstract Optional<String> getMetadataRecordId();

  @Nonnull
  @JsonProperty(METADATA_RECORD_NAME)
  public abstract Optional<String> getMetadataRecordName();

  @Nonnull
  @JsonProperty(METADATA_RECORD)
  public abstract ImmutableMap<String, Object> getMetadataRecord();

  @Nonnull
  @JsonProperty(TEMPLATE_ID)
  public abstract String getTemplateId();

  @Nonnull
  @JsonProperty(TEMPLATE_NAME)
  public abstract String getTemplateName();

  @Nonnull
  @JsonProperty(TEMPLATE_FIELD_NAMES)
  public abstract ImmutableList<String> getTemplateFieldNames();

  @JsonProperty(TOTAL_ISSUES_COUNT)
  public abstract int getTotalIssuesCount();

  @JsonProperty(WARNINGS_COUNT)
  public abstract int getWarningsCount();

  @JsonProperty(ERRORS_COUNT)
  public abstract int getErrorsCount();

  @Nonnull
  @JsonProperty(EVALUATION_REPORT_ITEMS)
  public abstract ImmutableList<EvaluationReportItem> getEvaluationReportItems();

  @Nonnull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // TODO: specify timezone
  @JsonProperty(GENERATED_ON)
  public abstract LocalDateTime getGeneratedOn();
}
