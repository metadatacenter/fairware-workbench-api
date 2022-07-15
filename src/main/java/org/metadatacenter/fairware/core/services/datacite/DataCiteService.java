package org.metadatacenter.fairware.core.services.datacite;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.fairware.core.services.MetadataServiceProvider;
import org.metadatacenter.fairware.shared.Metadata;
import org.metadatacenter.fairware.config.citationServices.datacite.DataCiteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class DataCiteService implements MetadataServiceProvider {

  private static final Logger logger = LoggerFactory.getLogger(DataCiteService.class);
  private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());
  private static final Pattern doiPattern = Pattern.compile("^.*(10\\.[A-Za-z0-9.\\/-]+).*$");

  private final DataCiteConfig dataCiteConfig;

  public DataCiteService(@Nonnull DataCiteConfig dataCiteConfig) {
    this.dataCiteConfig = checkNotNull(dataCiteConfig);
  }

  @Override
  public boolean isCompatible(@Nonnull String metadataId) {
    return doiPattern.matcher(metadataId).find();
  }

  private Optional<String> extractDoi(String inputString) {
    var matcher = doiPattern.matcher(inputString);
    if (matcher.find()) {
      var doi = matcher.group(1);
      return Optional.of(doi);
    } else {
      return Optional.empty();
    }
  }

  @Override
  @Nonnull
  public Metadata getMetadataById(String metadataId) throws IOException {
    var doi = extractDoi(metadataId);
    if (doi.isPresent()) {
      logger.info("Retrieving DataCite DOI metadata: " + doi.get());
      var url = dataCiteConfig.getDoisUrl() + doi.get();
      var request = Request.Get(url);
      var response = request.execute().returnResponse();
      var statusCode = response.getStatusLine().getStatusCode();
      logger.info("Response code: " + statusCode);
      switch (statusCode) {
        case HttpStatus.SC_OK:
          var metadata = objectMapper.readTree(new String(EntityUtils.toByteArray(response.getEntity())));
          var content = metadata.get("data").get("attributes");
          var metadataRecord = flattenObject(content);  // TODO: Support nested fields
          var metadataName = content.get("titles").get(0).get("title").asText();
          var metadataFields = metadataRecord.keySet();
          return Metadata.create(metadataId, metadataName, metadataFields, metadataRecord);
        case HttpStatus.SC_NOT_FOUND:
          throw new FileNotFoundException(String.format("Unable to retrieve DataCite DOI metadata: %s", url));
        default:
          throw new IOException(String.format("DataCite API error: %s", response.getStatusLine()));
      }
    }
    throw new BadRequestException(String.format("Illegal DataCite DOI: %s", metadataId));
  }

  private ImmutableMap<String, Object> flattenObject(JsonNode root) {
    var flatMap = Maps.<String, Object>newHashMap();
    var fieldIterator = root.fieldNames();
    fieldIterator.forEachRemaining(fieldName -> {
      var value = root.get(fieldName);
      if (!value.isContainerNode()) {
        if (value.isTextual()) {
          flatMap.put(fieldName, value.textValue());
        } else if (value.isNumber()) {
          flatMap.put(fieldName, value.numberValue());
        } else if (value.isBoolean()) {
          flatMap.put(fieldName, value.booleanValue());
        } else if (value.isNull()) {
          flatMap.put(fieldName, Optional.empty());
        }
      }
    });
    return ImmutableMap.copyOf(flatMap);
  }
}
