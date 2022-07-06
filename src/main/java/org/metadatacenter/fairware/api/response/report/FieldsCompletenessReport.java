package org.metadatacenter.fairware.api.response.report;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@AutoValue
public abstract class FieldsCompletenessReport {

  @Nonnull
  public static FieldsCompletenessReport create(int completeFieldsCount,
                                                int fieldsWithMissingRequiredValuesCount,
                                                int fieldsWithMissingOptionalValuesCount,
                                                @Nonnull ImmutableList<FieldReport> fieldReports) {
    return new AutoValue_FieldsCompletenessReport(completeFieldsCount, fieldsWithMissingRequiredValuesCount,
        fieldsWithMissingOptionalValuesCount, fieldReports);
  }

  public abstract int getCompleteFieldsCount();

  public abstract int getFieldsWithMissingRequiredValuesCount();

  public abstract int getFieldsWithMissingOptionalValuesCount();

  @Nonnull
  public abstract ImmutableList<FieldReport> getItems();
}
