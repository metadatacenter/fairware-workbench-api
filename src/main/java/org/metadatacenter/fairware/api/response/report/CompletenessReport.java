package org.metadatacenter.fairware.api.response.report;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class CompletenessReport {

  @Nonnull
  public static CompletenessReport create(@Nonnull RecordsCompletenessReport recordsCompletenessReport,
                                          @Nonnull FieldsCompletenessReport fieldsCompletenessReport) {
    return new AutoValue_CompletenessReport(recordsCompletenessReport, fieldsCompletenessReport);
  }

  @Nonnull
  public abstract RecordsCompletenessReport getRecordsReport();

  @Nonnull
  public abstract FieldsCompletenessReport getFieldsReport();
}
