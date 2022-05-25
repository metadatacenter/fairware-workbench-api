package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
public abstract class MetadataFieldInfo {

  @Nonnull
  public static MetadataFieldInfo create(@Nonnull String name,
                                         @Nonnull Optional<String> prefLabel,
                                         @Nonnull ImmutableList<String> path,
                                         @Nonnull Optional<Object> value,
                                         @Nonnull Optional<String> valueUri) {
    return new AutoValue_MetadataFieldInfo(name, prefLabel, path, value, valueUri);
  }

  @Nonnull
  public abstract String getName();

  @Nonnull
  public abstract Optional<String> getPrefLabel();

  @Nonnull
  public abstract ImmutableList<String> getPath();

  @Nonnull
  public abstract Optional<Object> getValue();

  @Nonnull
  public abstract Optional<String> getValueUri();
}
