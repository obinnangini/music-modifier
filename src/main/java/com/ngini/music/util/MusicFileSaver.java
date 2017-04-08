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
  
  public static void saveMusicFiles(File parentDir, List<Mp3File> mp3Files) throws NotSupportedException, IOException {
    log.info(String.format("Updated copies of music files in the folder will be saved at [%s]",
        parentDir.getPath()));
    for (Mp3File mp3File : mp3Files) {
      String fileName = mp3File.getFilename().substring(mp3File.getFilename().lastIndexOf(File.separator) + 1);
      mp3File.save(parentDir.getAbsolutePath() + File.separator + fileName);
    }
    log.info(String.format("Save to [%s] complete.",parentDir.getPath()));
  }
}
