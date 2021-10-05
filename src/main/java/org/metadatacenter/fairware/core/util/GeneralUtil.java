package org.metadatacenter.fairware.core.util;

import org.metadatacenter.fairware.core.domain.MetadataFieldInfo;
import org.metadatacenter.fairware.core.domain.TemplateNodeInfo;

import java.util.List;

public class GeneralUtil {

  /**
   * Generates the full path or a field or a node using dot notation. Dots are first removed from the field keys to avoid confusion
   * @return The full node path in dot notation (includes the node name)
   */
  /**
   * Based on a path and a name, generates the full path in dot notation. Dots are first removed from the name and path
   * fragments to ensure that the final path is not ambiguous
   * @param path
   * @param name
   * @return
   */
  public static String generateFullPathDotNotation(List<String> path, String name) {
    StringBuilder pathSb = new StringBuilder();
    for (int i=0; i<path.size(); i++) {
      pathSb.append(path.get(i).replace(".", "").trim()).append(".");
    }
    return pathSb.append(name.replace(".", "")).toString();
  }

  public static String generateFullPathDotNotation(TemplateNodeInfo templateNodeInfo) {
    return (generateFullPathDotNotation(templateNodeInfo.getPath(), templateNodeInfo.getName()));
  }

  public static String generateFullPathDotNotation(MetadataFieldInfo metadataFieldInfo) {
    return (generateFullPathDotNotation(metadataFieldInfo.getPath(), metadataFieldInfo.getName()));
  }

}
