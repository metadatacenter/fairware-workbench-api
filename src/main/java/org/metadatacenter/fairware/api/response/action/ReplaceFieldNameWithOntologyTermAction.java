package org.metadatacenter.fairware.api.response.action;

import java.util.List;

public class ReplaceFieldNameWithOntologyTermAction extends RepairAction {

  private String newFieldName;
  private List<SuggestedOntologyTerm> suggestedTerms;

  public ReplaceFieldNameWithOntologyTermAction(String newFieldName,
                                                List<SuggestedOntologyTerm> suggestedTerms) {
    super("Replace field name with ontology term");
    this.newFieldName = newFieldName;
    this.suggestedTerms = suggestedTerms;
  }

  public String getNewFieldName() {
    return newFieldName;
  }

  public List<SuggestedOntologyTerm> getSuggestedTerms() {
    return suggestedTerms;
  }
}
