package org.metadatacenter.fairware.config;

public class CoreConfig {

  private double similarityThreshold;
  private double nameSimilarityWeight;
  private double pathSimilarityWeight;
  private int termSuggestionsListSize;

  public CoreConfig() {
  }

  public double getSimilarityThreshold() {
    return similarityThreshold;
  }

  public double getNameSimilarityWeight() {
    return nameSimilarityWeight;
  }

  public double getPathSimilarityWeight() {
    return pathSimilarityWeight;
  }

  public int getTermSuggestionsListSize() { return termSuggestionsListSize; }
}
