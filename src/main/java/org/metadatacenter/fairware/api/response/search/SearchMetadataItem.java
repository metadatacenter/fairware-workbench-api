package org.metadatacenter.fairware.api.response.search;

import java.util.Map;

public class SearchMetadataItem {

  private String searchDoi;
  private String foundDoi;
  private String source;
  private String title;
  private String schemaVersion;
  private Map<String, Object> metadata;

  public SearchMetadataItem() {
  }

  public SearchMetadataItem(String searchDoi, String foundDoi, String source, String title, String schemaVersion,
                            Map<String, Object> metadata) {
    this.searchDoi = searchDoi;
    this.foundDoi = foundDoi;
    this.source = source;
    this.title = title;
    this.schemaVersion = schemaVersion;
    this.metadata = metadata;
  }

  public String getSearchDoi() {
    return searchDoi;
  }

  public String getFoundDoi() {
    return foundDoi;
  }

  public String getSource() {
    return source;
  }

  public String getTitle() {
    return title;
  }

  public String getSchemaVersion() {
    return schemaVersion;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }
}
