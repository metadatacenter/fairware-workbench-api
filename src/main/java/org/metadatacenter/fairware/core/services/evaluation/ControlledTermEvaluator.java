package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateField;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ControlledTermEvaluator implements IMetadataEvaluator {

  private final ValueFromOntologyChecker valueFromOntologyChecker;

  public ControlledTermEvaluator(@Nonnull ValueFromOntologyChecker valueFromOntologyChecker) {
    this.valueFromOntologyChecker = checkNotNull(valueFromOntologyChecker);
  }

  @Override
  public List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldsMap,
                                                     ImmutableMap<String, TemplateField> templateFieldMap,
                                                     List<FieldAlignment> fieldAlignments) throws HttpException, IOException {
    var reportItems = Lists.<EvaluationReportItem>newArrayList();
    for (var fieldAlignment : fieldAlignments) {
      if (metadataFieldsMap.containsKey(fieldAlignment.getMetadataFieldPath())
          && templateFieldMap.containsKey(fieldAlignment.getTemplateFieldPath())) {
        var metadataFieldInfo = metadataFieldsMap.get(fieldAlignment.getMetadataFieldPath());
        var templateField = templateFieldMap.get(fieldAlignment.getTemplateFieldPath());
        var templateFieldSpecification = templateField.valueField();
        var valueConstraints = templateFieldSpecification.getValueConstraints();
        if (valueConstraints.isPresent()) {
          for (var valueConstraint : valueConstraints.get()) {
            var kind = valueConstraint.getKind();
            switch (kind) {
              case ONTOLOGY:
                valueFromOntologyChecker.checkValueAgainstOntology(metadataFieldInfo, valueConstraint.ontology())
                    .ifPresent(reportItems::add);
                break;
            }
          }
        }
      }
    }
    return ImmutableList.copyOf(reportItems);
  }
}
