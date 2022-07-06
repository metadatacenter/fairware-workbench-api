package org.metadatacenter.fairware.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class MetadataIssue {

  private static final String ISSUE_CATEGORY = "issueCategory";
  private static final String ISSUE_TYPE = "issueType";
  private static final String ISSUE_LEVEL = "issueLevel";
  private static final String ISSUE_LOCATION = "issueLocation";
  private static final String VALUE = "value";

  public static MetadataIssue create(@Nonnull IssueCategory issueCategory,
                                     @Nonnull IssueType issueType,
                                     @Nonnull String issueLocation,
                                     @Nullable Object value) {
    return new AutoValue_MetadataIssue(issueCategory, issueType, issueLocation, value);
  }

  @Nonnull
  @JsonProperty(ISSUE_CATEGORY)
  public abstract IssueCategory getIssueCategory();

  @Nonnull
  @JsonProperty(ISSUE_TYPE)
  public abstract IssueType getIssueType();

  @Nonnull
  @JsonProperty(ISSUE_LEVEL)
  public IssueLevel getIssueLevel() {
    var issueType = getIssueType();
    switch (issueType) {
      case MISSING_OPTIONAL_VALUE:
      case FIELD_NOT_FOUND_IN_TEMPLATE:
        return IssueLevel.WARNING;
      default:
        return IssueLevel.ERROR;
    }
  }

  @Nonnull
  @JsonProperty(ISSUE_LOCATION)
  public abstract String getIssueLocation();

  @Nullable
  @JsonProperty(VALUE)
  public abstract Object getValue();
}
