package org.metadatacenter.fairware.constants;

public class CedarConstants {

  private CedarConstants() {}

  public final static String CEDAR_SYSTEM_NAME = "CEDAR";

  public final static String CEDAR_URI_REGEX = "https\\:\\/\\/[a-zA-Z\\.]+metadatacenter\\.org[x]*\\.*";
  public final static String CEDAR_TEMPLATE_INSTANCE_URI_REGEX = CEDAR_URI_REGEX + "\\/template-instances\\/.*";

  // Endpoint paths
  public final static String CEDAR_PATH_TEMPLATES = "templates/";
  public final static String CEDAR_PATH_TEMPLATE_INSTANCES = "template-instances/";
  public final static String CEDAR_PATH_RECOMMEND_TEMPLATES = "templates/recommend/";


}
