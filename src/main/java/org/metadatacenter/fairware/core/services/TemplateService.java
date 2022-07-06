package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.api.response.recommendation.RecommendTemplatesResponse;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.core.domain.CedarTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class TemplateService {

  private final CedarService cedarService;

  public TemplateService(@Nonnull CedarService cedarService) {
    this.cedarService = checkNotNull(cedarService);
  }

  public CedarTemplate getCedarTemplateById(String templateId) throws IOException {
    return cedarService.retrieveCedarTemplate(templateId);
  }

  public RecommendTemplatesResponse recommendCedarTemplates(ImmutableMap<String, Object> metadataRecord) throws IOException {
    return cedarService.recommendTemplates(metadataRecord);
  }
}
