package org.metadatacenter.fairware.config;

public class CoreConfig {

  private double similarityThreshold;
  private double nameSimilarityWeight;
  private double pathSimilarityWeight;

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
}
