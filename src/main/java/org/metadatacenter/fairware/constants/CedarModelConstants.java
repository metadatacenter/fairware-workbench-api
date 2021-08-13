package org.metadatacenter.fairware.constants;

public class CedarModelConstants
{

  private CedarModelConstants()
  {
  }

  /*
   * JSON-LD Keywords
   */
  public static final String JSON_LD_CONTEXT = "@context";
  public static final String JSON_LD_ID = "@id";
  public static final String JSON_LD_TYPE = "@type";
  public static final String JSON_LD_VALUE = "@value";
  public static final String JSON_LD_GRAPH = "@graph";
  public static final String JSON_LD_BASE = "@base";
  public static final String JSON_LD_CONTAINER = "@container";
  public static final String JSON_LD_INDEX = "@index";
  public static final String JSON_LD_LANGUAGE = "@language";
  public static final String JSON_LD_LIST = "@list";
  public static final String JSON_LD_NEST = "@nest";
  public static final String JSON_LD_NONE = "@none";
  public static final String JSON_LD_PREFIX = "@prefix";
  public static final String JSON_LD_REVERSE = "@reverse";
  public static final String JSON_LD_VERSION = "@version";
  public static final String JSON_LD_VOCAB = "@vocab";

  /*
   * JSON Schema Keywords
   */
  public static final String JSON_SCHEMA_SCHEMA = "$schema";
  public static final String JSON_SCHEMA_REF = "$ref";
  public static final String JSON_SCHEMA_TYPE = "type";
  public static final String JSON_SCHEMA_ARRAY = "array";
  public static final String JSON_SCHEMA_OBJECT = "object";
  public static final String JSON_SCHEMA_TITLE = "title";
  public static final String JSON_SCHEMA_DESCRIPTION = "description";
  public static final String JSON_SCHEMA_PROPERTIES = "properties";
  public static final String JSON_SCHEMA_FORMAT = "format";
  public static final String JSON_SCHEMA_ENUM = "enum";
  public static final String JSON_SCHEMA_ONE_OF = "oneOf";
  public static final String JSON_SCHEMA_ITEMS = "items";
  public static final String JSON_SCHEMA_UNIQUE_ITEMS = "uniqueItems";
  public static final String JSON_SCHEMA_MIN_ITEMS = "minItems";
  public static final String JSON_SCHEMA_MAX_ITEMS = "maxItems";
  public static final String JSON_SCHEMA_MIN_LENGTH = "minLength";
  public static final String JSON_SCHEMA_MAX_LENGTH = "maxLength";
  public static final String JSON_SCHEMA_MINIMUM = "minimum";
  public static final String JSON_SCHEMA_REQUIRED = "required";
  public static final String JSON_SCHEMA_PATTERN_PROPERTIES = "patternProperties";
  public static final String JSON_SCHEMA_ADDITIONAL_PROPERTIES = "additionalProperties";

  /*
   * CEDAR Artifact Keywords
   */

  // CEDAR model keywords that can occur at the top level of all (schema and instance) artifacts
  public static final String SCHEMA_ORG_NAME = "schema:name";
  public static final String SCHEMA_ORG_DESCRIPTION = "schema:description";
  public static final String SCHEMA_ORG_IDENTIFIER = "schema:identifier";
  public static final String PAV_CREATED_ON = "pav:createdOn";
  public static final String PAV_CREATED_BY = "pav:createdBy";
  public static final String PAV_LAST_UPDATED_ON = "pav:lastUpdatedOn";
  public static final String PAV_DERIVED_FROM = "pav:derivedFrom";
  public static final String OSLC_MODIFIED_BY = "oslc:modifiedBy";

  // CEDAR model keywords that can occur at the top level of all schema artifacts
  public static final String SCHEMA_ORG_SCHEMA_VERSION = "schema:schemaVersion";
  public static final String PAV_VERSION = "pav:version";
  public static final String PAV_PREVIOUS_VERSION = "pav:previousVersion";
  public static final String BIBO_STATUS = "bibo:status";
  public static final String UI = "_ui";

  // CEDAR model keywords that can occur at the top level of field schema artifacts
  public static final String VALUE_CONSTRAINTS = "_valueConstraints";

  // CEDAR model keywords that can occur at the top level of template instance artifacts
  public static final String SCHEMA_IS_BASED_ON = "schema:isBasedOn";

  // Keywords that can occur in a field instance artifact
  public static final String RDFS_LABEL = "rdfs:label";
  public static final String SKOS_NOTATION = "skos:notation";
  public static final String SKOS_PREFLABEL = "skos:prefLabel";
  public static final String SKOS_ALTLABEL = "skos:altLabel";

  // CEDAR keywords that can occur in a template and element schema artifact's _UI object;
  public static final String UI_PAGES = "pages";
  public static final String UI_PROPERTY_LABELS = "propertyLabels";
  public static final String UI_PROPERTY_DESCRIPTIONS = "propertyDescriptions";
  public static final String UI_ORDER = "order";

  // CEDAR keywords that can occur in a field schema artifact's _UI object;
  public static final String UI_VALUE_RECOMMENDATION_ENABLED = "valueRecommendationEnabled";
  public static final String UI_FIELD_INPUT_TYPE = "inputType";
  public static final String UI_HIDDEN = "hidden";
  public static final String UI_INPUT_TIME_FORMAT = "inputTimeFormat";
  public static final String UI_TEMPORAL_GRANULARITY = "temporalGranularity";
  public static final String UI_TIMEZONE_ENABLED = "timezoneEnabled";
  public static final String UI_CONTENT = "_content";
  public static final String UI_HEADER = "header";
  public static final String UI_FOOTER = "footer";

  // CEDAR field input types
  public static final String FIELD_INPUT_TYPE_TEXTFIELD = "textfield";
  public static final String FIELD_INPUT_TYPE_TEXTAREA = "textarea";
  public static final String FIELD_INPUT_TYPE_RADIO = "radio";
  public static final String FIELD_INPUT_TYPE_CHECKBOX = "checkbox";
  public static final String FIELD_INPUT_TYPE_TEMPORAL = "temporal";
  public static final String FIELD_INPUT_TYPE_EMAIL = "email";
  public static final String FIELD_INPUT_TYPE_LIST = "list";
  public static final String FIELD_INPUT_TYPE_NUMERIC = "numeric";
  public static final String FIELD_INPUT_TYPE_PHONE_NUMBER = "phone-number";
  public static final String FIELD_INPUT_TYPE_SECTION_BREAK = "section-break";
  public static final String FIELD_INPUT_TYPE_RICH_TEXT = "richtext";
  public static final String FIELD_INPUT_TYPE_IMAGE = "image";
  public static final String FIELD_INPUT_TYPE_LINK = "link";
  public static final String FIELD_INPUT_TYPE_YOUTUBE = "youtube";

  // CEDAR keywords that can occur in a field schema artifact's _valueConstraints object
  public static final String VALUE_CONSTRAINTS_ONTOLOGIES = "ontologies";
  public static final String VALUE_CONSTRAINTS_VALUE_SETS = "valueSets";
  public static final String VALUE_CONSTRAINTS_CLASSES = "classes";
  public static final String VALUE_CONSTRAINTS_BRANCHES = "branches";
  public static final String VALUE_CONSTRAINTS_LITERALS = "literals";
  public static final String VALUE_CONSTRAINTS_MULTIPLE_CHOICE = "multipleChoice";
  public static final String VALUE_CONSTRAINTS_DEFAULT_VALUE = "defaultValue";
  public static final String VALUE_CONSTRAINTS_URI = "uri";
  public static final String VALUE_CONSTRAINTS_LABEL = "label";
  public static final String VALUE_CONSTRAINTS_PREFLABEL = "prefLabel";
  public static final String VALUE_CONSTRAINTS_TYPE = "type";
  public static final String VALUE_CONSTRAINTS_NAME = "name";
  public static final String VALUE_CONSTRAINTS_SOURCE = "source";
  public static final String VALUE_CONSTRAINTS_ACRONYM = "acronym";
  public static final String VALUE_CONSTRAINTS_NUM_TERMS = "numTerms";
  public static final String VALUE_CONSTRAINTS_MAX_DEPTH = "maxDepth";
  public static final String VALUE_CONSTRAINTS_VS_COLLECTION = "vsCollection";
  public static final String VALUE_CONSTRAINTS_EXCLUSIONS = "exclusions";
  public static final String VALUE_CONSTRAINTS_REQUIRED_VALUE = "requiredValue";
  public static final String VALUE_CONSTRAINTS_SELECTED_BY_DEFAULT = "selectedByDefault";
  public static final String VALUE_CONSTRAINTS_MIN_STRING_LENGTH = "minLength";
  public static final String VALUE_CONSTRAINTS_MAX_STRING_LENGTH = "maxLength";
  public static final String VALUE_CONSTRAINTS_MIN_NUMBER_VALUE = "minValue";
  public static final String VALUE_CONSTRAINTS_MAX_NUMBER_VALUE = "maxValue";
  public static final String VALUE_CONSTRAINTS_DECIMAL_PLACE = "decimalPlace";
  public static final String VALUE_CONSTRAINTS_NUMBER_TYPE = "numberType";
  public static final String VALUE_CONSTRAINTS_UNIT_OF_MEASURE = "unitOfMeasure";
  public static final String VALUE_CONSTRAINTS_TEMPORAL_TYPE = "temporalType";

  public static final String RDFS = "rdfs";
  public static final String XSD = "xsd";
  public static final String PAV = "pav";
  public static final String SCHEMA = "schema";
  public static final String OSLC = "oslc";
  public static final String SKOS = "skos";
  public static final String BIBO = "bibo";
}
