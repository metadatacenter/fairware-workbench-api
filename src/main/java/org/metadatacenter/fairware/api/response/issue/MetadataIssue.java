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
      case MISSING_OPTIONAL_VALUE:
        return IssueLevel.WARNING;
      default:
        return IssueLevel.ERROR;
    }
  }
}
