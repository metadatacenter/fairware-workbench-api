package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.constants.CedarConstants;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.metadatacenter.fairware.constants.CedarModelConstants.IS_BASED_ON;

public class CedarUtil {

  /**
   * Checks if a URI corresponds to a CEDAR template instance identifier
   * Example: https://repo.metadatacenter.org/template-instances/8e21af07-33ca-4ce3-8e84-d4e3fbe017b6
   *
   * @param uri
   * @return
   */
  public static boolean isCedarTemplateInstanceId(String uri) {
    return Pattern.matches(CedarConstants.CEDAR_TEMPLATE_INSTANCE_URI_REGEX, uri);
  }

  @Nonnull
  public static Optional<String> getTemplateId(Map<String, Object> templateInstance) {
    var templateId = Optional.<String>empty();
    if (templateInstance.containsKey(IS_BASED_ON)) {
      templateId = Optional.of(templateInstance.get(IS_BASED_ON).toString());
    }
    return templateId;
  }
}
