package org.metadatacenter.fairware.core.services;

import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.EvaluationReportItem;
import org.metadatacenter.fairware.api.response.evaluationReport.EvaluationReportResponse;
import org.metadatacenter.fairware.api.response.search.SearchMetadataResponse;
import org.metadatacenter.fairware.api.shared.FieldAlignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMetadataService {

  List<FieldAlignment> alignMetadata(String templateId, Map<String, Object> metadataRecord) throws IOException, HttpException;
  EvaluateMetadataResponse evaluateMetadata(String metadataRecordId, String templateId, Map<String, Object> metadataRecord, List<FieldAlignment> fieldAlignments) throws HttpException, IOException;
  List<EvaluationReportItem> evaluateMetadata(String templateId, Map<String, Object> metadataRecord, List<FieldAlignment> fieldAlignments) throws HttpException, IOException;
  SearchMetadataResponse searchMetadata(List<String> dois) throws IOException, HttpException;
  EvaluationReportResponse generateEvaluationReport(List<EvaluateMetadataResponse> evaluationResults);
}
