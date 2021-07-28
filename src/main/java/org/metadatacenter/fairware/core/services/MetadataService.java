package org.metadatacenter.fairware.core.services;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.config.cedar.CoreConfig;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;
import org.metadatacenter.fairware.core.services.cedar.CedarService;
import org.metadatacenter.fairware.core.util.CedarTemplateContentExtractor;
import org.metadatacenter.fairware.core.util.FieldsAlignmentUtil;
import org.metadatacenter.fairware.core.util.HungarianAlgorithm;
import org.metadatacenter.fairware.core.util.MetadataContentExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetadataService implements IMetadataService {


  private final CedarService cedarService;
  private final CoreConfig coreConfig;

  public MetadataService(CedarService cedarService, CoreConfig coreConfig) {
    this.cedarService = cedarService;
    this.coreConfig = coreConfig;
  }

  @Override
  public List<FieldAlignment> alignMetadata(String templateId, Map<String, Object> metadataRecord)
      throws IOException, HttpException {
    Map<String, Object> template = cedarService.findTemplate(templateId);

    // Extract template nodes from the template. Keep fields only
    List<TemplateNodeInfo> templateFields = CedarTemplateContentExtractor.getTemplateNodes(template)
        .stream().filter(n->n.isTemplateFieldNode()).collect(Collectors.toList());
    // Extract metadata fields from the metadata record
    List<MetadataFieldInfo> metadataFields = MetadataContentExtractor.extractMetadataFieldsInfo(metadataRecord);

    // Find alignments between metadata fields and template fields
    double[][] similarityMatrix = new double[metadataFields.size()][templateFields.size()];
    int minDimension = Math.min(metadataFields.size(), templateFields.size()); // Relevant when the matrix is non-square
    for (int i=0; i<metadataFields.size(); i++) {
      for (int j=0; j<templateFields.size(); j++) {
        if (i>=minDimension || j>=minDimension) {
          similarityMatrix[i][j] = -1;
        }
        else {
          // Calculate and save distance (1 - similarity)
          similarityMatrix[i][j] = FieldsAlignmentUtil.calculateSimilarity(metadataFields.get(i), templateFields.get(j));
        }
      }
    }

    // Apply a similarity filter to discard all correspondences under a threshold
    similarityMatrix = FieldsAlignmentUtil.filterBySimilarity(similarityMatrix, coreConfig.getSimilarityThreshold());

    HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(FieldsAlignmentUtil.translateMatrix(similarityMatrix));
    int[] optimalAlignment = hungarianAlgorithm.execute();

    return FieldsAlignmentUtil.generateFieldAlignments(metadataFields, templateFields,
        similarityMatrix, optimalAlignment);

  }
}
