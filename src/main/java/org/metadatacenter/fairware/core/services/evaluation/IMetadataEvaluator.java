package org.metadatacenter.fairware.core.services.evaluation;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.shared.FieldAlignment;
import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;
import org.metadatacenter.fairware.core.util.cedar.extraction.model.InfoField;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMetadataEvaluator {

  List<EvaluationReportItem> evaluateMetadata(Map<String, InfoField> metadataFieldsMap,
                                              Map<String, TemplateNodeInfo> templateFieldsMap,
                                              List<FieldAlignment> fieldAlignments)
      throws HttpException, IOException;

}
