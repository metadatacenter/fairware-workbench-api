package org.metadatacenter.fairware.core;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.fairware.api.evaluation.output.TemplateRecommendation;

import java.io.IOException;
import java.util.List;

public interface IMetadataEvaluationService {

  // TODO: use a custom class as input, instead of JsonNode (e.g., MetadataRecord.java)
  public List<TemplateRecommendation> recommendTemplate(JsonNode metadataRecord) throws IOException;

}
