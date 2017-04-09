package com.ngini.music.model;

import com.mpatric.mp3agic.ID3v1Genres;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    if (year.length() > 0) {
      try {
        Integer.parseInt(year);
        fields.setYear(year);
      } catch (NumberFormatException e) {
        log.error(String.format("[%s] is not a valid Year!", year), e);
      }
    } else {
      fields.setYear(EMPTY_STRING);
    }

    if (genreDescription.length() > 0) {
      if (GENRES.contains(genreDescription)) {
        fields.setGenreDescription(genreDescription);
      } else {
        log.error(String.format("[%s] is not a valid Genre!", genreDescription));
      }
    } else {
      fields.setGenreDescription(EMPTY_STRING);
    }

    if (albumArtFilePath.length() > 0) {
      File file = new File(albumArtFilePath);
      if (file.exists()) {
        String mimetype = new MimetypesFileTypeMap().getContentType(file);
        String type = mimetype.split("/")[0];
        if (type.equals("image")) {
          fields.setAlbumArt(file);
        } else {
          log.warn(String.format(
              "[%s] is not a valid image file! Will not set album art",
              albumArtFilePath));
          fields.setAlbumArt(null);
        }
      } else {
        log.warn(String.format("[%s] does not exist! Will not set album art", albumArtFilePath));
        fields.setAlbumArt(null);
      }
    } else {
      fields.setAlbumArt(null);
    }

    fields.setRemoveFromTitle(removeFromTitle);

    return fields;
  }

}
