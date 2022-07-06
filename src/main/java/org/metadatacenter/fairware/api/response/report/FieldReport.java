package org.metadatacenter.fairware.api.response.report;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class FieldReport {

  @Nonnull
  public static FieldReport create(@Nonnull String metadataFieldPath,
                                   @Nonnull String templateId,
                                   @Nonnull String templateName,
                                   int fieldsCount,
                                   int completeCount,
                                   int missingRequiredValuesCount,
                                   int missingOptionalValuesCount) {
    return new AutoValue_FieldReport(metadataFieldPath, templateId, templateName, fieldsCount,
        completeCount, missingRequiredValuesCount, missingOptionalValuesCount);
  }

  @Nonnull
  public abstract String getMetadataFieldPath();

  @Nonnull
  public abstract String getTemplateId();

  @Nonnull
  public abstract String getTemplateName();

  public abstract int getFieldsCount();

  public abstract int getCompleteCount();

  public abstract int getMissingRequiredValuesCount();

  public abstract int getMissingOptionalValuesCount();

  public FieldReport incrementFieldsCount() {
    return create(getMetadataFieldPath(), getTemplateId(), getTemplateName(), getFieldsCount() + 1,
        getCompleteCount(), getMissingRequiredValuesCount(), getMissingOptionalValuesCount());
  }

  public FieldReport incrementCompleteCount() {
    return create(getMetadataFieldPath(), getTemplateId(), getTemplateName(), getFieldsCount(),
        getCompleteCount() + 1, getMissingRequiredValuesCount(), getMissingOptionalValuesCount());
  }

  public FieldReport decrementCompleteCount() {
    return create(getMetadataFieldPath(), getTemplateId(), getTemplateName(), getFieldsCount(),
        getCompleteCount() - 1, getMissingRequiredValuesCount(), getMissingOptionalValuesCount());
  }

  public FieldReport incrementMissingRequiredValuesCount() {
    return create(getMetadataFieldPath(), getTemplateId(), getTemplateName(), getFieldsCount(),
        getCompleteCount(), getMissingRequiredValuesCount() + 1, getMissingOptionalValuesCount());
  }

  public FieldReport incrementMissingOptionalValuesCount() {
    return create(getMetadataFieldPath(), getTemplateId(), getTemplateName(), getFieldsCount(),
        getCompleteCount(), getMissingRequiredValuesCount(), getMissingOptionalValuesCount() + 1);
  }
}
