package org.metadatacenter.fairware.core.services.evaluation;

import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.evaluation.EvaluationReportItem;
import org.metadatacenter.fairware.core.domain.CedarTemplateField;
import org.metadatacenter.fairware.shared.FieldAlignment;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.MetadataFieldInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMetadataEvaluator {

  List<EvaluationReportItem> evaluateMetadata(Map<String, MetadataFieldInfo> metadataFieldsMap,
                                              ImmutableMap<String, CedarTemplateField> templateFieldMap,
                                              List<FieldAlignment> fieldAlignments)
      throws HttpException, IOException;

}
