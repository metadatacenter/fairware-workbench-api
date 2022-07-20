package org.metadatacenter.fairware.core.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.metadatacenter.fairware.shared.Metadata;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class HttpJsonFileService implements MetadataServiceProvider {

  private static final Pattern httpJsonFile = Pattern.compile("^https?:\\/\\/.*\\.json$");

  private final HttpJsonFileParser httpJsonFileParser;

  public HttpJsonFileService(@Nonnull HttpJsonFileParser httpJsonFileParser) {
    this.httpJsonFileParser = checkNotNull(httpJsonFileParser);
  }

  @Override
  public boolean isCompatible(@Nonnull String metadataId) {
    return httpJsonFile.matcher(metadataId).find();
  }

  @Nonnull
  @Override
  public Metadata getMetadataById(@Nonnull String metadataId) throws IOException {
    var metadataUrl = new URL(metadataId);
    try (var in = metadataUrl.openStream()) {
      var metadataString = new String(in.readAllBytes(), StandardCharsets.UTF_8);
      return httpJsonFileParser.parse(metadataString, metadataId);
    }
  }
}
