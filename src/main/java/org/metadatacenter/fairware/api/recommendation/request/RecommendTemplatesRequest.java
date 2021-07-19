package org.metadatacenter.fairware.api.recommendation.request;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RecommendTemplatesRequest {

  @Valid
  @NotNull
  private JsonNode metadataRecord;

  public RecommendTemplatesRequest() { }

  public JsonNode getMetadataRecord() {
    return metadataRecord;
  }

  public void setMetadataRecord(JsonNode metadataRecord) {
    this.metadataRecord = metadataRecord;
  }

}
