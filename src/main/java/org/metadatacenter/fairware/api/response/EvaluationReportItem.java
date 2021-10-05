package org.metadatacenter.fairware.api.response;

import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;

public class EvaluationReportItem {

  private String metadataFieldPath;

  public enum Issue { MISSING_REQUIRED_VALUE };
  private Issue issue;

  public EvaluationReportItem() {}

  public EvaluationReportItem(String metadataFieldPath, Issue issue) {
    this.metadataFieldPath = metadataFieldPath;
    this.issue = issue;
  }

  public String getMetadataFieldPath() {
    return metadataFieldPath;
  }

  public Issue getIssue() {
    return issue;
  }
}
