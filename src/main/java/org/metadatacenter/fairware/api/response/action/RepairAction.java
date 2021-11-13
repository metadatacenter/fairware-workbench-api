package org.metadatacenter.fairware.api.response.action;

public abstract class RepairAction {

  private String description;

  protected RepairAction() {}

  protected RepairAction(String description) {
    this.description = description;
  }
}
