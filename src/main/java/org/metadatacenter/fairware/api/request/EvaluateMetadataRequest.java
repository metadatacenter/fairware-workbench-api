package org.metadatacenter.fairware.api.request;

import org.metadatacenter.fairware.api.shared.FieldAlignment;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

public class EvaluateMetadataRequest {

  @NotEmpty // ensure that name isn't null or blank
  private String templateId;

  @NotEmpty
  private Map<String, Object> metadataRecord;

  @NotEmpty
  private List<FieldAlignment> fieldAlignments;

  public EvaluateMetadataRequest() { }

  public String getTemplateId() {
    return templateId;
  }

  public Map<String, Object> getMetadataRecord() {
    return metadataRecord;
  }

  public List<FieldAlignment> getFieldAlignments() {
    return fieldAlignments;
  }
}
