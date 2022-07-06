package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableList;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.shared.RepairAction;
import org.metadatacenter.fairware.shared.SuggestedOntologyTerm;
import org.metadatacenter.fairware.shared.IssueCategory;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.MetadataIssue;
import org.metadatacenter.fairware.config.CoreConfig;
import org.metadatacenter.fairware.core.services.bioportal.BioportalService;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpClass;
import org.metadatacenter.fairware.core.services.bioportal.domain.BpPagedResults;
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
                                                                  @Nonnull String ontologyAcronym) throws IOException, HttpException {
    Optional<Object> value = metadataField.getValue();
    if (value.isPresent()) {
      var valueString = String.valueOf(value.get());
      if (valueString.isEmpty()) {
        return Optional.empty();
      }
      var results = bioportalService.search(valueString, ontologyAcronym);
      if (results.getTotalCount() == 0) {
        Optional<String> prefLabel = metadataField.getPrefLabel();
        if (prefLabel.isPresent()) {
          var prefLabelString = prefLabel.get();
          if (!prefLabelString.isEmpty()) {
            results = bioportalService.search(prefLabelString, ontologyAcronym);
          }
        }
      }
      if (results.getTotalCount() > 0) {
        var suggestionSize = coreConfig.getTermSuggestionsListSize();
        var suggestedTerms = collectTopSuggestions(results, suggestionSize, ontologyAcronym);
        if (suggestedTerms.stream().noneMatch((term) -> valueString.equalsIgnoreCase(term.getLabel()))) {
          var report = EvaluationReportItem.create(
              MetadataIssue.create(
                  IssueCategory.VALUE_ERROR,
                  IssueType.VALUE_NOT_ONTOLOGY_TERM,
                  GeneralUtil.generateFullPathDotNotation(metadataField),
                  valueString),
              RepairAction.ofReplaceMetadataValueWithStandardizedValue(suggestedTerms));
          return Optional.of(report);
        }
      }
    }
    return Optional.empty();
  }

  private ImmutableList<SuggestedOntologyTerm> collectTopSuggestions(BpPagedResults<BpClass> results,
                                                                     int suggestionSize,
                                                                     String ontologyAcronym) {
    return results.getCollection().stream()
        .limit(suggestionSize)
        .map(bpClass -> SuggestedOntologyTerm.create(
            bpClass.getId(),
            bpClass.getPrefLabel(),
            ontologyAcronym))
        .collect(ImmutableList.toImmutableList());
  }
}