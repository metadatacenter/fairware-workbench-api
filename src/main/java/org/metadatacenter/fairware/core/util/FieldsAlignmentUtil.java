package org.metadatacenter.fairware.core.util;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang3.StringUtils;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;

import java.util.ArrayList;
import java.util.List;

public class FieldsAlignmentUtil {

  /**
   * Calculates the similarity between a metadata field and a template field
   * @param metadataField a metadata field
   * @param templateField a template field
   * @param nameSimilarityWeight weight in the interval [0,1] assigned to the field name similarity
   * @param pathSimilarityWeight weight in the interval [0,1] assigned to the field path similarity
   * @return Similarity value in the range [0,1]
   */
  public static double calculateSimilarity(MetadataFieldInfo metadataField, CedarTemplateField templateField,
                                           double nameSimilarityWeight, double pathSimilarityWeight) {

    if (metadataField == null || templateField == null) {
      throw new IllegalArgumentException("Null argument");
    }
    if (metadataField.getName() == null || templateField.getName() == null) {
      throw new IllegalArgumentException("The field name cannot be null");
    }

    /* 1. Name similarity */
    var metadataFieldName = metadataField.getName();
    var templateFieldName = templateField.getName();
    double nameSimilarity = calculateNameSimilarity(metadataFieldName, templateFieldName);
    if (templateField.getPrefLabel().isPresent()) {
      var templateFieldPrefLabel = templateField.getPrefLabel().get();
      nameSimilarity = Math.max(nameSimilarity, calculateNameSimilarity(metadataFieldName, templateFieldPrefLabel));
    }

    double pathSimilarity = 0;
    /* 2. Path similarity */
    if (nameSimilarity > 0) {
      pathSimilarity = calculatePathSimilarity(metadataField.getPath(), templateField.getContainerName().orElse(""));
    }

    /* 3. Aggregate name and path similarities */

    return nameSimilarityWeight * nameSimilarity + pathSimilarityWeight * pathSimilarity;
  }

  /**
   * Calculates the similarity between two field names using the FuzzyWuzzy library.
   * @param fieldName1 a field name
   * @param fieldName2 another field name
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
   * @param fieldPath1 the path of the first field
   * @param fieldPath2 the path of the second field
   * @return a similarity value in the range [0,1] that represents the similarity between the two input paths
   */
  private static double calculatePathSimilarity(List<String> fieldPath1, String fieldPath2) {
//    if (fieldPath1.isEmpty() && fieldPath2.isEmpty()) {
//      return 1;
//    }
//    if (fieldPath1.isEmpty() || fieldPath2.isEmpty()) {
//      return 0;
//    }
    String fieldPath1Str = StringUtil.basicNormalization(StringUtils.join(fieldPath1, "."));
    String fieldPath2Str = fieldPath2;
    if (fieldPath1Str.isEmpty() && fieldPath2Str.isEmpty()) {
      return 1;
    } else if (fieldPath1.isEmpty() || fieldPath2Str.isEmpty()) {
      return 0;
    } else {
      int ratio = FuzzySearch.ratio(fieldPath1Str, fieldPath2Str);
      return (double) ratio / (double) 100;
    }
  }

  /**
   * Generates a list of FieldAlignment objects
   *
   * @param metadataFields     the list of metadata fields
   * @param templateFields     the list of template fields
   * @param similarityMatrix   a matrix with the similarity between metadata fields and template fields
   * @param selectedAlignments array with optimal alignments, selected by the hungarian algorithm. For
   *                           selectedAlignments[i]=j, the metadata field with index i is aligned to the
   *                           template field with index j in the similarity matrix
   * @return a list of alignments between metadata and template fields, represented using the FieldAlignment class
   */
  public static List<FieldAlignment> generateFieldAlignments(List<MetadataFieldInfo> metadataFields,
                                                             List<CedarTemplateField> templateFields,
                                                             double[][] similarityMatrix,
                                                             int[] selectedAlignments) {
    List<FieldAlignment> alignments = new ArrayList<>();
    for (int i=0; i<selectedAlignments.length; i++) {
      int templateFieldIndex = selectedAlignments[i];
      if (templateFieldIndex > -1 && similarityMatrix[i][templateFieldIndex] >= 0) {
        String metadataFieldPath = GeneralUtil.generateFullPathDotNotation(metadataFields.get(i));
        String templateFieldPath = templateFields.get(templateFieldIndex).getJsonPath();
        alignments.add(
            FieldAlignment.create(similarityMatrix[i][templateFieldIndex],
                metadataFieldPath,
                templateFieldPath));
      }
    }
    return alignments;
  }

  /**
   * Transforms the similarity matrix to ignore (set to -1) all correspondences with similarity under a given threshold
   *
   * @param matrix a similarity matrix, with similarity values in the interval [0,1]
   * @param threshold a threshold in the interval [0,1]
   * @return a transformed similarity matrix, with all values lower than the threshold set to -1
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
   *
   * @param matrix a similarity/distance matrix
   * @return a translated similarity/distance matrix
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
