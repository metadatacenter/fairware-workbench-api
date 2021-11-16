package org.metadatacenter.fairware.api.response.action;

public class SuggestedOntologyTerm {

  private String uri;
  private String label;
  private String ontologyAcronym;
  private String definition;

  public SuggestedOntologyTerm(String uri, String label, String ontologyAcronym, String definition) {
    this.uri = uri;
    this.label = label;
    this.ontologyAcronym = ontologyAcronym;
    this.definition = definition;
  }

  public String getUri() {
    return uri;
  }

  public String getLabel() {
    return label;
  }

  public String getOntologyAcronym() {
    return ontologyAcronym;
  }

  public String getDefinition() {
    return definition;
  }
}
