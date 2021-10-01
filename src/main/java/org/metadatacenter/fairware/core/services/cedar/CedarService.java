package org.metadatacenter.fairware.core.services.cedar;

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
    objectMapper = new ObjectMapper();
  }

  /**
   * Find CEDAR template by id
   *
   * @param templateId  the template identifier
   * @return  a CEDAR template
   * @throws IOException
   * @throws HttpException
   */
  public Map<String, Object> findTemplate(String templateId) throws IOException, HttpException {
    String url = cedarConfig.getResourceServer().getTemplatesUrl() +
        URLEncoder.encode(templateId, StandardCharsets.UTF_8.toString());
    Request request = Request.Get(url).addHeader("Authorization", "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      return objectMapper.readValue(
          response.getEntity().getContent(),
          new TypeReference<HashMap<String,Object>>(){});
    }
    else {
      throw new HttpException("Couldn't find CEDAR template (templateId = " + templateId + "). Cause: "
          + response.getStatusLine());
    }
  }

  /**
   * Makes a call to CEDAR's template recommendation endpoint to retrieve a ranked list of recommended templates for
   * the given input metadata record
   *
   * @param metadataRecord  the input metadata record
   * @return  a ranked list of recommended CEDAR templates
   * @throws IOException
   * @throws HttpException
   */
  public RecommendTemplatesResponse recommendTemplates(Map<String, Object> metadataRecord)
      throws IOException, HttpException {

    RecommendTemplatesRequest cedarRequest = new RecommendTemplatesRequest(metadataRecord);

    Request request = Request.Post(cedarConfig.getResourceServer().getRecommendTemplatesUrl())
        .bodyString(objectMapper.writeValueAsString(cedarRequest), ContentType.APPLICATION_JSON)
        .addHeader("Authorization", "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      return objectMapper.
          readValue(response.getEntity().getContent(), RecommendTemplatesResponse.class);
    } else {
      throw new HttpException("Error connecting to CEDAR: " + response.getStatusLine());
    }
  }
}
