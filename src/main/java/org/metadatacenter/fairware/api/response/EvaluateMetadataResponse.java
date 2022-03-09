package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EvaluateMetadataResponse {

  private String metadataRecordId;
  private String templateId;
  private Map<String, Object> metadataRecord;
  private List<EvaluationReportItem> items;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") // TODO: specify timezone
  private LocalDateTime generatedOn;

  public EvaluateMetadataResponse() {}

  public EvaluateMetadataResponse(String metadataRecordId, String templateId, LocalDateTime generatedOn, Map<String, Object> metadataRecord,
                                  List<EvaluationReportItem> items) {
    this.metadataRecordId = metadataRecordId;
    this.templateId = templateId;
    this.generatedOn = generatedOn;
    this.metadataRecord = metadataRecord;
    this.items = items;
  }

  public String getMetadataRecordId() { return metadataRecordId; }

  public String getTemplateId() {
    return templateId;
  }

  public LocalDateTime getGeneratedOn() {
    return generatedOn;
  }

  public Map<String, Object> getMetadataRecord() {
    return metadataRecord;
  }

  public List<EvaluationReportItem> getItems() {
    return items;
  }
}
