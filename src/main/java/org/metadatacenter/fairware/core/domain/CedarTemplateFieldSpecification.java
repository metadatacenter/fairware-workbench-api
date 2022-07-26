package org.metadatacenter.fairware.core.domain;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class CedarTemplateFieldSpecification {

  @Nonnull
  public static CedarTemplateFieldSpecification create(@Nonnull String schemaIri,
                                                       @Nonnull String iri,
                                                       @Nonnull String name,
                                                       @Nonnull Optional<String> prefLabel,
                                                       @Nonnull String dataType,
                                                       boolean isRequired,
                                                       boolean allowMultipleValues,
                                                       @Nonnull Optional<ImmutableList<ValueConstraint>> valueConstraints,
                                                       @Nullable String nullableValueFormat,
                                                       @Nonnull Optional<CedarTemplateField> parentField) {
    return new AutoValue_CedarTemplateFieldSpecification(schemaIri, iri, name, prefLabel, dataType, isRequired,
        allowMultipleValues, valueConstraints, nullableValueFormat, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofObjectField(@Nonnull String schemaIri,
                                                              @Nonnull String iri,
                                                              @Nonnull String name,
                                                              @Nonnull Optional<String> prefLabel,
                                                              boolean allowMultipleValues,
                                                              @Nonnull Optional<CedarTemplateField> parentField) {
    return create(schemaIri, iri, name, prefLabel, "object", false, allowMultipleValues,
        Optional.empty(), null, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofValueField(@Nonnull String schemaIri,
                                                             @Nonnull String iri,
                                                             @Nonnull String name,
                                                             @Nonnull Optional<String> prefLabel,
                                                             @Nonnull String dataType,
                                                             boolean isRequired,
                                                             boolean allowMultipleValues,
                                                             @Nonnull Optional<ImmutableList<ValueConstraint>> valueConstraints,
                                                             @Nonnull Optional<CedarTemplateField> parentField) {
    return create(schemaIri, iri, name, prefLabel, dataType, isRequired, allowMultipleValues,
        valueConstraints, null, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofDateTimeField(@Nonnull String schemaIri,
                                                                @Nonnull String iri,
                                                                @Nonnull String name,
                                                                @Nonnull Optional<String> prefLabel,
                                                                boolean isRequired,
                                                                boolean allowMultipleValues,
                                                                @Nonnull String valueFormat,
                                                                @Nonnull Optional<CedarTemplateField> parentField) {
    return create(schemaIri, iri, name, prefLabel, "xsd:dateTime", isRequired, allowMultipleValues,
        Optional.empty(), valueFormat, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofDateField(@Nonnull String schemaIri,
                                                            @Nonnull String iri,
                                                            @Nonnull String name,
                                                            @Nonnull Optional<String> prefLabel,
                                                            boolean isRequired,
                                                            boolean allowMultipleValues,
                                                            @Nonnull String valueFormat,
                                                            @Nonnull Optional<CedarTemplateField> parentField) {
    return create(schemaIri, iri, name, prefLabel, "xsd:date", isRequired, allowMultipleValues,
        Optional.empty(), valueFormat, parentField);
  }

  @Nonnull
  public static CedarTemplateFieldSpecification ofTimeField(@Nonnull String schemaIri,
                                                            @Nonnull String iri,
                                                            @Nonnull String name,
                                                            @Nonnull Optional<String> prefLabel,
                                                            boolean isRequired,
                                                            boolean allowMultipleValues,
                                                            @Nonnull String valueFormat,
                                                            @Nonnull Optional<CedarTemplateField> parentField) {
    return create(schemaIri, iri, name, prefLabel, "xsd:time", isRequired, allowMultipleValues,
        Optional.empty(), valueFormat, parentField);
  }

  @Nonnull
  public abstract String getSchemaIri();

  @Nonnull
  public abstract String getIri();

  @Nonnull
  public abstract String getName();

  @Nonnull
  public abstract Optional<String> getPrefLabel();

  @Nonnull
  public abstract String getDataType();

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
