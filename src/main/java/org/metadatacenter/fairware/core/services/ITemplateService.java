package org.metadatacenter.fairware.core.services;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;

import java.io.IOException;
import java.util.Map;

public interface ITemplateService {

  RecommendTemplatesResponse recommendCedarTemplates(Map<String, Object> metadataRecord) throws IOException, HttpException;

}
