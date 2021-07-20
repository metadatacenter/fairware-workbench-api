package org.metadatacenter.fairware.core;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.recommendation.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.recommendation.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.core.util.CedarService;

import java.io.IOException;

public class MetadataEvaluationService implements IMetadataEvaluationService {

  private final CedarService cedarService;

  public MetadataEvaluationService(CedarService cedarService) {
    this.cedarService = cedarService;
  }

  @Override
  public RecommendTemplatesResponse recommendCedarTemplates(RecommendTemplatesRequest request)
      throws IOException, HttpException {

    return cedarService.recommendTemplates(request);

  }
}
