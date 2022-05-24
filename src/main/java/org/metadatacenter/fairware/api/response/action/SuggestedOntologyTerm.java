package org.metadatacenter.fairware.api.response.action;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class SuggestedOntologyTerm {

  public static SuggestedOntologyTerm create(@Nonnull String uri,
                                             @Nonnull String label,
                                             @Nonnull String ontologyAcronym,
                                             @Nonnull String definition) {
    return new AutoValue_SuggestedOntologyTerm(uri, label, ontologyAcronym, definition);
  }

  @Nonnull
  public abstract String getUri();

  @Nonnull
  public abstract String getLabel();

  @Nonnull
  public abstract String getOntologyAcronym();

  @Nonnull
  public abstract String getDefinition();
}
