package org.metadatacenter.fairware.api.request;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class RecommendTemplatesRequest {

  @Valid
  @NotNull
  private Map<String, Object> metadataRecord;

  public RecommendTemplatesRequest() { }

  public RecommendTemplatesRequest(@Valid @NotNull Map<String, Object> metadataRecord) {
    this.metadataRecord = metadataRecord;
  }

  public Map<String, Object> getMetadataRecord() {
    return metadataRecord;
  }

  public void setMetadataRecord(Map<String, Object> metadataRecord) {
    this.metadataRecord = metadataRecord;
  }

}
