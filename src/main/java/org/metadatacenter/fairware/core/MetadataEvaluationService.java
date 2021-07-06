package org.metadatacenter.fairware.core;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.fairware.api.evaluation.output.TemplateInfo;
import org.metadatacenter.fairware.api.evaluation.output.TemplateRecommendation;
import org.metadatacenter.fairware.core.util.CedarService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MetadataEvaluationService implements IMetadataEvaluationService {

  @Override
  public List<TemplateRecommendation> recommendTemplate(JsonNode metadataRecord) throws IOException {

    CedarService service = new CedarService();

    List<String> fieldNames = new ArrayList<>();
    Iterator<String> itFieldNames = metadataRecord.fieldNames();
    while (itFieldNames.hasNext()) {
      fieldNames.add(itFieldNames.next());
    }
    JsonNode templatesFound = service.searchTemplatesByFieldName(fieldNames, 10, 0);

    List<TemplateRecommendation> recommendations = new ArrayList<>();
    Iterator<JsonNode> it = templatesFound.get("resources").elements();
    while (it.hasNext()) {
      JsonNode template = it.next();
      TemplateInfo templateInformation = new TemplateInfo(template.get("@id").asText(),
          template.get("schema:name").asText(),
          template.get("schema:description").asText(),
          template.get("ownedBy").asText(),
          template.get("ownedByUserName").asText());
      TemplateRecommendation recommendation = new TemplateRecommendation(templateInformation, 0);
      recommendations.add(recommendation);
    }
    return recommendations;
  }
}
