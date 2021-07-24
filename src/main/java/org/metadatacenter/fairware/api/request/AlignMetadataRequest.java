package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class AlignMetadataRequest {

  private String templateId;
  private Map<String, Object> metadataRecord;

  public AlignMetadataRequest() { }

  public String getTemplateId() {
    return templateId;
  }

  public Map<String, Object> getMetadataRecord() {
    return metadataRecord;
  }
}
