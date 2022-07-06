package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateFieldsExtractor;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.metadatacenter.fairware.constants.CedarModelConstants.JSON_LD_ID;
import static org.metadatacenter.fairware.constants.CedarModelConstants.SCHEMA_ORG_NAME;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class CedarTemplate {

  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());

  private final ImmutableMap<String, Object> template;
  private final CedarTemplateFieldsExtractor templateFieldsExtractor;

  private JsonNode templateNode = null;

  public CedarTemplate(@Nonnull ImmutableMap<String, Object> template,
                       @Nonnull CedarTemplateFieldsExtractor templateFieldsExtractor) {
    this.template = checkNotNull(template);
    this.templateFieldsExtractor = checkNotNull(templateFieldsExtractor);
  }

  public String getName() {
    var templateNode = getTemplateNode();
    return templateNode.get(SCHEMA_ORG_NAME).asText();
  }

  public String getId() {
    var templateNode = getTemplateNode();
    return templateNode.get(JSON_LD_ID).asText();
  }

  public ImmutableList<TemplateField> getFields() {
    var templateNode = getTemplateNode();
    return templateFieldsExtractor.extractFields(templateNode);
  }

  public ImmutableMap<String, Object> asMap() {
    return template;
  }

  private JsonNode getTemplateNode() {
    if (templateNode == null) {
      templateNode = objectMapper.valueToTree(template);
    }
    return templateNode;
  }
}
