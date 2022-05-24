package org.metadatacenter.fairware.api.response.evaluationReport;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
public abstract class RecordReport {

  @Nonnull
  public static RecordReport create(@Nonnull Optional<String> metadataRecordId,
                                    @Nonnull Optional<String> metadataRecordName,
                                    @Nonnull String templateId,
                                    @Nonnull String templateName,
                                    int fieldsCount,
                                    int missingRequiredValuesCount,
                                    int missingOptionalValuesCount) {
    return new AutoValue_RecordReport(metadataRecordId, metadataRecordName, templateId, templateName,
        fieldsCount, missingRequiredValuesCount, missingOptionalValuesCount);
  }

  @Nonnull
  public abstract Optional<String> getMetadataRecordId();

  @Nonnull
  public abstract Optional<String> getMetadataRecordName();

  @Nonnull
  public abstract String getTemplateId();

  @Nonnull
  public abstract String getTemplateName();

  public abstract int getFieldsCount();

  public int getCompleteCount() {
    return getFieldsCount() - getMissingRequiredValuesCount() - getMissingOptionalValuesCount();
  }

  public abstract int getMissingRequiredValuesCount();

  public abstract int getMissingOptionalValuesCount();
}
