package org.metadatacenter.fairware.api.response.issue;

public class MetadataIssue {

  private IssueType issueType;

  public MetadataIssue(IssueType issueType) {
    this.issueType = issueType;
  }

  public IssueType getIssueType() {
    return issueType;
  }
}
