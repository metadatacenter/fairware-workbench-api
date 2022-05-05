package org.metadatacenter.fairware.api.response.search;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@AutoValue
public abstract class SearchMetadataItem {

  public static SearchMetadataItem create(@Nonnull String uri,
                                          @Nonnull String source,
                                          @Nonnull String title,
                                          @Nonnull String schemaId,
                                          @Nonnull String schemaName,
                                          @Nonnull ImmutableMap<String, Object> metadata) {
    return new AutoValue_SearchMetadataItem(uri, source, title, schemaId, schemaName, metadata);
  }

  @Nonnull
  public abstract String getUri();

  @Nonnull
  public abstract String getSource();

  @Nonnull
  public abstract String getTitle();

  @Nonnull
  public abstract String getSchemaId();

  @Nonnull
  public abstract String getSchemaName();

  @Nonnull
  public abstract Map<String, Object> getMetadata();
}
