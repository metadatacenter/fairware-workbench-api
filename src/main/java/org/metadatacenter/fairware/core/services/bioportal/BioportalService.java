package org.metadatacenter.fairware.core.services.bioportal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpClass;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpPagedResults;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

public class BioportalService {

  private static final Logger logger = LoggerFactory.getLogger(BioportalService.class);
  private final BioportalConfig bioportalConfig;
  private final ObjectMapper objectMapper;

  public BioportalService(BioportalConfig bioportalConfig) {
    this.bioportalConfig = bioportalConfig;
    this.objectMapper = new ObjectMapper();
  }

  /**
   * Search for ontology classes
   */
  public BpPagedResults<BpClass> search(String q, int page, int pageSize) throws IOException, HttpException {

    q = GeneralUtil.encodeIfNeeded(q);
    String url = bioportalConfig.getSearchUrl() + "?q=" + q;

    // Include additional information
    url += "&include=prefLabel,definition";

    // Add pagination parameters
    url += "&page=" + page + "&pagesize=" + pageSize;

    logger.info("Bioportal search url: " + url);

    // Send request to the BioPortal API
    Request request = Request.Get(url).addHeader("Authorization", getBioPortalAuthHeader(bioportalConfig.getApiKey())).
        connectTimeout(bioportalConfig.getConnectTimeout()).socketTimeout(bioportalConfig.getSocketTimeout());
    HttpResponse response = request.execute().returnResponse();

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      JsonNode bpResult = objectMapper.readTree(new String(EntityUtils.toByteArray(response.getEntity())));
      return objectMapper.readValue(objectMapper.treeAsTokens(bpResult), new TypeReference<BpPagedResults<BpClass>>() {
      });
    } else {
      throw new HttpException("BioPortal API error: " + response.getStatusLine());
    }
  }

  private static String getBioPortalAuthHeader(String apikey) {
    return "apikey token=" + apikey;
  }


}
