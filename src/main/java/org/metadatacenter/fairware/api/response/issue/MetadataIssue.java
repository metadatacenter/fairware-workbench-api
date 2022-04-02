package org.metadatacenter.fairware.api.response.issue;

import java.security.InvalidParameterException;

public class MetadataIssue {

  private IssueType issueType;
  private IssueLevel issueLevel;

  public MetadataIssue() {
  }

  public MetadataIssue(IssueType issueType) {
    this.issueType = issueType;
    this.issueLevel = calculateIssueLevel(issueType);
  }

  public IssueType getIssueType() {
    return issueType;
  }

  public IssueLevel getIssueLevel() {
    return issueLevel;
  }

  private IssueLevel calculateIssueLevel(IssueType issueType) {
    if (issueType.equals(IssueType.MISSING_REQUIRED_VALUE)) {
      return IssueLevel.ERROR;
    }
    else if (issueType.equals(IssueType.MISSING_OPTIONAL_VALUE)) {
      return IssueLevel.WARNING;
    }
    else {
      throw new InvalidParameterException("Invalid issue type");
    }
  }
}
