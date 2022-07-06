package org.metadatacenter.fairware.core.domain;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class CedarTemplateFieldSpecification {

  @Nonnull
  public static CedarTemplateFieldSpecification create(@Nonnull String name,
                                                       @Nonnull Optional<String> prefLabel,
                                                       @Nonnull ValueType valueType,
                                                       boolean isRequired,
                                                       boolean allowMultipleValues,
                                                       @Nonnull Optional<ImmutableList<ValueConstraint>> valueConstraints,
                                                       @Nullable String nullableValueFormat,
                                                       @Nonnull Optional<CedarTemplateField> parentField) {
    return new AutoValue_CedarTemplateFieldSpecification(name, prefLabel, valueType, isRequired, allowMultipleValues,
        valueConstraints, nullableValueFormat, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofObjectField(@Nonnull String name,
                                                              @Nonnull Optional<String> prefLabel,
                                                              boolean allowMultipleValues,
                                                              @Nonnull Optional<CedarTemplateField> parentField) {
    return create(name, prefLabel, ValueType.OBJECT, false, allowMultipleValues,
        Optional.empty(), null, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofValueField(@Nonnull String name,
                                                             @Nonnull Optional<String> prefLabel,
                                                             @Nonnull ValueType valueType,
                                                             boolean isRequired,
                                                             boolean allowMultipleValues,
                                                             @Nonnull Optional<ImmutableList<ValueConstraint>> valueConstraints,
                                                             @Nonnull Optional<CedarTemplateField> parentField) {
    return create(name, prefLabel, valueType, isRequired, allowMultipleValues,
        valueConstraints, null, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofDateTimeField(@Nonnull String name,
                                                                @Nonnull Optional<String> prefLabel,
                                                                boolean isRequired,
                                                                boolean allowMultipleValues,
                                                                @Nonnull String valueFormat,
                                                                @Nonnull Optional<CedarTemplateField> parentField) {
    return create(name, prefLabel, ValueType.DATE_TIME, isRequired, allowMultipleValues,
        Optional.empty(), valueFormat, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofDateField(@Nonnull String name,
                                                            @Nonnull Optional<String> prefLabel,
                                                            boolean isRequired,
                                                            boolean allowMultipleValues,
                                                            @Nonnull String valueFormat,
                                                            @Nonnull Optional<CedarTemplateField> parentField) {
    return create(name, prefLabel, ValueType.DATE, isRequired, allowMultipleValues,
        Optional.empty(), valueFormat, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofTimeField(@Nonnull String name,
                                                            @Nonnull Optional<String> prefLabel,
                                                            boolean isRequired,
                                                            boolean allowMultipleValues,
                                                            @Nonnull String valueFormat,
                                                            @Nonnull Optional<CedarTemplateField> parentField) {
    return create(name, prefLabel, ValueType.TIME, isRequired, allowMultipleValues,
        Optional.empty(), valueFormat, parentField);
  }

  @Nonnull
  public abstract String getName();

  @Nonnull
  public abstract Optional<String> getPrefLabel();

  @Nonnull
  public abstract ValueType getValueType();

  @Nonnull
  public String getJsonValueType() {
    switch (getValueType()) {
      case STRING:
      case DATE_TIME:
      case DATE:
      case TIME:
        return "string";
      case NUMBER:
        return "number";
      case OBJECT:
        return "object";
      default:
        return "string";
    }
  }

  public abstract boolean isRequired();

  public abstract boolean allowMultipleValues();

  @Nonnull
  public abstract Optional<ImmutableList<ValueConstraint>> getValueConstraints();

  @Nullable
  public abstract String getNullableValueFormat();

  @Nonnull
  public Optional<String> getValueFormat() {
    return Optional.ofNullable(getNullableValueFormat());
  }

  @Nonnull
  public abstract Optional<CedarTemplateField> getParentField();
}
