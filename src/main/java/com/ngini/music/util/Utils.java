package com.ngini.music.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
  
  private static final Logger log = LoggerFactory.getLogger(Utils.class);

  public static String stripSubstringFromText(String text, String substringToRemove) {
    String newFileName = text;
    if (substringToRemove.length() > 0) {
      log.trace(String.format(
          "Current text: [%s], text to remove: [%s]",
          text, substringToRemove));
      if (text.contains(substringToRemove)) {
        String strippedName = text.replace(substringToRemove, "");
        if (strippedName.length() > 0) {
          newFileName = strippedName;
          log.trace(String.format(
              "Old text [%s] will be set to new text [%s]",
              text, newFileName));
        } else {
          log.warn(String.format(
              "Removing [%s] from text [%s] will string empty! Skipping...",
              text, substringToRemove));
        }
      }
    }
    return newFileName;
  }
}
