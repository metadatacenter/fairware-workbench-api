package org.metadatacenter.fairware.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoOneOf;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

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

  public static RepairAction ofReplaceMetadataValueWithStandardizedValue(@Nonnull ImmutableList<OntologyTerm> ontologyTerms) {
    return AutoOneOf_RepairAction.replaceMetadataValueWithStandardizedValue(ontologyTerms);
  }

  public static RepairAction ofReplaceMetadataFieldWithStandardizedName(@Nonnull ImmutableList<OntologyTerm> suggestedOntologyTerms) {
    return AutoOneOf_RepairAction.replaceMetadataFieldWithStandardizedName(suggestedOntologyTerms);
  }

  @Nonnull
  @JsonProperty(REPAIR_COMMAND)
  public abstract Kind getKind();

  @Nonnull
  @JsonProperty(VALUE_SUGGESTIONS)
  public ImmutableList<?> getValueSuggestion() {
    switch (getKind()) {
      case ENTER_STRING_VALUE:
        return ImmutableList.of(enterStringValue());
      case ENTER_NUMBER_VALUE:
        return ImmutableList.of(enterNumberValue());
      case REPLACE_METADATA_VALUE_WITH_STANDARDIZED_VALUE:
        return replaceMetadataValueWithStandardizedValue().stream()
            .map((term) -> {
              return new StringBuilder()
                  .append(term.getUri())
                  .append("|")
                  .append(term.getLabel()).toString();
            })
            .collect(ImmutableList.toImmutableList());
      case REPLACE_METADATA_FIELD_WITH_STANDARDIZED_NAME:
        return replaceMetadataFieldWithStandardizedName().stream()
            .map(OntologyTerm::getLabel)
            .collect(ImmutableList.toImmutableList());
      default:
        return ImmutableList.of();
    }
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
  public abstract ImmutableList<OntologyTerm> replaceMetadataValueWithStandardizedValue();

  @Nonnull
  public abstract ImmutableList<OntologyTerm> replaceMetadataFieldWithStandardizedName();

  public enum Kind {
    ENTER_MISSING_VALUE,
    ENTER_CORRECT_VALUE,
    ENTER_STRING_VALUE,
    ENTER_NUMBER_VALUE,
    REPLACE_METADATA_VALUE_WITH_STANDARDIZED_VALUE,
    REPLACE_METADATA_FIELD_WITH_STANDARDIZED_NAME
  }
}
