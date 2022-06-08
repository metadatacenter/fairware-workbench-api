package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableList;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.action.RepairAction;
import org.metadatacenter.fairware.api.response.action.SuggestedOntologyTerm;
import org.metadatacenter.fairware.api.response.issue.IssueType;
import org.metadatacenter.fairware.api.response.issue.MetadataIssue;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class ValueFromOntologyChecker {

  private final CoreConfig coreConfig;
  private final BioportalService bioportalService;

  public ValueFromOntologyChecker(@Nonnull CoreConfig coreConfig,
                                  @Nonnull BioportalService bioportalService) {
    this.coreConfig = checkNotNull(coreConfig);
    this.bioportalService = checkNotNull(bioportalService);
  }

  public Optional<EvaluationReportItem> checkValueAgainstOntology(@Nonnull MetadataFieldInfo metadataField,
                                                                  @Nonnull String ontology) throws IOException, HttpException {
    Optional<Object> value = metadataField.getValue();
    if (value.isPresent()) {
      var valueString = String.valueOf(value.get());
      if (!valueString.isEmpty()) {
        var results = bioportalService.search(valueString, ontology);
        if (results.getTotalCount() == 0) {
          Optional<String> prefLabel = metadataField.getPrefLabel();
          if (prefLabel.isPresent()) {
            var prefLabelString = prefLabel.get();
            if (!prefLabelString.isEmpty()) {
              results = bioportalService.search(prefLabelString, ontology);
            }
          }
        }
        if (results.getTotalCount() == 0) {
          return Optional.empty();
        } else {
          var suggestedTerms = results.getCollection().stream()
              .limit(coreConfig.getTermSuggestionsListSize())
              .map(bpClass -> SuggestedOntologyTerm.create(
                  bpClass.getId(),
                  bpClass.getPrefLabel(),
                  ontology))
              .collect(ImmutableList.toImmutableList());
          var report = EvaluationReportItem.create(
              MetadataIssue.create(IssueType.VALUE_NOT_ONTOLOGY_TERM,
                  GeneralUtil.generateFullPathDotNotation(metadataField),
                  valueString),
              RepairAction.ofReplaceMetadataValueWithStandardizedValue(suggestedTerms));
          return Optional.of(report);
        }
      }
    }
    return Optional.empty();
  }
}