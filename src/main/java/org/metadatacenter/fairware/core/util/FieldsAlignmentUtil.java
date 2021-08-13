package org.metadatacenter.fairware.core.util;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class FieldsAlignmentUtil {

  /**
   * Calculates the similarity between a metadata field and a template field
   * @param metadataField
   * @param templateField
   * @param nameSimilarityWeight
   * @param pathSimilarityWeight
   * @return Similarity value in the range [0,1]
   */
  public static double calculateSimilarity(MetadataFieldInfo metadataField, TemplateNodeInfo templateField,
                                           double nameSimilarityWeight, double pathSimilarityWeight) {

    if (metadataField == null || templateField == null) {
      throw new IllegalArgumentException("Null argument");
    }
    if (metadataField.getName() == null || templateField.getName() == null) {
      throw new IllegalArgumentException("The field name cannot be null");
    }

    /* 1. Name similarity */
    double nameSimilarity = calculateNameSimilarity(metadataField.getName(), templateField.getName());
    if (templateField.getPrefLabel() != null && templateField.getPrefLabel().length() > 0) {
      nameSimilarity = Math.max(nameSimilarity, calculateNameSimilarity(metadataField.getName(), templateField.getPrefLabel()));
    }

    double pathSimilarity = 0;
    /* 2. Path similarity */
    if (nameSimilarity > 0) {
      pathSimilarity = calculatePathSimilarity(metadataField.getPath(), templateField.getPath());
    }

    /* 3. Aggregate name and path similarities */

    return nameSimilarityWeight * nameSimilarity + pathSimilarityWeight * pathSimilarity;
  }

  /**
   * Calculates the similarity between two field names using the FuzzyWuzzy library.
   * @param fieldName1
   * @param fieldName2
   * @return Similarity value in the range [0,1]
   */
  private static double calculateNameSimilarity(String fieldName1, String fieldName2) {
    // FuzzySearch.weightedRatio calculates a weighted ratio between different FuzzyWuzzy algorithms for best results
    int ratio = FuzzySearch.weightedRatio(StringUtil.basicNormalization(fieldName1),
        StringUtil.basicNormalization(fieldName2));
    return (double) ratio / (double) 100;
  }

  /**
   * Calculates the similarity between two field paths using the FuzzyWuzzy library.
   * @param fieldPath1
   * @param fieldPath2
   * @return Similarity value in the range [0,1]
   */
  private static double calculatePathSimilarity(List<String> fieldPath1, List<String> fieldPath2) {
    if (fieldPath1.isEmpty() && fieldPath2.isEmpty()) {
      return 1;
    }
    if (fieldPath1.isEmpty() || fieldPath2.isEmpty()) {
      return 0;
    }
    String fieldPath1Str = StringUtil.basicNormalization(StringUtils.join(fieldPath1, "."));
    String fieldPath2Str = StringUtil.basicNormalization(StringUtils.join(fieldPath2, "."));
    int ratio = FuzzySearch.ratio(fieldPath1Str, fieldPath2Str);
    return (double) ratio / (double) 100;
  }

  /**
   * Generates a list of FieldAlignment objects
   * @param metadataFields
   * @param templateFields
   * @param similarityMatrix
   * @param selectedAlignments
   * @return
   */
  public static List<FieldAlignment> generateFieldAlignments(List<MetadataFieldInfo> metadataFields,
                                                             List<TemplateNodeInfo> templateFields,
                                                             double[][] similarityMatrix, int[] selectedAlignments) {
    List<FieldAlignment> alignments = new ArrayList<>();
    for (int i=0; i<selectedAlignments.length; i++) {
      int templateFieldIndex = selectedAlignments[i]; // note that i corresponds to the metadata field index
      if (templateFieldIndex > -1 && similarityMatrix[i][templateFieldIndex] >= 0) {
        alignments.add(new FieldAlignment(similarityMatrix[i][templateFieldIndex],
            metadataFields.get(i),
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
   * Translates a similarity matrix in the interval [0,1] to a distance matrix in [0,1], and viceversa
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
