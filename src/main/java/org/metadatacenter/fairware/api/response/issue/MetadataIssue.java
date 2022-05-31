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
    switch (issueType) {
      case MISSING_REQUIRED_VALUE:
      case INVALID_VALUE_REPRESENTATION:
        return IssueLevel.ERROR;
      case MISSING_OPTIONAL_VALUE:
        return IssueLevel.WARNING;
      default:
        throw new InvalidParameterException("Invalid issue type");
    }
  }
}
