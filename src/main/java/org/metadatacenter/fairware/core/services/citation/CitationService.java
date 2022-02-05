package org.metadatacenter.fairware.core.services.citation;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.search.SearchMetadataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitationService {

  private static final Logger logger = LoggerFactory.getLogger(CitationService.class);
  private DataCiteService dataCiteService;

  public CitationService(DataCiteService dataCiteService) {
    this.dataCiteService = dataCiteService;
  }

  public List<SearchMetadataItem> searchMetadata(List<String> dois) throws IOException, HttpException {
    List<SearchMetadataItem> results = new ArrayList<>();
    for (String doi : dois) {
      results.add(dataCiteService.retrieveDoiMetadata(doi));
    }
    return results;
  }
}
