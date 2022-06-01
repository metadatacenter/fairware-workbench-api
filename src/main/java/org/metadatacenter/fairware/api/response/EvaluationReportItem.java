package org.metadatacenter.fairware.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluationReportItem {

  private static final String ISSUE_LOCATION = "issueLocation";
  private static final String ISSUE_DETAILS = "issueDetails";
  private static final String REPAIR_ACTION = "repairAction";

  @JsonCreator
  public static EvaluationReportItem create(@Nonnull @JsonProperty(ISSUE_LOCATION) String metadataFieldPath,
                                            @Nonnull @JsonProperty(ISSUE_DETAILS) MetadataIssue metadataIssue,
                                            @Nonnull @JsonProperty(REPAIR_ACTION) RepairAction repairAction) {
    return new AutoValue_EvaluationReportItem(metadataFieldPath, metadataIssue, repairAction);
  }

  @Nonnull
  @JsonProperty(ISSUE_LOCATION)
  public abstract String getMetadataFieldPath();

  @Nonnull
  @JsonProperty(ISSUE_DETAILS)
  public abstract MetadataIssue getMetadataIssue();

  @Nonnull
  @JsonProperty(REPAIR_ACTION)
  public abstract RepairAction getRepairAction();
}
