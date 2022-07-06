package org.metadatacenter.fairware.api.response.evaluation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.metadatacenter.fairware.shared.RepairAction;
import org.metadatacenter.fairware.shared.MetadataIssue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluationReportItem {

  private static final String ISSUE_DETAILS = "issueDetails";
  private static final String REPAIR_ACTION = "repairAction";

  @JsonCreator
  public static EvaluationReportItem create(@Nonnull @JsonProperty(ISSUE_DETAILS) MetadataIssue metadataIssue,
                                            @Nonnull @JsonProperty(REPAIR_ACTION) RepairAction repairAction) {
    return new AutoValue_EvaluationReportItem(metadataIssue, repairAction);
  }

  @Nonnull
  @JsonProperty(ISSUE_DETAILS)
  public abstract MetadataIssue getMetadataIssue();

  @Nonnull
  @JsonProperty(REPAIR_ACTION)
  public abstract RepairAction getRepairAction();
}
