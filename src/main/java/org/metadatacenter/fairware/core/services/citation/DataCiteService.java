package org.metadatacenter.fairware.core.services.citation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.fairware.api.response.search.SearchMetadataItem;
import org.metadatacenter.fairware.config.citationServices.datacite.DataCiteConfig;
import org.metadatacenter.fairware.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class DataCiteService {

  private static final Logger logger = LoggerFactory.getLogger(DataCiteService.class);
  private final String doisUrl;
  private final ObjectMapper objectMapper;

  public DataCiteService(DataCiteConfig dataCiteConfig) {
    this.doisUrl = dataCiteConfig.getDoisUrl();
    this.objectMapper = new ObjectMapper();
  }

  public SearchMetadataItem retrieveDoiMetadata(String doi) throws IOException, HttpException {
    logger.info("Retrieving DataCite DOI metadata: " + doi);
    String url = doisUrl + doi;
    Request request = Request.Get(url);
    HttpResponse response = request.execute().returnResponse();

    logger.info("Response code: " + response.getStatusLine().getStatusCode());
    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      JsonNode result = objectMapper.readTree(new String(EntityUtils.toByteArray(response.getEntity())));
      final JsonNode data = JsonUtil.getJsonNodeValue(result, "data");
      final JsonNode attributes = JsonUtil.getJsonNodeValue(data, "attributes");
      final String doiFound = JsonUtil.getStringValue(attributes, "doi");
      final String schemaVersion = JsonUtil.getStringValue(attributes, "schemaVersion");
      final JsonNode titles = JsonUtil.getJsonNodeValue(attributes, "titles");
      String title = null;
      for (JsonNode titleNode : titles) {
        title = JsonUtil.getStringValue(titleNode, "title");
        break;
      }
      Map<String, Object> metadataMap =
          objectMapper.convertValue(attributes, new TypeReference<Map<String, Object>>() {});
      return new SearchMetadataItem(doiFound, "DataCite", title, schemaVersion, metadataMap);
    }
    else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
      return new SearchMetadataItem(doi, null, null, null, null);
    }
    else {
      throw new HttpException("DataCite API error: " + response.getStatusLine());
    }
  }
}
