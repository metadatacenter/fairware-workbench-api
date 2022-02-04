package org.metadatacenter.fairware.core.services.citation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.config.citationServices.datacite.DataCiteConfig;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpClass;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpPagedResults;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class DataCiteService {

  private static final Logger logger = LoggerFactory.getLogger(DataCiteService.class);
  private final String doisUrl;
  private final ObjectMapper objectMapper;


  public DataCiteService(DataCiteConfig dataCiteConfig) {
    this.doisUrl = dataCiteConfig.getDoisUrl();
    this.objectMapper = new ObjectMapper();
  }

  public void retrieveDoiMetadata(String doi) throws IOException {
    logger.info("Retrieving DataCite DOI metadata: " + doi);
    String url = doisUrl + doi;
    Request request = Request.Get(url);
    HttpResponse response = request.execute().returnResponse();
    JsonNode result = objectMapper.readTree(new String(EntityUtils.toByteArray(response.getEntity())));
  }
}
