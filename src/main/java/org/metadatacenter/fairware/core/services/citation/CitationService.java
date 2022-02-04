package org.metadatacenter.fairware.core.services.citation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class CitationService {

  private static final Logger logger = LoggerFactory.getLogger(CitationService.class);
  private DataCiteService dataCiteService;

  public CitationService(DataCiteService dataCiteService) {
    this.dataCiteService = dataCiteService;
  }

  public void searchMetadata(List<String> dois) throws IOException {
    for (String doi : dois) {
      dataCiteService.retrieveDoiMetadata(doi);
    }
  }
}
