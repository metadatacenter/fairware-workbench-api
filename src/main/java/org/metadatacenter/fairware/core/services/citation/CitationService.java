package org.metadatacenter.fairware.core.services.citation;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.search.SearchMetadataItem;
import org.metadatacenter.fairware.core.util.DoiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CitationService {

  private static final Logger logger = LoggerFactory.getLogger(CitationService.class);
  private DataCiteService dataCiteService;

  public CitationService(DataCiteService dataCiteService) {
    this.dataCiteService = dataCiteService;
  }

//  public List<SearchMetadataItem> searchMetadata(List<String> dois) throws IOException, HttpException {
//    List<SearchMetadataItem> results = new ArrayList<>();
//    for (String doi : dois) {
//      String normalizedDoi = DoiUtil.normalizeDoi(doi);
//      results.add(dataCiteService.retrieveDoiMetadata(normalizedDoi));
//    }
//    return results;
//  }

  public SearchMetadataItem searchMetadata(String doi) throws IOException, HttpException {
    return dataCiteService.retrieveDoiMetadata(DoiUtil.normalizeDoi(doi));
  }


}