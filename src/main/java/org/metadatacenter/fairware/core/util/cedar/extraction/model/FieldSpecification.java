package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
public abstract class FieldSpecification {

  @Nonnull
  public static FieldSpecification create(@Nonnull String name,
                                          @Nonnull Optional<String> prefLabel,
                                          @Nonnull ValueType valueType,
                                          boolean isRequired,
                                          boolean allowMultipleValues,
                                          @Nonnull Optional<ImmutableList<ValueConstraint>> valueConstraints,
                                          @Nonnull Optional<TemplateField> parentField) {
    return new AutoValue_FieldSpecification(name, prefLabel, valueType, isRequired, allowMultipleValues,
        valueConstraints, parentField);
  }

  @Nonnull
  public static FieldSpecification ofObjectField(@Nonnull String name,
                                                 @Nonnull Optional<String> prefLabel,
                                                 boolean allowMultipleValues,
                                                 @Nonnull Optional<TemplateField> parentField) {
    return create(name, prefLabel, ValueType.OBJECT, false, allowMultipleValues,
        Optional.empty(), parentField);
  }

  @Nonnull
  public static FieldSpecification ofValueField(@Nonnull String name,
                                                @Nonnull Optional<String> prefLabel,
                                                @Nonnull ValueType valueType,
                                                boolean isRequired,
                                                boolean allowMultipleValues,
                                                @Nonnull Optional<ImmutableList<ValueConstraint>> valueConstraints,
                                                @Nonnull Optional<TemplateField> parentField) {
    return create(name, prefLabel, valueType, isRequired, allowMultipleValues,
        valueConstraints, parentField);
  }

  @Nonnull
  public abstract String getName();

  @Nonnull
  public abstract Optional<String> getPrefLabel();

  @Nonnull
  public abstract ValueType getValueType();

  public abstract boolean isRequired();

  public abstract boolean allowMultipleValues();

  @Nonnull
  public abstract Optional<ImmutableList<ValueConstraint>> getValueConstraints();

  @Nonnull
  public abstract Optional<TemplateField> getParentField();
}
