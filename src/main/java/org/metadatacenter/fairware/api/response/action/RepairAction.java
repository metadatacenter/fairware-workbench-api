package org.metadatacenter.fairware.api.response.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoOneOf;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@AutoOneOf(RepairAction.Kind.class)
public abstract class RepairAction {

  private static final String REPAIR_COMMAND = "repairCommand";
  private static final String VALUE_SUGGESTION = "valueSuggestion";

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

  public static RepairAction ofReplaceMetadataValueWithStandardizedValue(@Nonnull SuggestedOntologyTerm ontologyTerm) {
    return AutoOneOf_RepairAction.replaceMetadataValueWithStandardizedValue(ontologyTerm);
  }

  public static RepairAction ofReplaceMetadataFieldWithStandardizedName(@Nonnull List<SuggestedOntologyTerm> suggestedOntologyTerms) {
    return AutoOneOf_RepairAction.replaceMetadataFieldWithStandardizedName(suggestedOntologyTerms);
  }

  @Nonnull
  @JsonProperty(REPAIR_COMMAND)
  public abstract Kind getKind();

  @Nonnull
  @JsonProperty(VALUE_SUGGESTION)
  public Optional<String> getValueSuggestion() {
    switch (getKind()) {
      case REPLACE_METADATA_VALUE_WITH_STANDARDIZED_VALUE:
        return Optional.of(replaceMetadataValueWithStandardizedValue().getUri());
      case REPLACE_METADATA_FIELD_WITH_STANDARDIZED_NAME:
        return replaceMetadataFieldWithStandardizedName().stream()
            .findFirst()
            .map(SuggestedOntologyTerm::getLabel);
    }
    return Optional.empty();
  }

  @Nonnull
  public abstract void enterMissingValue();

  @Nonnull
  public abstract void enterCorrectValue();

  @Nonnull
  public abstract String enterStringTypeValue();

  @Nonnull
  public abstract Number enterNumberTypeValue();

  @Nonnull
  public abstract SuggestedOntologyTerm replaceMetadataValueWithStandardizedValue();

  @Nonnull
  public abstract List<SuggestedOntologyTerm> replaceMetadataFieldWithStandardizedName();

  public enum Kind {
    ENTER_MISSING_VALUE,
    ENTER_CORRECT_VALUE,
    ENTER_STRING_TYPE_VALUE,
    ENTER_NUMBER_TYPE_VALUE,
    REPLACE_METADATA_VALUE_WITH_STANDARDIZED_VALUE,
    REPLACE_METADATA_FIELD_WITH_STANDARDIZED_NAME
  }
}
