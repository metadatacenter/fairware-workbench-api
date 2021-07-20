package org.metadatacenter.fairware.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.metadatacenter.fairware.api.recommendation.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.recommendation.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CedarService {

  private static final Logger logger = LoggerFactory.getLogger(CedarService.class);
  private final CedarConfig cedarConfig;
  private final ObjectMapper objectMapper;

  public CedarService(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
    // TODO find better place to define and reuse ObjectMapper
    objectMapper = new ObjectMapper();
  }

  public RecommendTemplatesResponse recommendTemplates(RecommendTemplatesRequest rcRequest)
      throws IOException, HttpException {

    Request request = Request.Post(cedarConfig.getResourceServer().getRecommendTemplatesUrl())
        .bodyString(objectMapper.writeValueAsString(rcRequest), ContentType.APPLICATION_JSON)
        .addHeader("Authorization", "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      RecommendTemplatesResponse cedarResponse = objectMapper.
          readValue(response.getEntity().getContent(), RecommendTemplatesResponse.class);
      return cedarResponse;
    } else {
      throw new HttpException("Error connecting to CEDAR: " + response.getStatusLine());
    }
  }
}
