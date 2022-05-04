package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class MetadataFieldInfo {

  @Nonnull
  public static MetadataFieldInfo create(@Nonnull String name,
                           @Nullable String prefLabel,
                           @Nonnull ImmutableList<String> path,
                           @Nullable Object value,
                           @Nullable String valueUri) {
    return new AutoValue_MetadataFieldInfo(name, prefLabel, path, value, valueUri);
  }

  @Nonnull
  public abstract String getName();

  @Nullable
  public abstract String getPrefLabel();

  @Nonnull
  public abstract ImmutableList<String> getPath();

  @Nullable
  public abstract Object getValue();

  @Nullable
  public abstract String getValueUri();
}
