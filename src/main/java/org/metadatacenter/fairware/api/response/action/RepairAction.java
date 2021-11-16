package org.metadatacenter.fairware.api.response.action;

public abstract class RepairAction {

  private String message;

  protected RepairAction() {}

  protected RepairAction(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
