package org.metadatacenter.fairware.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.request.CedarRecommendTemplatesRequest;
import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response.CedarRecommendTemplatesResponse;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CedarService {

  private static final Logger logger = LoggerFactory.getLogger(CedarService.class);
  private final CedarConfig cedarConfig;
  // TODO reuse ObjectMapper
  private final ObjectMapper mapper = new ObjectMapper();

  public CedarService(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
  }

  public CedarRecommendTemplatesResponse recommendTemplates(CedarRecommendTemplatesRequest rcRequest)
      throws IOException, HttpException {

    Request request = Request.Post(cedarConfig.getResourceServer().getRecommendTemplatesUrl())
        .bodyString(mapper.writeValueAsString(rcRequest), ContentType.APPLICATION_JSON)
        .addHeader("Authorization", "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      CedarRecommendTemplatesResponse cedarResponse = mapper.
          readValue(response.getEntity().getContent(), CedarRecommendTemplatesResponse.class);
      return cedarResponse;
    } else {
      throw new HttpException("Error connecting CEDAR: " + response.getStatusLine());
    }
  }
}
