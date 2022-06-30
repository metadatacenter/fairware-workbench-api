package org.metadatacenter.fairware.core.services.biosample;

import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.fairware.api.response.search.MetadataIndex;
import org.metadatacenter.fairware.config.citationServices.NcbiConfig;
import org.metadatacenter.fairware.core.services.citation.CitationServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class BioSampleService implements CitationServiceProvider {

  private static final Logger logger = LoggerFactory.getLogger(BioSampleService.class);

  private static final Pattern bioSampleIdPattern = Pattern.compile("^SAMN\\d+$");

  private final NcbiConfig ncbiConfig;
  private final BioSampleDataParser bioSampleDataParser;

  public BioSampleService(@Nonnull NcbiConfig ncbiConfig,
                          @Nonnull BioSampleDataParser bioSampleDataParser) {
    this.ncbiConfig = checkNotNull(ncbiConfig);
    this.bioSampleDataParser = checkNotNull(bioSampleDataParser);
  }

  @Override
  public boolean isCompatible(@Nonnull String metadataRecordId) {
    return bioSampleIdPattern.matcher(metadataRecordId).find();
  }

  @Nonnull
  @Override
  public MetadataIndex getMetadataIndex(@Nonnull String metadataRecordId) throws IOException {
    logger.info("Retrieving BioSample metadata: " + metadataRecordId);
    var url = getFetchUrl(metadataRecordId);
    var request = Request.Get(url);
    var response = request.execute().returnResponse();
    var statusCode = response.getStatusLine().getStatusCode();
    logger.info("Response code: " + statusCode);
    switch (statusCode) {
      case HttpStatus.SC_OK:
        var responseText = new String(EntityUtils.toByteArray(response.getEntity()));
        var metadataRecord = bioSampleDataParser.parseToMap(responseText);
        var metadataName = metadataRecordId;
        return MetadataIndex.create(metadataRecordId, metadataName, metadataRecord);
      case HttpStatus.SC_NOT_FOUND:
        throw new FileNotFoundException(String.format("Unable to retrieve BioSample metadata: %s", url));
      default:
        throw new IOException(String.format("BioSample API error: %s", response.getStatusLine()));
    }
  }

  private String getFetchUrl(String metadataRecordId) {
    var sb = new StringBuilder();
    sb.append(ncbiConfig.getRootUrl());
    sb.append("?db=biosample");
    sb.append("&id=").append(metadataRecordId);
    return sb.toString();
  }
}
