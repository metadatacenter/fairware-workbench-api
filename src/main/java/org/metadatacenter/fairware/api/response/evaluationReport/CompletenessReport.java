package org.metadatacenter.fairware.api.response.evaluationReport;

public class CompletenessReport {

  private RecordsCompletenessReport recordsReport;
  private FieldsCompletenessReport fieldsReport;

  public CompletenessReport() {
  }

  public RecordsCompletenessReport getRecordsReport() {
    return recordsReport;
  }

  public void setRecordsReport(RecordsCompletenessReport recordsReport) {
    this.recordsReport = recordsReport;
  }

  public FieldsCompletenessReport getFieldsReport() {
    return fieldsReport;
  }

  public void setFieldsReport(FieldsCompletenessReport fieldsReport) {
    this.fieldsReport = fieldsReport;
  }

}
