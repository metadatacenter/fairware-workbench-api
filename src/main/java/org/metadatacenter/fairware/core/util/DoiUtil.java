package org.metadatacenter.fairware.core.util;

public class DoiUtil {

  final static String[] prefixes = new String[]{"https://doi.org/", "doi:"};

  public static String normalizeDoi(String doi) {
    for (String prefix : prefixes) {
      if (doi.startsWith(prefix)) {
        return doi.substring(prefix.length());
      }
    }
    return doi;
  }

}
