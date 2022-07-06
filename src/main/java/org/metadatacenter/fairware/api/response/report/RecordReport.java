package org.metadatacenter.fairware.api.response.report;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RecordReport {

  @Nonnull
  public static RecordReport create(@Nonnull String metadataRecordId,
                                    @Nonnull String metadataRecordName,
                                    @Nonnull String templateId,
                                    @Nonnull String templateName,
                                    int fieldsCount,
                                    int missingRequiredValuesCount,
                                    int missingOptionalValuesCount) {
    return new AutoValue_RecordReport(metadataRecordId, metadataRecordName, templateId, templateName,
        fieldsCount, missingRequiredValuesCount, missingOptionalValuesCount);
  }

  @Nonnull
  public abstract String getMetadataRecordId();

  @Nonnull
  public abstract String getMetadataRecordName();

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
