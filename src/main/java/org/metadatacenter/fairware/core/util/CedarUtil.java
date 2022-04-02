package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.constants.CedarConstants;
import org.metadatacenter.fairware.constants.CedarModelConstants;

import java.util.Map;
import java.util.regex.Pattern;

public class CedarUtil {

  /**
   * Checks if a URI corresponds to a CEDAR template instance identifier
   * Example: https://repo.metadatacenter.org/template-instances/8e21af07-33ca-4ce3-8e84-d4e3fbe017b6
   * @param uri
   * @return
   */
  public static boolean isCedarTemplateInstanceId(String uri) {
    return Pattern.matches(CedarConstants.CEDAR_TEMPLATE_INSTANCE_URI_REGEX, uri);
  }

  public static String getTemplateId(Map<String, Object> templateInstance) {
    if (!templateInstance.containsKey(CedarModelConstants.IS_BASED_ON)) {
      return null;
    }
    return templateInstance.get(CedarModelConstants.IS_BASED_ON).toString();
  }
}
