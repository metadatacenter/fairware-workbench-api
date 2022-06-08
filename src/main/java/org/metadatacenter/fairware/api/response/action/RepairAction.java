package org.metadatacenter.fairware.api.response.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoOneOf;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoOneOf(RepairAction.Kind.class)
public abstract class RepairAction {

  private static final String REPAIR_COMMAND = "repairCommand";
  private static final String VALUE_SUGGESTIONS = "valueSuggestions";

  public static RepairAction ofEnterMissingValue() {
    return AutoOneOf_RepairAction.enterMissingValue();
  }

  public static RepairAction ofEnterCorrectValue() {
    return AutoOneOf_RepairAction.enterCorrectValue();
  }

  public static RepairAction ofEnterStringValue(@Nonnull String value) {
    return AutoOneOf_RepairAction.enterStringValue(value);
  }

  public static RepairAction ofEnterNumberValue(@Nonnull Number value) {
    return AutoOneOf_RepairAction.enterNumberValue(value);
  }

  public static RepairAction ofReplaceMetadataValueWithStandardizedValue(@Nonnull ImmutableList<SuggestedOntologyTerm> ontologyTerms) {
    return AutoOneOf_RepairAction.replaceMetadataValueWithStandardizedValue(ontologyTerms);
  }

  public static RepairAction ofReplaceMetadataFieldWithStandardizedName(@Nonnull ImmutableList<SuggestedOntologyTerm> suggestedOntologyTerms) {
    return AutoOneOf_RepairAction.replaceMetadataFieldWithStandardizedName(suggestedOntologyTerms);
  }

  @Nonnull
  @JsonProperty(REPAIR_COMMAND)
  public abstract Kind getKind();

  @Nonnull
  @JsonProperty(VALUE_SUGGESTIONS)
  public Optional<ImmutableList<String>> getValueSuggestion() {
    switch (getKind()) {
      case REPLACE_METADATA_VALUE_WITH_STANDARDIZED_VALUE:
        var valueSuggestions = replaceMetadataValueWithStandardizedValue().stream()
            .map(SuggestedOntologyTerm::getUri)
            .collect(ImmutableList.toImmutableList());
        return valueSuggestions.isEmpty() ? Optional.empty() : Optional.of(valueSuggestions);
      case REPLACE_METADATA_FIELD_WITH_STANDARDIZED_NAME:
        var fieldSuggestions = replaceMetadataFieldWithStandardizedName().stream()
            .map(SuggestedOntologyTerm::getLabel)
            .collect(ImmutableList.toImmutableList());
        return fieldSuggestions.isEmpty() ? Optional.empty() : Optional.of(fieldSuggestions);
    }
    return Optional.empty();
  }

  @Nonnull
  public abstract void enterMissingValue();

  @Nonnull
  public abstract void enterCorrectValue();

  @Nonnull
  public abstract String enterStringValue();

  @Nonnull
  public abstract Number enterNumberValue();

  @Nonnull
  public abstract ImmutableList<SuggestedOntologyTerm> replaceMetadataValueWithStandardizedValue();

  @Nonnull
  public abstract ImmutableList<SuggestedOntologyTerm> replaceMetadataFieldWithStandardizedName();

  public enum Kind {
    ENTER_MISSING_VALUE,
    ENTER_CORRECT_VALUE,
    ENTER_STRING_VALUE,
    ENTER_NUMBER_VALUE,
    REPLACE_METADATA_VALUE_WITH_STANDARDIZED_VALUE,
    REPLACE_METADATA_FIELD_WITH_STANDARDIZED_NAME
  }
}
