package org.metadatacenter.fairware.api.request;

import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class EvaluationReportRequest {

  @NotEmpty
  List<EvaluateMetadataRequest> evaluateMetadataRequests;

  public EvaluationReportRequest() { }

  public List<EvaluateMetadataRequest> getEvaluateMetadataRequests() {
    return evaluateMetadataRequests;
  }
}
