package com.ngini.music.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;

public class MusicFileSaver {

  private static final Logger log      = LoggerFactory.getLogger(MusicFileSaver.class);
  
  public static void saveMusicFiles(File parentDir, List<Mp3File> mp3Files, String textToRemoveFromFileName) throws NotSupportedException, IOException {
    if (textToRemoveFromFileName.length() > 0) {
      log.info(String.format("[%s] will be removed from file names if possible", textToRemoveFromFileName));
    }
    log.info(String.format("Updated copies of music files in the folder will be saved at [%s]",
        parentDir.getPath()));
    for (Mp3File mp3File : mp3Files) {
      String fileName = mp3File.getFilename().substring(mp3File.getFilename().lastIndexOf(File.separator) + 1);
      String newFileName = stripTextFromFileName(fileName, textToRemoveFromFileName);
      mp3File.save(parentDir.getAbsolutePath() + File.separator + newFileName);
    }
    log.info(String.format("Save to [%s] complete.", parentDir.getPath()));
  }
  
  public static String stripTextFromFileName(String oldFileName, String textToRemoveFromFileName) {
    String newFileName = oldFileName;
    if (textToRemoveFromFileName.length() > 0) {
      log.trace(String.format("Current fileName: [%s], text to remove: [%s]", oldFileName, textToRemoveFromFileName));
      if (oldFileName.contains(textToRemoveFromFileName)) {
        String strippedName = oldFileName.replace(textToRemoveFromFileName, "");
        if (strippedName.length() > 0) {
          newFileName = strippedName;
          log.trace(String.format("Old file name [%s] will be set to new file name [%s]", oldFileName, newFileName));
        } else {
          log.warn(String.format("Removing [%s] from file name [%s] will leave the file with no name! Skipping...", oldFileName, textToRemoveFromFileName));
        }
      }
    }
    return newFileName;
  }
}
