package org.metadatacenter.fairware.api.response.search;

import java.util.List;

public class SearchMetadataResponse {

  private int totalCount;
  private List<SearchMetadataItem> items;

  public SearchMetadataResponse() {
  }

  public SearchMetadataResponse(int totalCount, List<SearchMetadataItem> items) {
    this.totalCount = totalCount;
    this.items = items;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public List<SearchMetadataItem> getItems() {
    return items;
  }
}
