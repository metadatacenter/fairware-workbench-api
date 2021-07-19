package org.metadatacenter.fairware.core;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response.CedarRecommendTemplatesResponse;
import org.metadatacenter.fairware.api.recommendation.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.recommendation.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.core.util.CedarService;
import org.metadatacenter.fairware.core.util.ObjectConverter;

import java.io.IOException;

public class MetadataEvaluationService implements IMetadataEvaluationService {

  private final CedarService cedarService;

  public MetadataEvaluationService(CedarService cedarService) {
    this.cedarService = cedarService;
  }

  @Override
  public RecommendTemplatesResponse recommendCedarTemplates(RecommendTemplatesRequest request) throws IOException,
      HttpException {

    CedarRecommendTemplatesResponse cedarResponse =
        cedarService.recommendTemplates(ObjectConverter.recTempRequestToCedarRecTempRequest(request));
    return ObjectConverter.cedarRecTempResponseToRecTempResponse(cedarResponse);

  }
}
