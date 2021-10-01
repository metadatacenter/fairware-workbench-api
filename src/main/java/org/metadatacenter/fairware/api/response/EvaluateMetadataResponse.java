package org.metadatacenter.fairware.api.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EvaluateMetadataResponse {

  private String templateId;
  private Date createdOn;
  private Map<String, Object> metadataRecord;
  private List<EvaluationReportItem> items;

  public EvaluateMetadataResponse() {}

  public EvaluateMetadataResponse(String templateId, Date createdOn, Map<String, Object> metadataRecord,
                                  List<EvaluationReportItem> items) {
    this.templateId = templateId;
    this.createdOn = createdOn;
    this.metadataRecord = metadataRecord;
    this.items = items;
  }

  public String getTemplateId() {
    return templateId;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public Map<String, Object> getMetadataRecord() {
    return metadataRecord;
  }

  public List<EvaluationReportItem> getItems() {
    return items;
  }
}
