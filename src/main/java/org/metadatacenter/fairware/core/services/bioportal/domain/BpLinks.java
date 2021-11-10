package org.metadatacenter.fairware.core.services.bioportal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BpLinks
{

  private String self;
  private String ontology;
  private String children;
  private String parents;
  private String descendants;
  private String ancestors;
  private String instances;
  private String tree;
  private String notes;
  private String mappings;
  private String ui;

  public BpLinks() {}

  public BpLinks(String self, String ontology, String children, String parents, String descendants, String ancestors,
    String instances, String tree, String notes, String mappings, String ui)
  {
    this.self = self;
    this.ontology = ontology;
    this.children = children;
    this.parents = parents;
    this.descendants = descendants;
    this.ancestors = ancestors;
    this.instances = instances;
    this.tree = tree;
    this.notes = notes;
    this.mappings = mappings;
    this.ui = ui;
  }

  public String getSelf()
  {
    return self;
  }

  public void setSelf(String self)
  {
    this.self = self;
  }

  public String getOntology()
  {
    return ontology;
  }

  public void setOntology(String ontology)
  {
    this.ontology = ontology;
  }

  public String getChildren()
  {
    return children;
  }

  public void setChildren(String children)
  {
    this.children = children;
  }

  public String getParents()
  {
    return parents;
  }

  public void setParents(String parents)
  {
    this.parents = parents;
  }

  public String getDescendants()
  {
    return descendants;
  }

  public void setDescendants(String descendants)
  {
    this.descendants = descendants;
  }

  public String getAncestors()
  {
    return ancestors;
  }

  public void setAncestors(String ancestors)
  {
    this.ancestors = ancestors;
  }

  public String getInstances()
  {
    return instances;
  }

  public void setInstances(String instances)
  {
    this.instances = instances;
  }

  public String getTree()
  {
    return tree;
  }

  public void setTree(String tree)
  {
    this.tree = tree;
  }

  public String getNotes()
  {
    return notes;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  public String getMappings()
  {
    return mappings;
  }

  public void setMappings(String mappings)
  {
    this.mappings = mappings;
  }

  public String getUi()
  {
    return ui;
  }

  public void setUi(String ui)
  {
    this.ui = ui;
  }
}
