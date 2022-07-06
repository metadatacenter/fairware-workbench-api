package org.metadatacenter.fairware.core.services.cedar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.fairware.api.response.recommendation.RecommendTemplatesResponse;
import org.metadatacenter.fairware.shared.Metadata;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.metadatacenter.fairware.constants.CedarConstants;
import org.metadatacenter.fairware.core.services.HttpRequestHandler;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateFieldsExtractor;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.CedarTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.metadatacenter.fairware.constants.CedarModelConstants.SCHEMA_ORG_NAME;

public class CedarService {

  private static final Logger logger = LoggerFactory.getLogger(CedarService.class);

  private final CedarConfig cedarConfig;
  private final ObjectMapper objectMapper;
  private final HttpRequestHandler requestHandler;
  private final CedarTemplateFieldsExtractor cedarTemplateFieldsExtractor;

  public CedarService(@Nonnull CedarConfig cedarConfig,
                      @Nonnull ObjectMapper objectMapper,
                      @Nonnull HttpRequestHandler requestHandler,
                      @Nonnull CedarTemplateFieldsExtractor cedarTemplateFieldsExtractor) {
    this.cedarConfig = checkNotNull(cedarConfig);
    this.objectMapper = checkNotNull(objectMapper);
    this.requestHandler = checkNotNull(requestHandler);
    this.cedarTemplateFieldsExtractor = checkNotNull(cedarTemplateFieldsExtractor);
  }

  /**
   * Find CEDAR template by its identifier.
   *
   * @param id the template identifier
   * @return a CEDAR template
   */
  @Nonnull
  public ImmutableMap<String, Object> findTemplate(String id) throws IOException {
    var url = getTemplateUrl(id);
    var request = requestHandler.createGetRequest(url, "apiKey " + cedarConfig.getApiKey());
    var response = requestHandler.execute(request);
    switch (response.getStatusLine().getStatusCode()) {
      case HttpStatus.SC_OK:
        var content = objectMapper.readTree(new String(EntityUtils.toByteArray(response.getEntity())));
        var mapType = objectMapper.getTypeFactory().constructMapType(ImmutableMap.class, String.class, Object.class);
        return objectMapper.<ImmutableMap<String, Object>>convertValue(content, mapType);
      case HttpStatus.SC_NOT_FOUND:
        throw new FileNotFoundException(format(
            "Couldn't find CEDAR template (ID = %s). Cause: %s",
            id, response.getStatusLine()));
      default:
        throw new BadRequestException(format(
            "Error retrieving template (ID = %s). Cause: %s",
            id, response.getStatusLine()));
    }
  }

  @Nonnull
  public CedarTemplate retrieveCedarTemplate(String id) throws IOException {
    var templateObject = findTemplate(id);
    return new CedarTemplate(templateObject, cedarTemplateFieldsExtractor);
  }

  private String getTemplateUrl(String id) throws UnsupportedEncodingException {
    return new StringBuilder()
        .append(cedarConfig.getBaseUrl())
        .append(CedarConstants.CEDAR_PATH_TEMPLATES)
        .append(URLEncoder.encode(id, Charsets.UTF_8.toString()))
        .toString();
  }

  /**
   * Get the metadata record from CEDAR given its identifier.
   *
   * @param cedarTemplateInstanceId the CEDAR template instance identifier.
   * @return a {@link Metadata} of the CEDAR template instance.
   */
  @Nonnull
  public Metadata getMetadataById(String cedarTemplateInstanceId) throws IOException {
    var url = getTemplateInstanceUrl(cedarTemplateInstanceId);
    var request = requestHandler.createGetRequest(url, "apiKey " + cedarConfig.getApiKey());
    var response = requestHandler.execute(request);
    switch (response.getStatusLine().getStatusCode()) {
      case HttpStatus.SC_OK:
        var content = objectMapper.readTree(new String(EntityUtils.toByteArray(response.getEntity())));
        var mapType = objectMapper.getTypeFactory().constructMapType(ImmutableMap.class, String.class, Object.class);
        var metadataRecord = objectMapper.<ImmutableMap<String, Object>>convertValue(content, mapType);
        var metadataName = (String) metadataRecord.getOrDefault(SCHEMA_ORG_NAME, "");
        var metadataFields = metadataRecord.keySet(); // TODO: Support nested fields
        return Metadata.create(cedarTemplateInstanceId, metadataName, metadataFields, metadataRecord);
      case HttpStatus.SC_NOT_FOUND:
        throw new FileNotFoundException(format(
            "Couldn't find CEDAR template instance (ID = %s). Cause: %s",
            cedarTemplateInstanceId, response.getStatusLine()));
      default:
        throw new BadRequestException(format(
            "Error retrieving template instance (ID = %s). Cause: %s",
            cedarTemplateInstanceId, response.getStatusLine()));
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

  /**
   * Makes a call to CEDAR's template recommendation endpoint to retrieve a ranked list of recommended templates for
   * the given input metadata record
   *
   * @param metadataRecord the input metadata record
   * @return a ranked list of recommended CEDAR templates
   */
  @Nonnull
  public RecommendTemplatesResponse recommendTemplates(ImmutableMap<String, Object> metadataRecord) throws IOException {
    var url = getRecommendTemplateUrl();
    var request = requestHandler.createPostRequest(url, metadataRecord, "apiKey " + cedarConfig.getApiKey());
    var response = requestHandler.execute(request);
    switch (response.getStatusLine().getStatusCode()) {
      case HttpStatus.SC_OK:
        var content = response.getEntity().getContent();
        return objectMapper.readValue(content, RecommendTemplatesResponse.class);
      case HttpStatus.SC_NOT_FOUND:
        throw new FileNotFoundException(format(
            "Couldn't find recommended templates. Cause: %s",
            response.getStatusLine()));
      default:
        throw new BadRequestException(format(
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
