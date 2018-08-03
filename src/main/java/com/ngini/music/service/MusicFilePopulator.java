package com.ngini.music.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;


class MusicFilePopulator {

  private static Set<String> EXTENSIONS  = new HashSet<String>(Arrays.asList("mp3"));

  private static final Logger LOGGER = LoggerFactory.getLogger(MusicFilePopulator.class);

  List<Mp3File> getMusicFiles(File dir) throws IOException {
    List<Mp3File> mp3Files = new ArrayList<>();
    LOGGER.info(String.format("Files found in [%s]: ", dir.getPath()));
    for (File file : dir.listFiles()) {
      if (file.isFile() && isMusicFile(file)) {
        LOGGER.info(String.format("[%s]", file.getName()));
        try {
          Mp3File mp3File = new Mp3File(file.getAbsolutePath());
          mp3Files.add(mp3File);
        } catch (UnsupportedTagException | InvalidDataException e) {
          LOGGER.error(e.getMessage(), e);
        }
      }
    }
    return mp3Files;
  }

  private static boolean isMusicFile(File f) {
    return EXTENSIONS.contains(f.getPath().substring(f.getPath().lastIndexOf('.') + 1));
  }


}
