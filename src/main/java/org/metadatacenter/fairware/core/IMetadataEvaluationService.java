package org.metadatacenter.fairware.core;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.recommendation.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.recommendation.response.RecommendTemplatesResponse;

import java.io.IOException;

public interface IMetadataEvaluationService {

  public RecommendTemplatesResponse recommendCedarTemplates(RecommendTemplatesRequest request) throws IOException, HttpException;

}
