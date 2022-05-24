package org.metadatacenter.fairware.api.response.action;

import com.google.auto.value.AutoOneOf;

import javax.annotation.Nonnull;
import java.util.List;

@AutoOneOf(RepairAction.Kind.class)
public abstract class RepairAction {

  public static RepairAction ofEnterMissingValue() {
    return AutoOneOf_RepairAction.enterMissingValue();
  }

  public static RepairAction ofReplaceFieldNameWithOntologyTerm(@Nonnull List<SuggestedOntologyTerm> suggestedOntologyTerms) {
    return AutoOneOf_RepairAction.replaceFieldNameWithOntologyTerm(suggestedOntologyTerms);
  }

  @Nonnull
  public abstract Kind getKind();

  @Nonnull
  public abstract void enterMissingValue();

  @Nonnull
  public abstract List<SuggestedOntologyTerm> replaceFieldNameWithOntologyTerm();

  public enum Kind {
    ENTER_MISSING_VALUE,
    REPLACE_FIELD_NAME_WITH_ONTOLOGY_TERM
  }
}
