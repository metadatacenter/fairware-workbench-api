package org.metadatacenter.fairware.core.services.biosample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class BioSampleDataParser {

  private final XmlMapper xmlMapper;

  public BioSampleDataParser(@Nonnull XmlMapper xmlMapper) {
    this.xmlMapper = checkNotNull(xmlMapper);
  }

  public ImmutableMap<String, Object> parseToMap(String xmlText) throws JsonProcessingException {
    var mapType = xmlMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
    var parsedData = xmlMapper.readTree(xmlText);
    return preprocess(parsedData);
  }

  private ImmutableMap<String, Object> preprocess(JsonNode parsedData) {
    var data = Maps.<String, Object>newHashMap();
    var ids = parsedData.get("BioSample").get("Ids").get("Id");
    for (var id : ids) {
      if (id.has("db")) {
        if ("BioSample".equals(id.get("db").asText()) && id.has("")) {
          data.put("bio_sample_id", id.get("").asText());
        } else if ("SRA".equals(id.get("db").asText()) && id.has("")) {
          data.put("sra_id", id.get("").asText());
        }
      }
    }
    if (parsedData.get("BioSample").has("Links")) {
      if (parsedData.get("BioSample").get("Links").has("Link")) {
        var links = parsedData.get("BioSample").get("Links").get("Link");
        if (links.isArray()) {
          for (var link : links) {
            if (link.has("target") && link.has("label")) {
              if ("BioProject".equals(link.get("target").asText())) {
                data.put("bio_project_id", link.get("label").asText());
              }
            }
          }
        } else {
          if (links.has("target") && links.has("label")) {
            if ("BioProject".equals(links.get("target").asText())) {
              data.put("bio_project_id", links.get("label").asText());
            }
          }
        }
      }
    } else {
      data.put("bio_project_id", "");
    }
    data.put("organism", parsedData.get("BioSample").get("Description").get("Organism").get("taxonomy_name").asText());
    data.put("package", parsedData.get("BioSample").get("Package").get("").asText());
    if (parsedData.get("BioSample").has("Attributes")) {
      var attributes = parsedData.get("BioSample").get("Attributes").get("Attribute");
      if (attributes.isArray()) {
        for (var attribute : attributes) {
          if (attribute.has("attribute_name") && attribute.has("")) {
            data.put(attribute.get("attribute_name").asText(), attribute.get("").asText());
          }
        }
      } else {
        if (attributes.has("attribute_name") && attributes.has("")) {
          data.put(attributes.get("attribute_name").asText(), attributes.get("").asText());
        }
      }
    }
    return ImmutableMap.copyOf(data);
  }
}
