package org.metadatacenter.fairware.api.response.search;

import java.util.Map;

public class SearchMetadataItem {

  private String uri;
  private String source;
  private String title;
  private String schemaId;
  private String schemaName;
  private Map<String, Object> metadata;

  public SearchMetadataItem() {
  }

  public SearchMetadataItem(String uri, String source, String title, String schemaId, String schemaName, Map<String,
      Object> metadata) {
    this.uri = uri;
    this.source = source;
    this.title = title;
    this.schemaId = schemaId;
    this.schemaName = schemaName;
    this.metadata = metadata;
  }

  public String getUri() {
    return uri;
  }

  public String getSource() {
    return source;
  }

  public String getTitle() {
    return title;
  }

  public String getSchemaId() {
    return schemaId;
  }

  public String getSchemaName() {
    return schemaName;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }
}
