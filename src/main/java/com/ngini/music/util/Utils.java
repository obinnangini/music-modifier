package com.ngini.music.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

public class Utils {

  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  public static String stripSubstringFromText(String text, String substringToRemove) {
    if (text == null) {
      return "";
    }
    String newFileName = text;
    if (substringToRemove.length() > 0) {
      LOGGER.trace(String.format(
          "Current text: [%s], text to remove: [%s]",
          text, substringToRemove));
      if (text.contains(substringToRemove)) {
        String strippedName = text.replace(substringToRemove, "");
        if (strippedName.length() > 0) {
          newFileName = strippedName;
          LOGGER.trace(String.format(
              "Old text [%s] will be set to new text [%s]",
              text, newFileName));
        } else {
          LOGGER.warn(String.format(
              "Removing [%s] from text [%s] will make string empty! Skipping...",
              text, substringToRemove));
        }
      }
    }
    return newFileName;
  }


  public static void printAsciiArt() {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(
        Utils.class.getClassLoader().getResourceAsStream("asciiArt.txt")))) {
      String line;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e1) { }
  }

  private void logDetails(Mp3File mp3file) {
    if (mp3file.hasId3v2Tag()) {
      logDetails(mp3file.getId3v2Tag());
    } else  if (mp3file.hasId3v1Tag()) {
      logDetails(mp3file.getId3v1Tag());
    }
  }

  private void logDetails(ID3v2 tag) {
    LOGGER.info("Track: " + tag.getTrack());
    LOGGER.info("Artist: " + tag.getArtist());
    LOGGER.info("Title: " + tag.getTitle());
    LOGGER.info("Album: " + tag.getAlbum());
    LOGGER.info("Year: " + tag.getYear());
    LOGGER.info("Genre: " + tag.getGenre() + " (" + tag.getGenreDescription() + ")");
    LOGGER.info("Comment: " + tag.getComment());

    LOGGER.info("Lyrics: " + tag.getLyrics());
    LOGGER.info("Composer: " + tag.getComposer());
    LOGGER.info("Publisher: " + tag.getPublisher());
    LOGGER.info("Original artist: " + tag.getOriginalArtist());
    LOGGER.info("Album artist: " + tag.getAlbumArtist());
    LOGGER.info("Copyright: " + tag.getCopyright());
    LOGGER.info("URL: " + tag.getUrl());
    LOGGER.info("Encoder: " + tag.getEncoder());
    byte[] albumImageData = tag.getAlbumImage();
    if (albumImageData != null) {
      LOGGER.info("Have album image data, length: " + albumImageData.length + " bytes");
      LOGGER.info("Album image mime type: " + tag.getAlbumImageMimeType());
    }
  }

  private void logDetails(ID3v1 tag) {
    LOGGER.info("Track: " + tag.getTrack());
    LOGGER.info("Artist: " + tag.getArtist());
    LOGGER.info("Title: " + tag.getTitle());
    LOGGER.info("Album: " + tag.getAlbum());
    LOGGER.info("Year: " + tag.getYear());
    LOGGER.info("Genre: " + tag.getGenre() + " (" + tag.getGenreDescription() + ")");
    LOGGER.info("Comment: " + tag.getComment());
  }
}
