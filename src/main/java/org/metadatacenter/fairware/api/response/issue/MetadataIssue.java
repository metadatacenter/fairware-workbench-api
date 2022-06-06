package org.metadatacenter.fairware.api.response.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class MetadataIssue {

  private static final String ISSUE_TYPE = "issueType";
  private static final String ISSUE_LEVEL = "issueLevel";
  private static final String ISSUE_LOCATION = "issueLocation";
  private static final String VALUE = "value";

  public static MetadataIssue create(@Nonnull @JsonProperty(ISSUE_TYPE) IssueType issueType,
                                     @Nonnull @JsonProperty(ISSUE_LOCATION) String issueLocation,
                                     @Nullable Object value) {
    return new AutoValue_MetadataIssue(issueType, issueLocation, value);
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
