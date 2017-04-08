package com.ngini.music.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MusicFilePopulater {

  private static Set<String>  EXTENSIONS  = new HashSet<String>(Arrays.asList("mp3"));

  private static final Logger log      = LoggerFactory.getLogger(MusicFilePopulater.class);

  public static List<Mp3File> getMusicFiles(File dir) throws IOException {
    List<Mp3File> mp3Files = new ArrayList<>();
    log.info(String.format("Files found in [%s]: ", dir.getPath()));
    for (File file : dir.listFiles()) {
      if (file.isFile() && isMusicFile(file)) {
        log.info(String.format("[%s]", file.getName()));
        try {
          Mp3File mp3File = new Mp3File(file.getAbsolutePath());
          mp3Files.add(mp3File);
        } catch (UnsupportedTagException | InvalidDataException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return mp3Files;
  }
  
  private static boolean isMusicFile(File f) {
    return EXTENSIONS.contains(f.getPath().substring(f.getPath().lastIndexOf('.') + 1));
  }

  private void logDetails(Mp3File mp3file) {
    if (mp3file.hasId3v1Tag()) {
      logDetails(mp3file.getId3v1Tag());
    } else if (mp3file.hasId3v2Tag()) {
      logDetails(mp3file.getId3v2Tag());
    }
  }

  private void logDetails(ID3v1 tag) {
    log.info("Track: " + tag.getTrack());
    log.info("Artist: " + tag.getArtist());
    log.info("Title: " + tag.getTitle());
    log.info("Album: " + tag.getAlbum());
    log.info("Year: " + tag.getYear());
    log.info("Genre: " + tag.getGenre() + " (" + tag.getGenreDescription() + ")");
    log.info("Comment: " + tag.getComment());
    
    if (tag instanceof ID3v2) {
      ID3v2 id3v2Tag = (ID3v2) tag;
      log.info("Lyrics: " + id3v2Tag.getLyrics());
      log.info("Composer: " + id3v2Tag.getComposer());
      log.info("Publisher: " + id3v2Tag.getPublisher());
      log.info("Original artist: " + id3v2Tag.getOriginalArtist());
      log.info("Album artist: " + id3v2Tag.getAlbumArtist());
      log.info("Copyright: " + id3v2Tag.getCopyright());
      log.info("URL: " + id3v2Tag.getUrl());
      log.info("Encoder: " + id3v2Tag.getEncoder());
      byte[] albumImageData = id3v2Tag.getAlbumImage();
      if (albumImageData != null) {
        log.info("Have album image data, length: " + albumImageData.length + " bytes");
        log.info("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
      }
    }
  }

}
