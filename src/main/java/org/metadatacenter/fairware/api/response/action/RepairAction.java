package org.metadatacenter.fairware.api.response.action;

import com.google.auto.value.AutoOneOf;

import javax.annotation.Nonnull;
import java.util.List;

@AutoOneOf(RepairAction.Kind.class)
public abstract class RepairAction {

  public static RepairAction ofEnterMissingValue() {
    return AutoOneOf_RepairAction.enterMissingValue();
  }

  public static RepairAction ofEnterCorrectValue() {
    return AutoOneOf_RepairAction.enterCorrectValue();
  }

  public static RepairAction ofEnterStringTypeValue(@Nonnull String value) {
    return AutoOneOf_RepairAction.enterStringTypeValue(value);
  }

  public static RepairAction ofEnterNumberTypeValue(@Nonnull Number value) {
    return AutoOneOf_RepairAction.enterNumberTypeValue(value);
  }

  public static RepairAction ofReplaceMetadataValueWithOntologyTerm(@Nonnull SuggestedOntologyTerm ontologyTerm) {
    return AutoOneOf_RepairAction.replaceMetadataValueWithOntologyTerm(ontologyTerm);
  }

  public static RepairAction ofReplaceFieldNameWithOntologyTerm(@Nonnull List<SuggestedOntologyTerm> suggestedOntologyTerms) {
    return AutoOneOf_RepairAction.replaceFieldNameWithOntologyTerm(suggestedOntologyTerms);
  }

  @Nonnull
  public abstract Kind getKind();

  @Nonnull
  public abstract void enterMissingValue();

  @Nonnull
  public abstract void enterCorrectValue();

  @Nonnull
  public abstract String enterStringTypeValue();

  @Nonnull
  public abstract Number enterNumberTypeValue();

  @Nonnull
  public abstract SuggestedOntologyTerm replaceMetadataValueWithOntologyTerm();

  @Nonnull
  public abstract List<SuggestedOntologyTerm> replaceFieldNameWithOntologyTerm();

  public enum Kind {
    ENTER_MISSING_VALUE,
    ENTER_CORRECT_VALUE,
    ENTER_STRING_TYPE_VALUE,
    ENTER_NUMBER_TYPE_VALUE,
    REPLACE_METADATA_VALUE_WITH_ONTOLOGY_TERM,
    REPLACE_FIELD_NAME_WITH_ONTOLOGY_TERM
  }
}
