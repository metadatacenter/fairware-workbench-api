package org.metadatacenter.fairware.core.util;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class FieldsAlignmentUtil {

  public static double calculateSimilarity(MetadataFieldInfo metadataField, TemplateNodeInfo templateField) {
    // Apply basic normalization
    String metadataFieldName = StringUtil.basicNormalization(metadataField.getName());
    String templateFieldName = StringUtil.basicNormalization(templateField.getName());
    String templateFieldPrefLabel = StringUtil.basicNormalization(templateField.getPrefLabel());

    if (metadataField == null || templateField == null) {
      throw new IllegalArgumentException("Null argument");
    }
    if (metadataField.getName() == null || templateField.getName() == null) {
      throw new IllegalArgumentException("The field name cannot be null");
    }
    // FuzzySearch.weightedRatio calculates a weighted ratio between the different FuzzyWuzzy algorithms for best results
    int ratio = FuzzySearch.weightedRatio(metadataFieldName, templateFieldName);
    if (templateFieldPrefLabel != null && templateFieldPrefLabel.length() > 0) {
      ratio = Math.max(ratio, FuzzySearch.weightedRatio(metadataFieldName, templateFieldPrefLabel));
    }
    return (double) ratio / (double) 100;
  }

  public static List<FieldAlignment> generateFieldAlignments(List<MetadataFieldInfo> metadataFields,
                                                             List<TemplateNodeInfo> templateFields,
                                                             double[][] similarityMatrix, int[] selectedAlignments) {
    List<FieldAlignment> alignments = new ArrayList<>();
    for (int i=0; i<selectedAlignments.length; i++) {
      int metadataFieldIndex = i;
      int templateFieldIndex = selectedAlignments[i];
      if (templateFieldIndex > -1 && similarityMatrix[metadataFieldIndex][templateFieldIndex] >= 0) {
        alignments.add(new FieldAlignment(similarityMatrix[metadataFieldIndex][templateFieldIndex],
            metadataFields.get(metadataFieldIndex),
            templateFields.get(templateFieldIndex)));
      }
    }
    return alignments;
  }

  /**
   * Transform the similarity matrix to ignore (set to -1) all correspondences with similarity under a given threshold
   * @param matrix
   * @param threshold
   * @return
   */
  public static double[][] filterBySimilarity(double[][] matrix, double threshold) {
    double[][] result = new double[matrix.length][matrix[0].length];
    for (int i=0; i<matrix.length; i++) {
      for (int j=0; j<matrix[0].length; j++) {
        result[i][j] = matrix[i][j] >= threshold ? matrix[i][j] : -1;
      }
    }
    return result;
  }

  /**
   * Translates a similarity matrix in the interval [0,1] to a distance matrix in [0,1], or viceversa
   * @param matrix
   * @return
   */
  public static double[][] translateMatrix(double[][] matrix) {
    double[][] result = new double[matrix.length][matrix[0].length];
    for (int i=0; i<matrix.length; i++) {
      for (int j=0; j<matrix[0].length; j++) {
        result[i][j] = 1 - matrix[i][j];
      }
    }
    return result;
  }

}
