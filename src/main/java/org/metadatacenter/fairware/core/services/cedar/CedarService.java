package org.metadatacenter.fairware.core.services.cedar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.metadatacenter.fairware.api.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.response.search.SearchMetadataItem;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.metadatacenter.fairware.constants.CedarConstants;
import org.metadatacenter.fairware.constants.CedarModelConstants;
import org.metadatacenter.fairware.core.services.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class CedarService {

  private static final Logger logger = LoggerFactory.getLogger(CedarService.class);

  private final CedarConfig cedarConfig;
  private final ObjectMapper objectMapper;
  private final HttpRequestHandler requestHandler;

  public CedarService(@Nonnull CedarConfig cedarConfig,
                      @Nonnull ObjectMapper objectMapper,
                      @Nonnull HttpRequestHandler requestHandler) {
    this.cedarConfig = checkNotNull(cedarConfig);
    this.objectMapper = checkNotNull(objectMapper);
    this.requestHandler = checkNotNull(requestHandler);
  }

  /**
   * Find CEDAR template by its identifier.
   *
   * @param id the template identifier
   * @return a CEDAR template
   */
  @Nonnull
  public ImmutableMap<String, Object> findTemplate(String id) throws IOException, HttpException {
    String url = getTemplateUrl(id);
    Request request = requestHandler.createGetRequest(url, "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = requestHandler.execute(request);
    switch (response.getStatusLine().getStatusCode()) {
      case HttpStatus.SC_OK:
        return objectMapper.readValue(
            response.getEntity().getContent(),
            ImmutableMap.class);
      case HttpStatus.SC_NOT_FOUND:
        throw new HttpException(format(
            "Couldn't find CEDAR template (ID = %s). Cause: %s",
            id, response.getStatusLine()));
      default:
        throw new HttpException(format(
            "Error retrieving template (ID = %s). Cause: %s",
            id, response.getStatusLine()));
    }
  }

  private String getTemplateUrl(String id) throws UnsupportedEncodingException {
    return new StringBuilder()
        .append(cedarConfig.getBaseUrl())
        .append(CedarConstants.CEDAR_PATH_TEMPLATES)
        .append(URLEncoder.encode(id, Charsets.UTF_8.toString()))
        .toString();
  }

  /**
   * Find CEDAR template instance by its identifier.
   *
   * @param id the template instance identifier
   * @return a CEDAR instance
   */
  @Nonnull
  public ImmutableMap<String, Object> findTemplateInstance(String id) throws IOException, HttpException {
    String url = getTemplateInstanceUrl(id);
    Request request = requestHandler.createGetRequest(url, "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = requestHandler.execute(request);
    switch (response.getStatusLine().getStatusCode()) {
      case HttpStatus.SC_OK:
        return objectMapper.readValue(
            response.getEntity().getContent(),
            ImmutableMap.class);
      case HttpStatus.SC_NOT_FOUND:
        throw new HttpException(format(
            "Couldn't find CEDAR template instance (ID = %s). Cause: %s",
            id, response.getStatusLine()));
      default:
        throw new HttpException(format(
            "Error retrieving template instance (ID = %s). Cause: %s",
            id, response.getStatusLine()));
    }
  }

  @Nonnull
  private String getTemplateInstanceUrl(String id) throws UnsupportedEncodingException {
    return new StringBuilder()
        .append(cedarConfig.getBaseUrl())
        .append(CedarConstants.CEDAR_PATH_TEMPLATE_INSTANCES)
        .append(URLEncoder.encode(id, StandardCharsets.UTF_8.toString()))
        .toString();
  }

  @Nonnull
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
   */
  @Nonnull
  public RecommendTemplatesResponse recommendTemplates(ImmutableMap<String, Object> metadataRecord) throws IOException, HttpException {
    String url = getRecommendTemplateUrl();
    Request request = requestHandler.createPostRequest(url, metadataRecord, "apiKey " + cedarConfig.getApiKey());
    HttpResponse response = requestHandler.execute(request);
    switch (response.getStatusLine().getStatusCode()) {
      case HttpStatus.SC_OK:
        InputStream content = response.getEntity().getContent();
        return objectMapper.readValue(content, RecommendTemplatesResponse.class);
      case HttpStatus.SC_NOT_FOUND:
        throw new HttpException(format(
            "Couldn't find recommended templates. Cause: %s",
            response.getStatusLine()));
      default:
        throw new HttpException(format(
            "Error retrieving recommended templates. Cause: %s",
            response.getStatusLine()));
    }
  }

  private String getRecommendTemplateUrl() {
    return new StringBuilder()
        .append(cedarConfig.getBaseUrl())
        .append(CedarConstants.CEDAR_PATH_RECOMMEND_TEMPLATES)
        .toString();
  }
}
