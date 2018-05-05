package com.ngini.music.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.ngini.music.util.Utils;


class MusicFileSaver {

  private static final Logger LOGGER = LoggerFactory.getLogger(MusicFileSaver.class);

  void saveMusicFiles(
    File parentDir, List<Mp3File> mp3Files, String textToRemoveFromFileName)
      throws NotSupportedException, IOException {
    if (textToRemoveFromFileName.length() > 0) {
      LOGGER.info(String.format(
          "[%s] will be removed from file names if possible",
          textToRemoveFromFileName));
    }
    LOGGER.info(String.format("Updated copies of music files in the folder will be saved at [%s]",
        parentDir.getPath()));
    for (Mp3File mp3File : mp3Files) {
      String fileName = mp3File.getFilename()
          .substring(mp3File.getFilename().lastIndexOf(File.separator) + 1);
      String newFileName = Utils.stripSubstringFromText(fileName, textToRemoveFromFileName);
      mp3File.save(parentDir.getAbsolutePath() + File.separator + newFileName);
    }
    LOGGER.info(String.format("Save to [%s] complete.", parentDir.getPath()));
  }


}
