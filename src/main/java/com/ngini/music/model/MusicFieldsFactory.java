package com.ngini.music.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1Genres;


public class MusicFieldsFactory {

  private static final String EMPTY_STRING = "";

  private static final List<String> GENRES = Arrays.asList(ID3v1Genres.GENRES);

  private static final Logger log = LoggerFactory.getLogger(MusicFieldsFactory.class);

  public static MusicFields getMusicFields(String albumArtist, String album, 
      String contributingArtist, String year,
      String genreDescription, String albumArtFilePath, String removeFromTitle) {
    MusicFields fields = new MusicFields();

    fields.setAlbumArtist(albumArtist);
    fields.setAlbum(album);
    fields.setContributingArtist(contributingArtist);
    fields.setRemoveFromTitle(removeFromTitle);
    
    fields.setAlbumArt(getAlbumFilePath(albumArtFilePath));
    fields.setYear(getYear(year));
    fields.setGenreDescription(getGenreDesc(genreDescription));

    return fields;
  }

  public static File getAlbumFilePath(String albumArtFilePath) {
    if (albumArtFilePath.length() > 0) {
      File file = new File(albumArtFilePath);
      if (file.exists()) {
        String mimeType = new MimetypesFileTypeMap().getContentType(file);
        String type = mimeType.split("/")[0];
        if (type.equals("image")) {
          return file;
        } else {
          log.warn(String.format(
              "[%s] is not a valid image file! Will not set album art",
              albumArtFilePath));
        }
      } else {
        log.warn(String.format("[%s] does not exist! Will not set album art", albumArtFilePath));
      }
    }
    return null;
  }

  public static String getGenreDesc(String genreDescription) {
    if (genreDescription.length() > 0) {
      if (GENRES.contains(genreDescription)) {
        return genreDescription;
      } else {
        log.error(String.format("[%s] is not a valid Genre!", genreDescription));
      }
    }
    return EMPTY_STRING;
  }

  public static String getYear(String year) {
    if (year.length() > 0) {
      try {
        Integer.parseInt(year);
        return year;
      } catch (NumberFormatException e) {
        log.error(String.format("[%s] is not a valid Year!", year), e);
      }
    }
    return EMPTY_STRING;
  }

}
