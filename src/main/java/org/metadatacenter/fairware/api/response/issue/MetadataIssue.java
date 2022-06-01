package org.metadatacenter.fairware.api.response.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class MetadataIssue {

  private static final String ISSUE_TYPE = "issueType";
  private static final String ISSUE_LEVEL = "issueLevel";

  public static MetadataIssue create(@Nonnull @JsonProperty(ISSUE_TYPE) IssueType issueType) {
    return new AutoValue_MetadataIssue(issueType);
  }

  @Nonnull
  @JsonProperty(ISSUE_TYPE)
  public abstract IssueType getIssueType();

  @Nonnull
  @JsonProperty(ISSUE_LEVEL)
  public IssueLevel getIssueLevel() {
    var issueType = getIssueType();
    switch (issueType) {
      case MISSING_OPTIONAL_VALUE:
        return IssueLevel.WARNING;
      default:
        return IssueLevel.ERROR;
    }
  }
}
