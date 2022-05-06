package org.metadatacenter.fairware.core.services.cedar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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

  @BeforeEach
  public void setUp() throws IOException {
    when(cedarConfig.getBaseUrl()).thenReturn("http://example.org/");
    when(cedarConfig.getApiKey()).thenReturn("abc123");
    when(response.getStatusLine()).thenReturn(statusLine);
  }

  @Test
  public void shouldFindTemplate() throws IOException, HttpException {
    var expected = ImmutableMap.of("title", "Lorem Ipsum", "id", "123");

    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    mockInputStreamResponse();
    when(objectMapper.readValue(any(InputStream.class), eq(ImmutableMap.class))).thenReturn(expected);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);

    var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
    var output = cedarService.findTemplate("123");

    verify(objectMapper, times(1)).readValue(any(InputStream.class), eq(ImmutableMap.class));
    assertThat(output, equalTo(expected));
  }

  private void mockInputStreamResponse() throws IOException {
    when(response.getEntity()).thenReturn(entity);
    when(entity.getContent()).thenReturn(inputStream);
  }

  @Test
  public void shouldThrowHttpExceptionWhenTemplateNotFound() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
    when(statusLine.toString()).thenReturn("<404>");

    var thrown = Assertions.assertThrows(HttpException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
      cedarService.findTemplate("123");
    }, "HttpException was expected");
    assertThat(thrown.getMessage(), equalTo("Couldn't find CEDAR template (ID = 123). Cause: <404>"));
  }

  @Test
  public void shouldThrowHttpExceptionWhenFindingTemplateReturnsBadResponse() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_GATEWAY_TIMEOUT);
    when(statusLine.toString()).thenReturn("<504>");

    var thrown = Assertions.assertThrows(HttpException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
      cedarService.findTemplate("123");
    }, "HttpException was expected");
    assertThat(thrown.getMessage(), equalTo("Error retrieving template (ID = 123). Cause: <504>"));
  }

  @Test
  public void shouldFindTemplateInstance() throws IOException, HttpException {
    var expected = ImmutableMap.of("fname", "John", "lname", "Doe");

    mockInputStreamResponse();
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(objectMapper.readValue(any(InputStream.class), eq(ImmutableMap.class))).thenReturn(expected);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);

    var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
    var output = cedarService.findTemplateInstance("123");

    verify(objectMapper, times(1)).readValue(any(InputStream.class), eq(ImmutableMap.class));
    assertThat(output, equalTo(expected));
  }

  @Test
  public void shouldThrowHttpExceptionWhenTemplateInstanceNotFound() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
    when(statusLine.toString()).thenReturn("<404>");

    var thrown = Assertions.assertThrows(HttpException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
      cedarService.findTemplateInstance("123");
    }, "HttpException was expected");
    assertThat(thrown.getMessage(), equalTo("Couldn't find CEDAR template instance (ID = 123). Cause: <404>"));
  }

  @Test
  public void shouldThrowHttpExceptionWhenFindingTemplateInstanceReturnsBadResponse() throws IOException {
    when(requestHandler.createGetRequest(anyString(), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_GATEWAY);
    when(statusLine.toString()).thenReturn("<502>");

    var thrown = Assertions.assertThrows(HttpException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
      cedarService.findTemplateInstance("123");
    }, "HttpException was expected");
    assertThat(thrown.getMessage(), equalTo("Error retrieving template instance (ID = 123). Cause: <502>"));
  }

  @Test
  @Disabled
  public void shouldFindRecommendedTemplate() throws IOException, HttpException {
  }

  @Test
  public void shouldThrowHttpExceptionWhenFindingRecommendedNotFound() throws IOException {
    var payload = mock(ImmutableMap.class);

    when(requestHandler.createPostRequest(anyString(), eq(payload), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
    when(statusLine.toString()).thenReturn("<404>");

    var thrown = Assertions.assertThrows(HttpException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
      cedarService.recommendTemplates(payload);
    }, "HttpException was expected");
    assertThat(thrown.getMessage(), equalTo("Couldn't find recommended templates. Cause: <404>"));
  }

  @Test
  public void shouldThrowHttpExceptionWhenFindingRecommendedTemplateReturnsBadResponse() throws IOException {
    var payload = mock(ImmutableMap.class);

    when(requestHandler.createPostRequest(anyString(), eq(payload), anyString())).thenReturn(request);
    when(requestHandler.execute(request)).thenReturn(response);
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.SC_BAD_REQUEST);
    when(statusLine.toString()).thenReturn("<400>");

    var thrown = Assertions.assertThrows(HttpException.class, () -> {
      var cedarService = new CedarService(cedarConfig, objectMapper, requestHandler);
      cedarService.recommendTemplates(payload);
    }, "HttpException was expected");
    assertThat(thrown.getMessage(), equalTo("Error retrieving recommended templates. Cause: <400>"));
  }
}