package com.ngini.music.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
              "Removing [%s] from text [%s] will make string empty! Skipping...",
              text, substringToRemove));
        }
      }
    }
    return newFileName;
  }
  
  
  public static void printAsciiArt() {
    Path asciiArtPath = Paths.get("src/main/resources", "asciiArt.txt");
    try {
      List<String> lines = Files.readAllLines(asciiArtPath);
      for (String string : lines) {
        System.out.println(string);
      }
    } catch (IOException e1) { }
   
//    try(BufferedReader in = new BufferedReader(new InputStreamReader(
//        MusicModifierApplication.class.getClassLoader().getResourceAsStream("asciiArt.txt")))) {
//      String line = null;
//      while ((line = in.readLine()) != null) {
//        System.out.println(line);
//      }
//    } catch (IOException e1) { }
  }
}
