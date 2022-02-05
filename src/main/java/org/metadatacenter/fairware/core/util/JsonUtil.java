package org.metadatacenter.fairware.core.util;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonUtil {

  public static String getStringValue(JsonNode node, String fieldName) {
    if (!node.has(fieldName)) {
      throw new IllegalArgumentException("Missing required Json field: '" + fieldName + "'");
    }
    return node.get(fieldName).asText();
  }

  public static JsonNode getJsonNodeValue(JsonNode node, String fieldName) {
    if (!node.has(fieldName)) {
      throw new IllegalArgumentException("Missing required Json field: '" + fieldName + "'");
    }
    return node.get(fieldName);
  }





}




