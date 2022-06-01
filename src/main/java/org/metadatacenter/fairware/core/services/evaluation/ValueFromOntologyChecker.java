package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.Lists;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.action.SuggestedOntologyTerm;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.config.bioportal.BioportalConfig;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class ValueFromOntologyChecker {

  private final BioportalService bioportalService;

  public ValueFromOntologyChecker(@Nonnull BioportalService bioportalService) {
    this.bioportalService = checkNotNull(bioportalService);
  }

  public Optional<EvaluationReportItem> checkValueAgainstOntology(@Nonnull MetadataFieldInfo metadataField,
                                                                  @Nonnull String ontology) throws IOException, HttpException {
    Optional<Object> value = metadataField.getValue();
    if (value.isPresent()) {
      var valueString = String.valueOf(value.get());
      var results = bioportalService.search(valueString, ontology);
      if (results.getTotalCount() == 0) {
        Optional<String> prefLabel = metadataField.getPrefLabel();
        if (prefLabel.isPresent()) {
          results = bioportalService.search(prefLabel.get(), ontology);
          if (results.getTotalCount() == 0) {
            return Optional.empty();
          }
        }
      }
      var firstOntologyTerm = results.getCollection().stream().findFirst()
          .map(bpClass -> SuggestedOntologyTerm.create(
              bpClass.getId(),
              bpClass.getPrefLabel(),
              ontology)).get();
      var report = EvaluationReportItem.create(
          GeneralUtil.generateFullPathDotNotation(metadataField),
          MetadataIssue.create(IssueType.VALUE_NOT_ONTOLOGY_TERM),
          RepairAction.ofReplaceMetadataValueWithOntologyTerm(firstOntologyTerm));
      return Optional.of(report);
    }
    return Optional.empty();
  }
}