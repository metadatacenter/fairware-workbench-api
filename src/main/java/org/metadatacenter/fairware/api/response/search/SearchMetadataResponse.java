package org.metadatacenter.fairware.api.response.search;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

@AutoValue
public abstract class SearchMetadataResponse {

  public static SearchMetadataResponse create(int totalCount,
                                              @Nonnull ImmutableList<SearchMetadataItem> items) {
    return new AutoValue_SearchMetadataResponse(totalCount, items);
  }

  public abstract int getTotalCount();

  @Nonnull
  public abstract ImmutableList<SearchMetadataItem> getItems();
}
