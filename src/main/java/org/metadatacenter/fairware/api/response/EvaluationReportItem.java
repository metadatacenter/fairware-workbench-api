package org.metadatacenter.fairware.api.response;

import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;

public class EvaluationReportItem {

  private String metadataFieldPath;
  private MetadataIssue issue;
  private RepairAction repairAction;

  public EvaluationReportItem() {
  }

  public EvaluationReportItem(String metadataFieldPath, MetadataIssue issue, RepairAction repairAction) {
    this.metadataFieldPath = metadataFieldPath;
    this.issue = issue;
    this.repairAction = repairAction;
  }

  public String getMetadataFieldPath() {
    return metadataFieldPath;
  }

  public MetadataIssue getIssue() {
    return issue;
  }

  public RepairAction getRepairAction() {
    return repairAction;
  }
}
