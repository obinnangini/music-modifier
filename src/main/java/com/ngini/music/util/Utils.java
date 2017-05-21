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
    try(BufferedReader in = new BufferedReader(new InputStreamReader(
        Utils.class.getClassLoader().getResourceAsStream("asciiArt.txt")))) {
      String line = null;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e1) { }
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
