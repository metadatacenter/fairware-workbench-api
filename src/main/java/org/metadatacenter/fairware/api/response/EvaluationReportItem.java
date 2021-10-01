package org.metadatacenter.fairware.api.response;

import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;

public class EvaluationReportItem {

  private MetadataFieldInfo metadataField;

  public enum Issue { MISSING_REQUIRED_VALUE };
  private Issue issue;

  public EvaluationReportItem() {}

  public EvaluationReportItem(MetadataFieldInfo metadataField, Issue issue) {
    this.metadataField = metadataField;
    this.issue = issue;
  }

  public MetadataFieldInfo getMetadataField() {
    return metadataField;
  }

  public Issue getIssue() {
    return issue;
  }
}
