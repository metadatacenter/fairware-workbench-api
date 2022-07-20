package org.metadatacenter.fairware.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.metadatacenter.fairware.shared.Metadata;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class HttpJsonFileParser {

  private final ObjectMapper objectMapper;

  private ImmutableSet<String> fieldNames = ImmutableSet.of();

  public HttpJsonFileParser(@Nonnull ObjectMapper objectMapper) {
    this.objectMapper = checkNotNull(objectMapper);
  }

  public Metadata parse(String metadataString, String defaultMetadataId) throws JsonProcessingException {
    var metadata = objectMapper.readTree(metadataString);
    return Metadata.create(
        defaultMetadataId,
        extractMetadataName(metadata, "Record #" + RandomStringUtils.randomNumeric(8)),
        extractMetadataFields(metadata),
        asImmutableMap(metadata));
  }

  private String extractMetadataName(JsonNode metadata, String defaultName) {
    var firstMatchNameField = extractMetadataFields(metadata).stream()
        .filter((fieldName) -> fieldName.matches("(?i)id$"))
        .findFirst();
    return firstMatchNameField.orElse(defaultName);
  }

  private ImmutableSet<String> extractMetadataFields(JsonNode metadata) {
    if (fieldNames.isEmpty()) {
      fieldNames = asImmutableMap(metadata).keySet();
    }
    return fieldNames;
  }

  private ImmutableMap<String, Object> asImmutableMap(JsonNode metadata) {
    return flattenObject(metadata); // TODO: Support nested fields
  }

  private ImmutableMap<String, Object> flattenObject(JsonNode root) {
    var flatMap = Maps.<String, Object>newHashMap();
    var fieldIterator = root.fieldNames();
    fieldIterator.forEachRemaining(fieldName -> {
      var value = root.get(fieldName);
      if (!value.isContainerNode()) {
        if (value.isTextual()) {
          flatMap.put(fieldName, value.textValue());
        } else if (value.isNumber()) {
          flatMap.put(fieldName, value.numberValue());
        } else if (value.isBoolean()) {
          flatMap.put(fieldName, value.booleanValue());
        } else if (value.isNull()) {
          flatMap.put(fieldName, Optional.empty());
        }
      }
    });
    return ImmutableMap.copyOf(flatMap);
  }
}
