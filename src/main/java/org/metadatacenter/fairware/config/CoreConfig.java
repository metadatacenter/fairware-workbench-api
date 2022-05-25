package org.metadatacenter.fairware.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class CoreConfig {

  private static final String SIMILARITY_THRESHOLD = "similarityThreshold";
  private static final String NAME_SIMILARITY_WEIGHT = "nameSimilarityWeight";
  private static final String PATH_SIMILARITY_WEIGHT = "pathSimilarityWeight";
  private static final String TERM_SUGGESTIONS_LIST_SIZE = "termSuggestionsListSize";

  @Nonnull
  @JsonCreator
  public static CoreConfig create(@JsonProperty(SIMILARITY_THRESHOLD) double similarityThreshold,
                                  @JsonProperty(NAME_SIMILARITY_WEIGHT) double nameSimilarityWeight,
                                  @JsonProperty(PATH_SIMILARITY_WEIGHT) double pathSimilarityWeight,
                                  @JsonProperty(TERM_SUGGESTIONS_LIST_SIZE) int termSuggestionsListSize) {
    return new AutoValue_CoreConfig(similarityThreshold, nameSimilarityWeight, pathSimilarityWeight,
        termSuggestionsListSize);
  }

  @JsonProperty(SIMILARITY_THRESHOLD)
  public abstract double getSimilarityThreshold();

  @JsonProperty(NAME_SIMILARITY_WEIGHT)
  public abstract double getNameSimilarityWeight();

  @JsonProperty(PATH_SIMILARITY_WEIGHT)
  public abstract double getPathSimilarityWeight();

  @JsonProperty(TERM_SUGGESTIONS_LIST_SIZE)
  public abstract int getTermSuggestionsListSize();
}
