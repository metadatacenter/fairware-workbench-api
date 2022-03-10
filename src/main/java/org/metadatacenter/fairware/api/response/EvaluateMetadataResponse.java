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
  private int totalIssuesCount;
  private int warningsCount;
  private int errorsCount;
  private List<EvaluationReportItem> items;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") // TODO: specify timezone
  private LocalDateTime generatedOn;

  public EvaluateMetadataResponse() {}

  public EvaluateMetadataResponse(String metadataRecordId, String templateId, Map<String, Object> metadataRecord,
                                  int totalIssuesCount, int warningsCount, int errorsCount,
                                  List<EvaluationReportItem> items, LocalDateTime generatedOn) {
    this.metadataRecordId = metadataRecordId;
    this.templateId = templateId;
    this.metadataRecord = metadataRecord;
    this.totalIssuesCount = totalIssuesCount;
    this.warningsCount = warningsCount;
    this.errorsCount = errorsCount;
    this.items = items;
    this.generatedOn = generatedOn;
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

  public int getTotalIssuesCount() {
    return totalIssuesCount;
  }

  public int getWarningsCount() {
    return warningsCount;
  }

  public int getErrorsCount() {
    return errorsCount;
  }

  public List<EvaluationReportItem> getItems() {
    return items;
  }
}
