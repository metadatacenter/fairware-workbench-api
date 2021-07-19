package org.metadatacenter.fairware.api.cedar.resourceserver.recommend.request;

import com.fasterxml.jackson.databind.JsonNode;

public class CedarRecommendTemplatesRequest {

  private JsonNode metadataRecord;

  public CedarRecommendTemplatesRequest() { }

  public CedarRecommendTemplatesRequest(JsonNode metadataRecord) {
    this.metadataRecord = metadataRecord;
  }

  public JsonNode getMetadataRecord() {
    return metadataRecord;
  }

}
