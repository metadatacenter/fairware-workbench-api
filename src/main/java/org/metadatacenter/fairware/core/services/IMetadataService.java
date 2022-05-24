package org.metadatacenter.fairware.core.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpException;
import org.metadatacenter.fairware.api.response.EvaluateMetadataResponse;
import org.metadatacenter.fairware.api.response.evaluationReport.EvaluationReportResponse;
import org.metadatacenter.fairware.api.response.search.SearchMetadataResponse;
import org.metadatacenter.fairware.api.shared.FieldAlignment;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Optional;

public interface IMetadataService {

  @Nonnull
  ImmutableList<FieldAlignment> alignMetadata(@Nonnull String templateId,
                                              @Nonnull ImmutableMap<String, Object> metadataRecord) throws IOException, HttpException;

  @Nonnull
  EvaluateMetadataResponse evaluateMetadata(@Nonnull Optional<String> metadataRecordId,
                                            @Nonnull ImmutableMap<String, Object> metadataRecord,
                                            @Nonnull String templateId,
                                            @Nonnull ImmutableList<FieldAlignment> fieldAlignments) throws HttpException, IOException;

  @Nonnull
  SearchMetadataResponse searchMetadata(@Nonnull ImmutableList<String> urls) throws IOException, HttpException;

  @Nonnull
  EvaluationReportResponse generateEvaluationReport(@Nonnull ImmutableList<EvaluateMetadataResponse> evaluationResults);
}
