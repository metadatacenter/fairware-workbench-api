package org.metadatacenter.fairware.core.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarArtifactType {

  FIELD(Types.FIELD, Prefix.FIELDS, AtType.FIELD),
  ELEMENT(Types.ELEMENT, Prefix.ELEMENTS, AtType.ELEMENT),
  TEMPLATE(Types.TEMPLATE, Prefix.TEMPLATES, AtType.TEMPLATE),
  INSTANCE(Types.INSTANCE, Prefix.INSTANCES, null);


  public static class Types {
    public static final String FIELD = "field";
    public static final String ELEMENT = "element";
    public static final String TEMPLATE = "template";
    public static final String INSTANCE = "instance";
  }

  public static class Prefix {
    public static final String FOLDERS = "folders";
    public static final String FIELDS = "template-fields";
    public static final String ELEMENTS = "template-elements";
    public static final String TEMPLATES = "templates";
    //TODO: this should be removed once we don't need it in the frontend and the validation
    public static final String ELEMENT_INSTANCES = "template-element-instances";
    public static final String INSTANCES = "template-instances";
    public static final String USERS = "users";
    public static final String GROUPS = "groups";
    public static final String MESSAGES = "messages";
    public static final String USERMESSAGES = "user-messages";
    public static final String PROCESS_TYPES = "process-types";
    public static final String CATEGORIES = "categories";
  }

  public static class AtType {
    public static final String AT_TYPE_PREFIX = "https://schema.metadatacenter.org/core/";
    public static final String FIELD = AT_TYPE_PREFIX + "TemplateField";
    public static final String ELEMENT = AT_TYPE_PREFIX + "TemplateElement";
    public static final String TEMPLATE = AT_TYPE_PREFIX + "Template";
  }

  private final String value;
  private final String prefix;
  private final String atType;

  CedarArtifactType(String value, String prefix, String atType) {
    this.value = value;
    this.prefix = prefix;
    this.atType = atType;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getAtType() {
    return atType;
  }

  public static CedarArtifactType forValue(String type) {
    for (CedarArtifactType t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  public static CedarArtifactType forAtType(String atType) {
    if (atType != null) {
      for (CedarArtifactType t : values()) {
        if (atType.equals(t.getAtType())) {
          return t;
        }
      }
    }
    return null;
  }
}
