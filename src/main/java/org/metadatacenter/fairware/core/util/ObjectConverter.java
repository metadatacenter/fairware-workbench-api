package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.request.CedarRecommendTemplatesRequest;
import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response.CedarRecommendTemplatesRequestSummary;
import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response.CedarRecommendTemplatesResponse;
import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response.CedarTemplateExtract;
import org.metadatacenter.fairware.api.cedar.resourceserver.recommend.response.CedarTemplateRecommendation;
import org.metadatacenter.fairware.api.recommendation.request.RecommendTemplatesRequest;
import org.metadatacenter.fairware.api.recommendation.response.RecommendTemplatesRequestSummary;
import org.metadatacenter.fairware.api.recommendation.response.RecommendTemplatesResponse;
import org.metadatacenter.fairware.api.recommendation.response.TemplateExtract;
import org.metadatacenter.fairware.api.recommendation.response.TemplateRecommendation;

import java.util.ArrayList;
import java.util.List;

public class ObjectConverter {

  public static CedarRecommendTemplatesRequest recTempRequestToCedarRecTempRequest(RecommendTemplatesRequest r) {
    return new CedarRecommendTemplatesRequest(r.getMetadataRecord());
  }

  public static RecommendTemplatesRequestSummary cedarRecTempReqSumToRecTempReqSum(CedarRecommendTemplatesRequestSummary summary) {
    return new RecommendTemplatesRequestSummary(summary.getSourceFieldsCount());
  }

  public static TemplateExtract cedarTemplateExtToTemplateExt(CedarTemplateExtract cte) {
    return new TemplateExtract()
  }

  public static TemplateRecommendation cedarTempRecToTempRec(CedarTemplateRecommendation ctr) {
    return new TemplateRecommendation(
        ctr.getRecommendationScore(),
        ctr.getSourceFieldsMatched(),
        ctr.getTargetFieldsCount(),
        cedarTemplateExtToTemplateExt(ctr.getTemplateExtract()));
  }

  public static List<TemplateRecommendation> cedarTempRecListToTempRecList(List<CedarTemplateRecommendation> ctrs) {
    List<TemplateRecommendation> trs = new ArrayList<>();
    for (CedarTemplateRecommendation ctr : ctrs) {
      trs.add(cedarTempRecToTempRec(ctr));
    }
    return trs;
  }

  public static RecommendTemplatesResponse cedarRecTempResponseToRecTempResponse(CedarRecommendTemplatesResponse cr) {

    return new RecommendTemplatesResponse(
        cr.getTotalCount(),
        cedarRecTempReqSumToRecTempReqSum(cr.getRequestSummary()),
        cedarTempRecListToTempRecList(cr.getRecommendations()));
  }

}
