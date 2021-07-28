package org.metadatacenter.fairware.core.util;

import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;

public class StringUtil {

  private StringUtil() {
  }

  public static String stripAccents(String str) {
    if (str == null) {
      return null;
    }
    String s = Normalizer.normalize(str, Normalizer.Form.NFD);
    return s.replaceAll("\\p{M}", "");
  }

  public static String removeSpecialCharacters(String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("[^a-zA-Z0-9]", " ");
  }

  public static String normalizeSpace(String str) {
    if (str == null) {
      return null;
    }
    return StringUtils.normalizeSpace(str);
  }

  public static String basicNormalization(String str) {
    if (str == null) {
      return null;
    }
    return normalizeSpace(removeSpecialCharacters(stripAccents(str.toLowerCase())));
  }
}




