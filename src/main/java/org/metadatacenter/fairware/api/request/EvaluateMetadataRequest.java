package org.metadatacenter.fairware.api.request;

import org.metadatacenter.fairware.api.shared.FieldAlignment;

import java.util.List;
import java.util.Map;

public class EvaluateMetadataRequest {

  private String templateId;
  private Map<String, Object> metadataRecord;
  private String metadataRecordId;
  private List<FieldAlignment> fieldAlignments;

  public EvaluateMetadataRequest() { }

  public String getTemplateId() {
    return templateId;
  }

  public Map<String, Object> getMetadataRecord() {
    return metadataRecord;
  }

  public String getMetadataRecordId() {
    return metadataRecordId;
  }

  public List<FieldAlignment> getFieldAlignments() {
    return fieldAlignments;
  }
}
