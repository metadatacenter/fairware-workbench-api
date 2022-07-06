package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableMap;
import org.metadatacenter.fairware.api.response.recommendation.RecommendTemplatesResponse;
import org.metadatacenter.fairware.core.services.cedar.CedarService;

import java.io.IOException;

public class TemplateService {

  private final CedarService cedarService;

  public TemplateService(CedarService cedarService) {
    this.cedarService = cedarService;
  }

  public RecommendTemplatesResponse recommendCedarTemplates(ImmutableMap<String, Object> metadataRecord) throws IOException {
    return cedarService.recommendTemplates(metadataRecord);
  }
}
