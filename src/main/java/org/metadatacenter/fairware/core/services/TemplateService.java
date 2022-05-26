package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.core.services.cedar.CedarService;

import java.io.IOException;
import java.util.Map;

public class TemplateService {

  private final CedarService cedarService;

  public TemplateService(CedarService cedarService) {
    this.cedarService = cedarService;
  }

  public RecommendTemplatesResponse recommendCedarTemplates(Map<String, Object> metadataRecord) throws IOException {
    return cedarService.recommendTemplates(ImmutableMap.copyOf(metadataRecord));
  }
}
