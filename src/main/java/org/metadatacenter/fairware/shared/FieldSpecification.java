package org.metadatacenter.fairware.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class FieldSpecification {

  private static final String SCHEMA_IRI = "schemaIri";
  private static final String IRI = "iri";
  private static final String NAME = "name";
  private static final String LABEL = "label";
  private static final String DATA_TYPE = "dataType";
  private static final String IS_REQUIRED = "isRequired";
  private static final String USE_VOCABULARY = "useVocabulary";

  @JsonCreator
  public static FieldSpecification create(@Nonnull @JsonProperty(SCHEMA_IRI) String schemaIri,
                                          @Nonnull @JsonProperty(IRI) String iri,
                                          @Nonnull @JsonProperty(NAME) String name,
                                          @Nonnull @JsonProperty(LABEL) String label,
                                          @Nonnull @JsonProperty(DATA_TYPE) String dataType,
                                          @JsonProperty(IS_REQUIRED) boolean isRequired,
                                          @JsonProperty(USE_VOCABULARY) boolean useVocabulary) {
    return new AutoValue_FieldSpecification(schemaIri, iri, name, label, dataType, isRequired, useVocabulary);
  }

  @Nonnull
  @JsonProperty(SCHEMA_IRI)
  public abstract String getSchemaIri();

  @Nonnull
  @JsonProperty(IRI)
  public abstract String getIri();

  @Nonnull
  @JsonProperty(NAME)
  public abstract String getName();

  @Nonnull
  @JsonProperty(LABEL)
  public abstract String getLabel();

  @Nonnull
  @JsonProperty(DATA_TYPE)
  public abstract String getDataType();

  @Nonnull
  @JsonProperty(IS_REQUIRED)
  public abstract boolean isRequired();

  @Nonnull
  @JsonProperty(USE_VOCABULARY)
  public abstract boolean useVocabulary();
}
