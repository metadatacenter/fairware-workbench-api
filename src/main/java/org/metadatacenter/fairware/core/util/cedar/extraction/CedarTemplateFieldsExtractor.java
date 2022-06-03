package org.metadatacenter.fairware.core.util.cedar.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.FieldSpecification;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateField;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.ValueConstraint;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.ValueType;

import javax.annotation.Nonnull;
import java.util.Optional;

import static org.metadatacenter.fairware.constants.CedarModelConstants.JSON_LD_TYPE;
import static org.metadatacenter.fairware.constants.CedarModelConstants.JSON_SCHEMA_ITEMS;
import static org.metadatacenter.fairware.constants.CedarModelConstants.REQUIRED_VALUE;
import static org.metadatacenter.fairware.constants.CedarModelConstants.SCHEMA_ORG_NAME;
import static org.metadatacenter.fairware.constants.CedarModelConstants.SKOS_PREFLABEL;
import static org.metadatacenter.fairware.constants.CedarModelConstants.VALUE_CONSTRAINTS;
import static org.metadatacenter.fairware.core.domain.CedarArtifactType.ELEMENT;
import static org.metadatacenter.fairware.core.domain.CedarArtifactType.FIELD;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class CedarTemplateFieldsExtractor {

  private static boolean isFieldType(JsonNode node) {
    if (node.has(JSON_SCHEMA_ITEMS)) {
      node = node.get(JSON_SCHEMA_ITEMS);
    }
    var typeNode = node.get(JSON_LD_TYPE);
    return typeNode != null && typeNode.asText().equals(FIELD.getAtType());
  }

  private static boolean isElementType(JsonNode node) {
    if (node.has(JSON_SCHEMA_ITEMS)) {
      node = node.get(JSON_SCHEMA_ITEMS);
    }
    var typeNode = node.get(JSON_LD_TYPE);
    return typeNode != null && typeNode.asText().equals(ELEMENT.getAtType());
  }

  @Nonnull
  public ImmutableList<TemplateField> extractFields(@Nonnull JsonNode templateNode) {
    return extractFieldsFromTemplate(templateNode);
  }

  private ImmutableList<TemplateField> extractFieldsFromTemplate(JsonNode templateNode) {
    var collector = Lists.<TemplateField>newArrayList();
    var fieldNames = getFieldsFromTemplateOrElement(templateNode);
    var propertiesNode = templateNode.get("properties");
    for (var fieldName : fieldNames) {
      var node = propertiesNode.get(fieldName);
      if (isElementType(node)) {
        var templateFields = extractFieldsFromElement(node, Optional.empty());
        collector.addAll(templateFields);
      } else if (isFieldType(node)) {
        var templateField = getTemplateField(node, Optional.empty());
        collector.add(templateField);
      } else {
        throw new RuntimeException("Element or field is not found. Please validate the template in CEDAR.");
      }
    }
    return ImmutableList.copyOf(collector);
  }

  private ImmutableList<String> getFieldsFromTemplateOrElement(JsonNode templateNode) {
    return Streams.<JsonNode>stream(templateNode.get("_ui").withArray("order").elements())
        .map(JsonNode::asText)
        .collect(ImmutableList.toImmutableList());
  }

  private ImmutableList<TemplateField> extractFieldsFromElement(JsonNode elementNode, Optional<TemplateField> parentField) {
    var allowMultipleValues = elementNode.has(JSON_SCHEMA_ITEMS);
    if (allowMultipleValues) {
      elementNode = elementNode.get(JSON_SCHEMA_ITEMS);
    }
    var collector = Lists.<TemplateField>newArrayList();
    var fieldNames = getFieldsFromTemplateOrElement(elementNode);
    var propertiesNode = elementNode.get("properties");
    var objectField = TemplateField.ofObjectField(
        FieldSpecification.ofObjectField(
            getName(elementNode), getPrefLabel(elementNode), allowMultipleValues, parentField));
    for (var fieldName : fieldNames) {
      var node = propertiesNode.get(fieldName);
      if (isElementType(node)) {
        var templateFields = extractFieldsFromElement(node, Optional.of(objectField));
        collector.addAll(templateFields);
      } else if (isFieldType(node)) {
        var templateField = getTemplateField(node, Optional.of(objectField));
        collector.add(templateField);
      } else {
        throw new RuntimeException("Element or field is not found. Please validate the template in CEDAR.");
      }
    }
    return ImmutableList.copyOf(collector);
  }

  private TemplateField getTemplateField(JsonNode fieldNode, Optional<TemplateField> parentField) {
    var allowMultipleValues = fieldNode.has(JSON_SCHEMA_ITEMS);
    if (allowMultipleValues) {
      fieldNode = fieldNode.get(JSON_SCHEMA_ITEMS);
    }
    var valueType = getValueType(fieldNode);
    switch (valueType) {
      case DATE_TIME:
        return TemplateField.ofValueField(
            FieldSpecification.ofDateTimeField(
                getName(fieldNode), getPrefLabel(fieldNode),
                isRequired(fieldNode), allowMultipleValues,
                getDateTimeFormat(fieldNode), parentField));
      case DATE:
        return TemplateField.ofValueField(
            FieldSpecification.ofDateField(
                getName(fieldNode), getPrefLabel(fieldNode),
                isRequired(fieldNode), allowMultipleValues,
                getDateFormat(fieldNode), parentField));
      case TIME:
        return TemplateField.ofValueField(
            FieldSpecification.ofTimeField(
                getName(fieldNode), getPrefLabel(fieldNode),
                isRequired(fieldNode), allowMultipleValues,
                getTimeFormat(fieldNode), parentField));
      default:
        return TemplateField.ofValueField(
            FieldSpecification.ofValueField(
                getName(fieldNode), getPrefLabel(fieldNode),
                getValueType(fieldNode), isRequired(fieldNode),
                allowMultipleValues, getValueConstraints(fieldNode),
                parentField));
    }
  }

  private String getName(JsonNode node) {
    if (!node.has(SCHEMA_ORG_NAME)) {
      throw new RuntimeException("Field name is missing. Please validate the template in CEDAR.");
    }
    return node.get(SCHEMA_ORG_NAME).asText();
  }

  private Optional<String> getPrefLabel(JsonNode node) {
    var prefLabel = Optional.<String>empty();
    if (node.has(SKOS_PREFLABEL)) {
      prefLabel = Optional.ofNullable(node.get(SKOS_PREFLABEL).asText());
    }
    return prefLabel;
  }

  private ValueType getValueType(JsonNode node) {
    var inputType = node.get("_ui").get("inputType").asText();
    if ("textfield".equals(inputType)) {
      return ValueType.STRING;
    } else if ("numeric".equals(inputType)) {
      return ValueType.NUMBER;
    } else if ("temporal".equals(inputType)) {
      var temporalType = node.get("_valueConstraints").get("temporalType").asText();
      if ("xsd:dateTime".equals(temporalType)) {
        return ValueType.DATE_TIME;
      } else if ("xsd:date".equals(temporalType)) {
          return ValueType.DATE;
      } else if ("xsd:time".equals(temporalType)) {
        return ValueType.TIME;
      }
    }
    return ValueType.UNSUPPORTED_TYPE;
  }

  private boolean isRequired(JsonNode node) {
    return node.get(VALUE_CONSTRAINTS).get(REQUIRED_VALUE).asBoolean();
  }

  private Optional<ImmutableList<ValueConstraint>> getValueConstraints(JsonNode node) {
    var valueConstraints = node.get(VALUE_CONSTRAINTS);
    var collector = Lists.<ValueConstraint>newArrayList();

    // Ontologies as value constraints
    var ontologies = Streams.<JsonNode>stream(valueConstraints.withArray("ontologies").elements())
        .map(o -> o.get("acronym").asText())
        .map(ValueConstraint::ofOntology)
        .collect(ImmutableList.toImmutableList());
    collector.addAll(ontologies);

    // Value sets as value constraints
    var valueSets = Streams.<JsonNode>stream(valueConstraints.withArray("valueSets").elements())
        .map(o -> o.get("name").asText())
        .map(ValueConstraint::ofValueSet)
        .collect(ImmutableList.toImmutableList());
    collector.addAll(valueSets);

    // Branches as value constraints
    var branches = Streams.<JsonNode>stream(valueConstraints.withArray("branches").elements())
        .map(o -> o.get("uri").asText())
        .map(ValueConstraint::ofBranch)
        .collect(ImmutableList.toImmutableList());
    collector.addAll(branches);

    // Term classes as value constraints
    var terms = Streams.<JsonNode>stream(valueConstraints.withArray("classes").elements())
        .map(o -> o.get("uri").asText())
        .map(ValueConstraint::ofTerm)
        .collect(ImmutableList.toImmutableList());
    collector.addAll(terms);

    return collector.isEmpty()
        ? Optional.empty()
        : Optional.of(ImmutableList.copyOf(collector));
  }

  private String getDateTimeFormat(JsonNode node) {
    var temporalGranularity = node.get("_ui").get("temporalGranularity").asText();
    if ("second".equals(temporalGranularity)) {
      return "yyyy-MM-dd[[ ]['T']]hh:mm:ss[[]['Z']";
    } else if ("decimalSecond".equals(temporalGranularity)) {
      return "yyyy-MM-dd[[ ]['T']]hh:mm:ss.SSS[[]['Z']";
    } else if ("minute".equals(temporalGranularity)) {
      return "yyyy-MM-dd[[ ]['T']]hh:mm[[]['Z']";
    } else if ("hour".equals(temporalGranularity)) {
      return "yyyy-MM-dd[[ ]['T']]hh[[]['Z']";
    }
    throw new IllegalArgumentException("Unknown temporal granularity from CEDAR template: " + temporalGranularity);
  }

  private String getDateFormat(JsonNode node) {
    var temporalGranularity = node.get("_ui").get("temporalGranularity").asText();
    if ("day".equals(temporalGranularity)) {
      return "yyyy-MM-dd";
    } else if ("month".equals(temporalGranularity)) {
      return "yyyy-MM";
    } else if ("year".equals(temporalGranularity)) {
      return "yyyy";
    }
    throw new IllegalArgumentException("Unknown temporal granularity from CEDAR template: " + temporalGranularity);
  }

  private String getTimeFormat(JsonNode node) {
    var temporalGranularity = node.get("_ui").get("temporalGranularity").asText();
    if ("second".equals(temporalGranularity)) {
      return "hh:mm:ss";
    } else if ("decimalSecond".equals(temporalGranularity)) {
      return "hh:mm:ss.SSS";
    } else if ("minute".equals(temporalGranularity)) {
      return "hh:mm";
    } else if ("hour".equals(temporalGranularity)) {
      return "hh";
    }
    throw new IllegalArgumentException("Unknown temporal granularity from CEDAR template: " + temporalGranularity);
  }
}
