package org.metadatacenter.fairware.api.request;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

public class AlignMetadataRequest {

  @NotEmpty // ensure that name isn't null or blank
  private String templateId;

  @NotEmpty // ensure that name isn't null or blank
  private Map<String, Object> metadataRecord;

  public AlignMetadataRequest() { }

  public String getTemplateId() {
    return templateId;
  }

  public Map<String, Object> getMetadataRecord() {
    return metadataRecord;
  }
}
