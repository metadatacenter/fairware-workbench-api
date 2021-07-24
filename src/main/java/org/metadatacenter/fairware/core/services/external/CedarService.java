package org.metadatacenter.fairware.core.services.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.metadatacenter.fairware.api.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CedarService {

  private static final Logger logger = LoggerFactory.getLogger(CedarService.class);
  private final CedarConfig cedarConfig;
  private final ObjectMapper objectMapper;

  public CedarService(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
    // TODO find better place to define and reuse ObjectMapper
    objectMapper = new ObjectMapper();
  }

  public Map<String, Object> findTemplate(String templateId) throws IOException, HttpException {
    templateId = URLEncoder.encode(templateId, StandardCharsets.UTF_8.toString());
    String url = cedarConfig.getResourceServer().getTemplatesUrl() + templateId;
    Request request = Request.Get(url).addHeader("Authorization", "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      Map<String, Object> templateMap = objectMapper.readValue(
          response.getEntity().getContent(),
          new TypeReference<HashMap<String,Object>>(){});
      return templateMap;
    } else {
      throw new HttpException("Error connecting to CEDAR: " + response.getStatusLine());
    }
  }

  public RecommendTemplatesResponse recommendTemplates(Map<String, Object> metadataRecord)
      throws IOException, HttpException {

    RecommendTemplatesRequest cedarRequest = new RecommendTemplatesRequest(metadataRecord);

    Request request = Request.Post(cedarConfig.getResourceServer().getRecommendTemplatesUrl())
        .bodyString(objectMapper.writeValueAsString(cedarRequest), ContentType.APPLICATION_JSON)
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
