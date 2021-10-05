package org.metadatacenter.fairware.core.domain;

import java.util.List;
import java.util.Objects;

public class MetadataFieldInfo {

  private String name;
  private List<String> path;
  private String value; // TODO: extend implementation to deal with multiple cardinality

  public MetadataFieldInfo() {}

  public MetadataFieldInfo(String name, List<String> path) {
    this.name = name;
    this.path = path;
    this.value =  null;
  }

  public MetadataFieldInfo(String name, List<String> path, String value) {
    this.name = name;
    this.path = path;
    this.value =  value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPath() {
    return path;
  }

  public String getValue() { return value; }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetadataFieldInfo that = (MetadataFieldInfo) o;
    return name.equals(that.name) && path.equals(that.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, path);
  }
}

