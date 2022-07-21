package org.metadatacenter.fairware.core.domain;

import com.google.auto.value.AutoOneOf;
import com.google.common.collect.ImmutableList;
import org.metadatacenter.fairware.shared.OntologyTerm;

import javax.annotation.Nonnull;

@AutoOneOf(ValueConstraint.Kind.class)
public abstract class ValueConstraint {

  public enum Kind {
    ONTOLOGY,
    VALUE_SET,
    BRANCH,
    CLASSES
  }

  @Nonnull
  public static ValueConstraint ofOntology(@Nonnull String name) {
    return AutoOneOf_ValueConstraint.ontology(name);
  }

  @Nonnull
  public static ValueConstraint ofValueSet(@Nonnull String name) {
    return AutoOneOf_ValueConstraint.valueSet(name);
  }

  @Nonnull
  public static ValueConstraint ofBranch(@Nonnull String name) {
    return AutoOneOf_ValueConstraint.branch(name);
  }

  @Nonnull
  public static ValueConstraint ofClasses(@Nonnull ImmutableList<OntologyTerm> classes) {
    return AutoOneOf_ValueConstraint.classes(classes);
  }

  @Nonnull
  public abstract Kind getKind();

  @Nonnull
  public abstract String ontology();

  @Nonnull
  public abstract String valueSet();

  @Nonnull
  public abstract String branch();

  @Nonnull
  public abstract ImmutableList<OntologyTerm> classes();
}
