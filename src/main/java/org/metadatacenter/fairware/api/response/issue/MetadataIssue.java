package org.metadatacenter.fairware.api.response.issue;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import java.security.InvalidParameterException;

@AutoValue
public abstract class MetadataIssue {

  public static MetadataIssue create(@Nonnull IssueType issueType) {
    return new AutoValue_MetadataIssue(issueType);
  }

  @Nonnull
  public abstract IssueType getIssueType();

  @Nonnull
  public IssueLevel getIssueLevel() {
    var issueType = getIssueType();
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
