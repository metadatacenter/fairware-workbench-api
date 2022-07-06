package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.TemplateField;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMetadataEvaluator {

  List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldsMap,
                                              ImmutableMap<String, TemplateField> templateFieldMap,
                                              List<FieldAlignment> fieldAlignments)
      throws HttpException, IOException;

}
