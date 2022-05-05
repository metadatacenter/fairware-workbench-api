package org.metadatacenter.fairware.core.services.citation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableMap;
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

public class DataCiteService {

  private static final Logger logger = LoggerFactory.getLogger(DataCiteService.class);
  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());
  private final String doisUrl;

  public DataCiteService(DataCiteConfig dataCiteConfig) {
    this.doisUrl = dataCiteConfig.getDoisUrl();
  }

  public SearchMetadataItem retrieveDoiMetadata(String doi) throws IOException, HttpException {
    logger.info("Retrieving DataCite DOI metadata: " + doi);
    String url = doisUrl + doi;
    Request request = Request.Get(url);
    HttpResponse response = request.execute().returnResponse();

    logger.info("Response code: " + response.getStatusLine().getStatusCode());
    switch (response.getStatusLine().getStatusCode()) {
      case HttpStatus.SC_OK:
        JsonNode result = objectMapper.readTree(new String(EntityUtils.toByteArray(response.getEntity())));
        JsonNode data = JsonUtil.getJsonNodeValue(result, "data");
        JsonNode attributes = JsonUtil.getJsonNodeValue(data, "attributes");
        String doiFound = JsonUtil.getStringValue(attributes, "doi");
        String schemaVersion = JsonUtil.getStringValue(attributes, "schemaVersion");
        JsonNode titles = JsonUtil.getJsonNodeValue(attributes, "titles");
        String title = titles.findValues("title").stream().findFirst().get().asText("");
        ImmutableMap<String, Object> metadataMap =
            objectMapper.convertValue(attributes, new TypeReference<ImmutableMap<String, Object>>() {
            });
        return SearchMetadataItem.create(doiFound, "DataCite", title, schemaVersion, "DataCite Schema", metadataMap);
      case HttpStatus.SC_NOT_FOUND:
        throw new HttpException(String.format("Unable to retrieve DataCite DOI metadata: %s (%s)", doi, url));
      default:
        throw new HttpException(String.format("DataCite API error: %s", response.getStatusLine()));
    }
  }
}
