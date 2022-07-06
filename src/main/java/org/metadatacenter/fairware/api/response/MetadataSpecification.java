package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@AutoValue
public abstract class MetadataSpecification {

  private static final String TEMPLATE_ID = "templateId";
  private static final String TEMPLATE_NAME = "templateName";
  private static final String TEMPLATE_URL = "templateUrl";
  private static final String TEMPLATE_FIELDS = "templateFields";

  @Nonnull
  @JsonCreator
  public static MetadataSpecification create(@Nonnull @JsonProperty(TEMPLATE_ID) String templateId,
                                             @Nonnull @JsonProperty(TEMPLATE_NAME) String templateName,
                                             @Nonnull @JsonProperty(TEMPLATE_FIELDS) ImmutableMap<String, String> templateFields) {
    return new AutoValue_MetadataSpecification(templateId, templateName, templateFields);
  }

  @Nonnull
  @JsonProperty(TEMPLATE_ID)
  public abstract String getTemplateId();

  @Nonnull
  @JsonProperty(TEMPLATE_NAME)
  public abstract String getTemplateName();

  @Nonnull
  @JsonProperty(TEMPLATE_URL)
  public String getTemplateUrl() {
    return "https://cedar.metadatacenter.org/templates/edit/" + getTemplateId();
  }

  @Nonnull
  @JsonProperty(TEMPLATE_FIELDS)
  public abstract ImmutableMap<String, String> getTemplateFields();
}
