package org.metadatacenter.fairware.api.response;

import com.google.auto.value.AutoValue;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;

import javax.annotation.Nonnull;

@AutoValue
public abstract class EvaluationReportItem {

  public static EvaluationReportItem create(@Nonnull String metadataFieldPath,
                                            @Nonnull MetadataIssue metadataIssue,
                                            @Nonnull RepairAction repairAction) {
    return new AutoValue_EvaluationReportItem(metadataFieldPath, metadataIssue, repairAction);
  }

  @Nonnull
  public abstract String getMetadataFieldPath();

  @Nonnull
  public abstract MetadataIssue getMetadataIssue();

  @Nonnull
  public abstract RepairAction getRepairAction();
}
