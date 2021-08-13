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

  /**
   * This method finds an alignment between the fields in a metadata record and a given CEDAR template. It makes use
   * of the Hungarian algorithm to ensure that the alignment is optimal.
   * @param templateId  CEDAR template identifier
   * @param metadataRecord  Input metadata record
   * @return  A list of alignments between fields in the metadata record and fields in the given template
   * @throws IOException
   * @throws HttpException
   */
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
    int maxDimension = Math.max(metadataFields.size(), templateFields.size()); // Relevant when the matrix is non-square
    double[][] similarityMatrix = new double[maxDimension][maxDimension];
    for (int i=0; i<maxDimension; i++) {
      for (int j=0; j<maxDimension; j++) {
        if (i>=metadataFields.size() || j>=templateFields.size()) {
          similarityMatrix[i][j] = -1;
        }
        else {
          // Calculate and save similarity
          similarityMatrix[i][j] = FieldsAlignmentUtil.calculateSimilarity(metadataFields.get(i), templateFields.get(j),
              coreConfig.getNameSimilarityWeight(), coreConfig.getPathSimilarityWeight());
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
