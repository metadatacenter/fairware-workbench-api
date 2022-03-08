package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import java.util.List;
import java.util.Objects;

public class MetadataFieldInfo {

  private String name;
  private String prefLabel;
  private List<String> path;
  private Object value;
  private String valueUri;

  public MetadataFieldInfo() {}

  public MetadataFieldInfo(String name, String prefLabel, List<String> path, Object value, String valueUri) {
    this.name = name;
    this.prefLabel = prefLabel;
    this.path = path;
    this.value = value;
    this.valueUri = valueUri;
  }

  public String getName() {
    return name;
  }

  public String getPrefLabel() {
    return prefLabel;
  }

  public List<String> getPath() {
    return path;
  }

  public Object getValue() {
    return value;
  }

  public String getValueUri() {
    return valueUri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetadataFieldInfo that = (MetadataFieldInfo) o;
    return Objects.equals(name, that.name) && Objects.equals(prefLabel, that.prefLabel) && Objects.equals(path,
        that.path) && Objects.equals(value, that.value) && Objects.equals(valueUri, that.valueUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, prefLabel, path, value, valueUri);
  }
}
