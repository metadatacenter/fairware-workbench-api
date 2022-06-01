package org.metadatacenter.fairware.api.response.action;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class SuggestedOntologyTerm {

  public static SuggestedOntologyTerm create(@Nonnull String uri,
                                             @Nonnull String label,
                                             @Nonnull String ontologyAcronym) {
    return new AutoValue_SuggestedOntologyTerm(uri, label, ontologyAcronym);
  }

  @Nonnull
  public abstract String getUri();

  @Nonnull
  public abstract String getLabel();

  @Nonnull
  public abstract String getOntologyAcronym();
}
