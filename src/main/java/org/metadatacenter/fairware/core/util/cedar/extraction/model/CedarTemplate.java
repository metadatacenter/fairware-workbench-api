package org.metadatacenter.fairware.core.util.cedar.extraction.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateFieldsExtractor;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class CedarTemplate {

  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());

  private final ImmutableMap<String, Object> template;
  private final CedarTemplateFieldsExtractor templateFieldsExtractor;

  public CedarTemplate(@Nonnull ImmutableMap<String, Object> template,
                       @Nonnull CedarTemplateFieldsExtractor templateFieldsExtractor) {
    this.template = checkNotNull(template);
    this.templateFieldsExtractor = checkNotNull(templateFieldsExtractor);
  }

  public ImmutableList<TemplateField> getTemplateFields() {
    var templateNode = objectMapper.valueToTree(template);
    return templateFieldsExtractor.extractFields(templateNode);
  }
}
