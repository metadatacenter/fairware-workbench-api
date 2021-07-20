package org.metadatacenter.fairware.api.recommendation.request;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RecommendTemplatesRequest2 {

  private int number;

  public RecommendTemplatesRequest2(int number) {
    this.number = number;
  }

  public RecommendTemplatesRequest2() {
  }

  public int getNumber() {
    return number;
  }
}
