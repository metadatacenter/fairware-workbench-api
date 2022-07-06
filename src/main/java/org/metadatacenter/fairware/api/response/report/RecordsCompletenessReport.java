package org.metadatacenter.fairware.api.response.report;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@AutoValue
public abstract class RecordsCompletenessReport {

  @Nonnull
  public static RecordsCompletenessReport create(int recordsCount,
                                                 int completeRecordsCount,
                                                 int recordsWithMissingRequiredValuesCount,
                                                 int recordsWithMissingOptionalValuesCount,
                                                 @Nonnull ImmutableList<RecordReport> recordReports) {
    return new AutoValue_RecordsCompletenessReport(recordsCount, completeRecordsCount,
        recordsWithMissingRequiredValuesCount, recordsWithMissingOptionalValuesCount,
        recordReports);
  }

  public abstract int getRecordsCount();

  public abstract int getCompleteRecordsCount();

  public abstract int getRecordsWithMissingRequiredValuesCount();

  public abstract int getRecordsWithMissingOptionalValuesCount();

  @Nonnull
  public abstract ImmutableList<RecordReport> getRecordReports();
}
