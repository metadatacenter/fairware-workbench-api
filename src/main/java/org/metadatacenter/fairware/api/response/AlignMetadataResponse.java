package org.metadatacenter.fairware.api.response;

import org.metadatacenter.fairware.api.shared.FieldAlignment;

import java.util.List;

public class AlignMetadataResponse {

  private int totalCount;
  private List<FieldAlignment> fieldAlignments;

  public AlignMetadataResponse() {
  }

  public AlignMetadataResponse(int totalCount, List<FieldAlignment> fieldAlignments) {
    this.totalCount = totalCount;
    this.fieldAlignments = fieldAlignments;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public List<FieldAlignment> getFieldAlignments() {
    return fieldAlignments;
  }
}
