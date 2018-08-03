package com.ngini.music.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Genres;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.ngini.music.model.MusicFields;
import com.ngini.music.util.Utils;


class MusicFieldUpdater {

  private static final Logger LOGGER = LoggerFactory.getLogger(MusicFieldUpdater.class);

  void setFields(List<Mp3File> mp3Files, MusicFields fields) {
    LOGGER.info("Updating field values.");
    for (Mp3File mp3File : mp3Files) {
      if (mp3File.hasId3v2Tag()) {
        setFields(mp3File.getId3v2Tag(), fields);
      } else if (mp3File.hasId3v1Tag()) {
        setFields(mp3File.getId3v1Tag(), fields);
      }
    }
    LOGGER.info("Updating complete.");
  }

  void setFields(ID3v2 tag, MusicFields fields) {
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

    if (fields.getAlbumArtist().length() > 0) {
      tag.setAlbumArtist(fields.getAlbumArtist());
    }

    // Currently not working
    if (fields.getAlbumArt() != null) {
      setAlbumImage(tag, fields);
    }
  }

  void setAlbumImage(ID3v2 tag, MusicFields fields) {
    try {
      tag.setAlbumImage(
        Files.readAllBytes(fields.getAlbumArt().toPath()),
        Files.probeContentType(fields.getAlbumArt().toPath()));
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  void setFields(ID3v1 tag, MusicFields fields) {
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
  }

}
