package org.metadatacenter.fairware.core.services;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.domain.TemplateNode;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.core.util.CedarTemplateContentExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetadataService implements IMetadataService {


  private final CedarService cedarService;

  public MetadataService(CedarService cedarService) {
    this.cedarService = cedarService;
  }

  @Override
  public List<FieldAlignment> alignMetadata(String templateId, Map<String, Object> metadataRecord)
      throws IOException, HttpException {
    Map<String, Object> template = cedarService.findTemplate(templateId);

    List<TemplateNode> nodes = CedarTemplateContentExtractor.getTemplateNodes(template);


    return new ArrayList<>();
  }
}
