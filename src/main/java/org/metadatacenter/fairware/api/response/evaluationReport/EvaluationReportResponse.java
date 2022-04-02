package org.metadatacenter.fairware.api.response.evaluationReport;

public class EvaluationReportResponse {

  CompletenessReport completenessReport;

  public EvaluationReportResponse() {}

  public CompletenessReport getCompletenessReport() {
    return completenessReport;
  }

  public void setCompletenessReport(CompletenessReport completenessReport) {
    this.completenessReport = completenessReport;
  }
}
