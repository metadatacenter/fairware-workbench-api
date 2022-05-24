package org.metadatacenter.fairware.api.response;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.api.shared.FieldAlignment;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue
public abstract class AlignMetadataResponse {

  @Nonnull
  public static AlignMetadataResponse create(@Nonnull ImmutableList<FieldAlignment> fieldAlignments) {
    return new AutoValue_AlignMetadataResponse(fieldAlignments);
  }

  @Nonnull
  public abstract ImmutableList<FieldAlignment> getFieldAlignments();

  public int getTotalCount() {
    return getFieldAlignments().size();
  }
}
