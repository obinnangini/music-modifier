package com.ngini.music.util;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Genres;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.ngini.music.model.MusicFields;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MusicFieldUpdater {
  
  private static final Logger log      = LoggerFactory.getLogger(MusicFieldUpdater.class);
  
  public static void setFields(List<Mp3File> mp3Files, MusicFields fields) {
    log.info("Updating field values.");
    for (Mp3File mp3File : mp3Files) {
      if (mp3File.hasId3v1Tag()) {
        setFields(mp3File.getId3v1Tag(), fields);
      } else if (mp3File.hasId3v2Tag()) {
        setFields(mp3File.getId3v2Tag(), fields);
      }
    }
    log.info("Updating complete.");
  }

  private static void setFields(ID3v1 tag, MusicFields fields) {
    if (fields.getRemoveFromTitle().length() > 0) {
      String newTitle = Utils.stripSubstringFromText(tag.getTitle(), fields.getRemoveFromTitle());
      tag.setTitle(newTitle);
    }
    if (fields.getAlbum().length() > 0) {
      tag.setAlbum(fields.getAlbum());
    }
    if (fields.getContributingArtist().length() > 0) {
      tag.setArtist(fields.getContributingArtist());
    }
    if (fields.getGenreDescription().length() > 0) {
      tag.setGenre(ID3v1Genres.matchGenreDescription(fields.getGenreDescription()));
    }
    if (fields.getYear().length() > 0) {
      tag.setYear(fields.getYear());
    }
    
    if (tag instanceof ID3v2) {
      ID3v2 id3v2Tag = (ID3v2) tag;
      if (fields.getAlbumArtist().length() > 0) {
        id3v2Tag.setAlbumArtist(fields.getAlbumArtist());
      }

      // Currently not working
      if (fields.getAlbumArt() != null) {
        setAlbumImage(id3v2Tag, fields);
      }
    }
  }

  private static void setAlbumImage(ID3v2 tag, MusicFields fields) {
    try {
      tag.setAlbumImage(
          Files.readAllBytes(fields.getAlbumArt().toPath()),
          Files.probeContentType(fields.getAlbumArt().toPath()));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

}
