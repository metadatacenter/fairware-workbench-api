package org.metadatacenter.fairware.api.response;

import org.metadatacenter.fairware.api.shared.FieldAlignment;

import java.util.List;

public class AlignMetadataResponse {

  public AlignMetadataResponse(int totalCount) {
    this.totalCount = totalCount;
  }

  public AlignMetadataResponse(int totalCount, List<FieldAlignment> fieldAlignments) {
    this.totalCount = totalCount;
    this.fieldAlignments = fieldAlignments;
  }

  private int totalCount;
  private List<FieldAlignment> fieldAlignments;

}
