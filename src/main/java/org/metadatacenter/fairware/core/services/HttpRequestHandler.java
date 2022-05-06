package org.metadatacenter.fairware.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class HttpRequestHandler {

  private final ObjectMapper objectMapper;

  public HttpRequestHandler(@Nonnull ObjectMapper objectMapper) {
    this.objectMapper = checkNotNull(objectMapper);
  }

  @Nonnull
  public Request createGetRequest(String uri) {
    return createGetRequest(uri, Optional.empty());
  }

  @Nonnull
  public Request createGetRequest(String uri, String apiKey) {
    return createGetRequest(uri, Optional.of(apiKey));
  }

  @Nonnull
  public Request createGetRequest(String uri, Optional<String> apiKey) {
    var request = Request.Get(uri);
    request = addAuthorization(apiKey, request);
    return request;
  }

  @Nonnull
  public Request createPostRequest(String uri, ImmutableMap<String, Object> payload)
      throws JsonProcessingException {
    return createPostRequest(uri, payload, Optional.empty());
  }

  @Nonnull
  public Request createPostRequest(String uri, ImmutableMap<String, Object> payload, String apiKey)
      throws JsonProcessingException {
    return createPostRequest(uri, payload, Optional.of(apiKey));
  }

  @Nonnull
  public Request createPostRequest(String uri, ImmutableMap<String, Object> payload, Optional<String> apiKey)
      throws JsonProcessingException {
    var request = Request.Post(uri)
        .bodyString(objectMapper.writeValueAsString(payload), ContentType.APPLICATION_JSON);
    request = addAuthorization(apiKey, request);
    return request;
  }

  private Request addAuthorization(Optional<String> apiKey, Request request) {
    if (apiKey.isPresent()) {
      request = request.addHeader("Authorization", apiKey.get());
    }
    return request;
  }

  @Nonnull
  public HttpResponse execute(Request request) throws IOException {
    return request.execute().returnResponse();
  }
}
