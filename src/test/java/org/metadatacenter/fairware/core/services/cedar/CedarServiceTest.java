package org.metadatacenter.fairware.core.services.cedar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metadatacenter.fairware.config.cedar.CedarConfig;
import org.metadatacenter.fairware.core.services.HttpRequestHandler;
import org.metadatacenter.fairware.core.util.cedar.extraction.CedarTemplateFieldsExtractor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.BadRequestException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@ExtendWith(MockitoExtension.class)
class CedarServiceTest {

  @Mock
  private CedarConfig cedarConfig;
  @Mock
  private ObjectMapper objectMapper;
  @Mock
  private HttpRequestHandler requestHandler;
  @Mock
  private Request request;
  @Mock
  private HttpResponse response;
  @Mock
  private StatusLine statusLine;
  @Mock
  private HttpEntity entity;
  @Mock
  private InputStream inputStream;
  @Mock
  private CedarTemplateFieldsExtractor cedarTemplateFieldsExtractor;

  @BeforeEach
  public void setUp() {
    when(cedarConfig.getBaseUrl()).thenReturn("http://example.org/");
    when(cedarConfig.getApiKey()).thenReturn("abc123");
    when(response.getStatusLine()).thenReturn(statusLine);
  }

  private void mockInputStreamResponse() throws IOException {
    when(response.getEntity()).thenReturn(entity);
    when(entity.getContent()).thenReturn(inputStream);
  }

  @Test
  public void shouldThrowFileNotFoundExceptionWhenTemplateNotFound() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
    when(statusLine.toString()).thenReturn("<404>");

    var thrown = Assertions.assertThrows(FileNotFoundException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler, cedarTemplateFieldsExtractor);
      cedarService.findTemplate("123");
    }, "FileNotFoundException was expected");
    assertThat(thrown.getMessage(), equalTo("Couldn't find CEDAR template (ID = 123). Cause: <404>"));
  }

  @Test
  public void shouldThrowBadRequestExceptionWhenFindingTemplateReturnsBadResponse() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_GATEWAY_TIMEOUT);
    when(statusLine.toString()).thenReturn("<504>");

    var thrown = Assertions.assertThrows(BadRequestException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler, cedarTemplateFieldsExtractor);
      cedarService.findTemplate("123");
    }, "BadRequestException was expected");
    assertThat(thrown.getMessage(), equalTo("Error retrieving template (ID = 123). Cause: <504>"));
  }

  @Test
  public void shouldThrowFileNotFoundExceptionWhenTemplateInstanceNotFound() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
    when(statusLine.toString()).thenReturn("<404>");

    var thrown = Assertions.assertThrows(FileNotFoundException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler, cedarTemplateFieldsExtractor);
      cedarService.getMetadataIndexByCedarId("123");
    }, "FileNotFoundException was expected");
    assertThat(thrown.getMessage(), equalTo("Couldn't find CEDAR template instance (ID = 123). Cause: <404>"));
  }

  @Test
  public void shouldThrowBadRequestExceptionWhenFindingTemplateInstanceReturnsBadResponse() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_GATEWAY);
    when(statusLine.toString()).thenReturn("<502>");

    var thrown = Assertions.assertThrows(BadRequestException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler, cedarTemplateFieldsExtractor);
      cedarService.getMetadataIndexByCedarId("123");
    }, "BadRequestException was expected");
    assertThat(thrown.getMessage(), equalTo("Error retrieving template instance (ID = 123). Cause: <502>"));
  }

  @Test
  @Disabled
  public void shouldFindRecommendedTemplate() throws IOException, IOException {
  }

  @Test
  public void shouldThrowFileNotFoundExceptionWhenFindingRecommendedNotFound() throws IOException {
    var payload = mock(ImmutableMap.class);

    when(requestHandler.createPostRequest(anyString(), eq(payload), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
    when(statusLine.toString()).thenReturn("<404>");

    var thrown = Assertions.assertThrows(FileNotFoundException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler, cedarTemplateFieldsExtractor);
      cedarService.recommendTemplates(payload);
    }, "FileNotFoundException was expected");
    assertThat(thrown.getMessage(), equalTo("Couldn't find recommended templates. Cause: <404>"));
  }

  @Test
  public void shouldThrowBadRequestExceptionWhenFindingRecommendedTemplateReturnsBadResponse() throws IOException {
    var payload = mock(ImmutableMap.class);

    when(requestHandler.createPostRequest(anyString(), eq(payload), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
    when(statusLine.toString()).thenReturn("<400>");

    var thrown = Assertions.assertThrows(BadRequestException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler, cedarTemplateFieldsExtractor);
      cedarService.recommendTemplates(payload);
    }, "BadRequestException was expected");
    assertThat(thrown.getMessage(), equalTo("Error retrieving recommended templates. Cause: <400>"));
  }
}