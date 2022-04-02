package org.metadatacenter.fairware.api.response.evaluationReport;

import java.util.List;

public class RecordsCompletenessReport {

  private int recordsCount;
  private int completeRecordsCount;
  private int recordsWithMissingRequiredValuesCount;
  private int recordsWithMissingOptionalValuesCount;
  private List<RecordReport> items;

  public RecordsCompletenessReport() {
  }

  public int getRecordsCount() {
    return recordsCount;
  }

  public void setRecordsCount(int recordsCount) {
    this.recordsCount = recordsCount;
  }

  public int getCompleteRecordsCount() {
    return completeRecordsCount;
  }

  public void setCompleteRecordsCount(int completeRecordsCount) {
    this.completeRecordsCount = completeRecordsCount;
  }

  public int getRecordsWithMissingRequiredValuesCount() {
    return recordsWithMissingRequiredValuesCount;
  }

  public void setRecordsWithMissingRequiredValuesCount(int recordsWithMissingRequiredValuesCount) {
    this.recordsWithMissingRequiredValuesCount = recordsWithMissingRequiredValuesCount;
  }

  public int getRecordsWithMissingOptionalValuesCount() {
    return recordsWithMissingOptionalValuesCount;
  }

  public void setRecordsWithMissingOptionalValuesCount(int recordsWithMissingOptionalValuesCount) {
    this.recordsWithMissingOptionalValuesCount = recordsWithMissingOptionalValuesCount;
  }

  public List<RecordReport> getItems() {
    return items;
  }

  public void setItems(List<RecordReport> items) {
    this.items = items;
  }
}
