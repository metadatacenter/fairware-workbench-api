package org.metadatacenter.fairware.core.services.cedar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.metadatacenter.fairware.api.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.response.search.SearchMetadataItem;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.metadatacenter.fairware.constants.CedarConstants;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CedarService {

  private static final Logger logger = LoggerFactory.getLogger(CedarService.class);
  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());
  private final CedarConfig cedarConfig;

  public CedarService(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
  }

  /**
   * Find CEDAR template by id
   *
   * @param id the template identifier
   * @return a CEDAR template
   * @throws IOException
   * @throws HttpException
   */
  public Map<String, Object> findTemplate(String id) throws IOException, HttpException {
    String url = cedarConfig.getBaseUrl() + CedarConstants.CEDAR_PATH_TEMPLATES +
        URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
    Request request = Request.Get(url).addHeader("Authorization", "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      return objectMapper.readValue(
          response.getEntity().getContent(),
          new TypeReference<HashMap<String, Object>>() {
          });
    } else {
      throw new HttpException("Couldn't find CEDAR template (templateId = " + id + "). Cause: "
          + response.getStatusLine());
    }
  }

  /**
   * Find CEDAR template instance by id
   *
   * @param id the template instance identifier
   * @return a CEDAR instance
   * @throws IOException
   * @throws HttpException
   */
  public ImmutableMap<String, Object> findTemplateInstance(String id) throws IOException, HttpException {
    String url = cedarConfig.getBaseUrl() + CedarConstants.CEDAR_PATH_TEMPLATE_INSTANCES +
        URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
    Request request = Request.Get(url).addHeader("Authorization", "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      return objectMapper.readValue(
          response.getEntity().getContent(),
          new TypeReference<ImmutableMap<String, Object>>() {
          });
    } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
      logger.info("Template instance not found (id = " + id + ")");
      return null;
    } else { // Error
      throw new HttpException("Error retrieving template instance (id = " + id + "). Cause: "
          + response.getStatusLine());
    }
  }

  public SearchMetadataItem toMetadataItem(ImmutableMap<String, Object> templateInstance) throws HttpException, IOException {
    String uri = templateInstance.get(CedarModelConstants.JSON_LD_ID).toString();
    String source = CedarConstants.CEDAR_SYSTEM_NAME;
    String name = templateInstance.get(CedarModelConstants.SCHEMA_ORG_NAME).toString();
    String schemaId = templateInstance.get(CedarModelConstants.IS_BASED_ON).toString();
    String schemaName = findTemplate(schemaId).get(CedarModelConstants.SCHEMA_ORG_NAME).toString();
    return SearchMetadataItem.create(uri, source, name, schemaId, schemaName, templateInstance);
  }

  /**
   * Makes a call to CEDAR's template recommendation endpoint to retrieve a ranked list of recommended templates for
   * the given input metadata record
   *
   * @param metadataRecord the input metadata record
   * @return a ranked list of recommended CEDAR templates
   * @throws IOException
   * @throws HttpException
   */
  public RecommendTemplatesResponse recommendTemplates(Map<String, Object> metadataRecord)
      throws IOException, HttpException {

    String url = cedarConfig.getBaseUrl() + CedarConstants.CEDAR_PATH_RECOMMEND_TEMPLATES;
    RecommendTemplatesRequest payload = new RecommendTemplatesRequest(metadataRecord);
    Request request = Request.Post(url)
        .bodyString(objectMapper.writeValueAsString(payload), ContentType.APPLICATION_JSON)
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
