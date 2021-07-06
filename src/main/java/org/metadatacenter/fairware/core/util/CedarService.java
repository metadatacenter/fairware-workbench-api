package org.metadatacenter.fairware.core.util;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class CedarService {

  public JsonNode searchTemplatesByFieldName(List<String> fieldNames, int limit, int offset) throws IOException {

    StringBuilder query = new StringBuilder();
    for (int i = 0; i < fieldNames.size() - 1; i++) {
      query.append("\"").append(fieldNames.get(i)).append("\"").append(": OR ");
    }
    int lastIndex = fieldNames.size() - 1;
    query.append(fieldNames.get(lastIndex)).append(":");

    StringBuilder uri = new StringBuilder("https://resource.metadatacenter.org/search?")
        .append("q=").append(URLEncoder.encode(query.toString(), "UTF-8"))
        .append("&limit=").append(limit)
        .append("&offset=").append(offset)
        .append("&resource_types=template&version=all");

    // TODO: read URL from constants file
    HttpResponse response = Request.Get(uri.toString()).addHeader("Authorization", "apiKey " +
        "<your_api_key>").execute().returnResponse();

    //int statusCode = response.returnResponse().getStatusLine().getStatusCode();
    //String res = response.returnResponse().toString();
    return new ObjectMapper().readTree(new String(EntityUtils.toByteArray(response.getEntity())));

    // TODO: error handling
    //return new ObjectMapper().readTree(response.getEntity().toString());

  }

}
