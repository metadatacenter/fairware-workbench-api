package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.core.util.GeneralUtil;
import org.metadatacenter.fairware.core.util.StringUtil;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.shared.IssueCategory;
import org.metadatacenter.fairware.shared.IssueType;
import org.metadatacenter.fairware.shared.MetadataIssue;
import org.metadatacenter.fairware.shared.OntologyTerm;
import org.metadatacenter.fairware.shared.RepairAction;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ValueFromOntologyClassesChecker {

  public Optional<EvaluationReportItem> checkValueAgainstOntologyClasses(@Nonnull MetadataFieldInfo metadataField,
                                                                         @Nonnull ImmutableList<OntologyTerm> ontologyTerms) {
    Optional<Object> value = metadataField.getValue();
    if (value.isEmpty()) {
      return Optional.empty();
    }
    var valueString = String.valueOf(value.get());
    if (valueString.isEmpty()) {
      return Optional.empty();
    }
    if (ontologyTerms.stream().anyMatch(term -> term.getLabel().equals(valueString))) {
      return Optional.empty();
    }
    var orderedClasses = orderOntologyTermsByLabelSimilarity(ontologyTerms, valueString);
    var report = EvaluationReportItem.create(
        MetadataIssue.create(
            IssueCategory.VALUE_ERROR,
            IssueType.VALUE_NOT_ONTOLOGY_TERM,
            GeneralUtil.generateFullPathDotNotation(metadataField),
            valueString),
        RepairAction.ofReplaceMetadataValueWithStandardizedValue(orderedClasses));
    return Optional.of(report);
  }

  private ImmutableList<OntologyTerm> orderOntologyTermsByLabelSimilarity(ImmutableList<OntologyTerm> ontologyTerms, String inputLabel) {
    var maxDistance = 0;
    var orderedList = Lists.<OntologyTerm>newArrayList();
    for (var candidateTerm : ontologyTerms) {
      var label1 = inputLabel;
      var label2 = candidateTerm.getLabel();
      var distance = FuzzySearch.ratio(StringUtil.basicNormalization(label1), StringUtil.basicNormalization(label2));
      if (distance > maxDistance) {
        orderedList.add(0, candidateTerm);
        maxDistance = distance;
      } else {
        orderedList.add(candidateTerm);
      }
    }
    return ImmutableList.copyOf(orderedList);
  }
}