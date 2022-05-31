package org.metadatacenter.fairware.api.response.action;

import com.google.auto.value.AutoOneOf;

import javax.annotation.Nonnull;
import java.util.List;

@AutoOneOf(RepairAction.Kind.class)
public abstract class RepairAction {

  public static RepairAction ofEnterMissingValue() {
    return AutoOneOf_RepairAction.enterMissingValue();
  }

  public static RepairAction ofEnterStringTypeValue(@Nonnull String value) {
    return AutoOneOf_RepairAction.enterStringTypeValue(value);
  }

  public static RepairAction ofReplaceFieldNameWithOntologyTerm(@Nonnull List<SuggestedOntologyTerm> suggestedOntologyTerms) {
    return AutoOneOf_RepairAction.replaceFieldNameWithOntologyTerm(suggestedOntologyTerms);
  }

  @Nonnull
  public abstract Kind getKind();

  @Nonnull
  public abstract void enterMissingValue();

  @Nonnull
  public abstract String enterStringTypeValue();

  @Nonnull
  public abstract List<SuggestedOntologyTerm> replaceFieldNameWithOntologyTerm();

  public enum Kind {
    ENTER_MISSING_VALUE,
    ENTER_STRING_TYPE_VALUE,
    REPLACE_FIELD_NAME_WITH_ONTOLOGY_TERM
  }
}
