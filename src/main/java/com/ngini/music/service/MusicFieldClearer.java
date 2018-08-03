package com.ngini.music.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;


class MusicFieldClearer {

  private static final String BLANK_VALUE = " ";

  private static final Logger LOGGER = LoggerFactory.getLogger(MusicFieldClearer.class);

  void clearOutUnwantedFields(List<Mp3File> mp3Files) {
    LOGGER.info("Clearing out unwanted fields.");
    for (Mp3File mp3File : mp3Files) {
      if (mp3File.hasId3v1Tag()) {
        clearOutUnwantedFields(mp3File.getId3v1Tag());
      } else if (mp3File.hasId3v2Tag()) {
        clearOutUnwantedFields(mp3File.getId3v2Tag());
      }
    }
    LOGGER.info("Unwanted fields cleared.");
  }

  void clearOutUnwantedFields(ID3v1 tag) {
    tag.setComment(BLANK_VALUE);

    if (tag instanceof ID3v2) {
      ID3v2 id3v2Tag = (ID3v2) tag;

      id3v2Tag.setArtistUrl(BLANK_VALUE);

      id3v2Tag.setEncoder(BLANK_VALUE);
      id3v2Tag.setComposer(BLANK_VALUE);
      id3v2Tag.setCompilation(false);
      id3v2Tag.setItunesComment(BLANK_VALUE);
      id3v2Tag.setCopyright(BLANK_VALUE);
      id3v2Tag.setCopyrightUrl(BLANK_VALUE);
      id3v2Tag.setPaymentUrl(BLANK_VALUE);
      id3v2Tag.setPublisher(BLANK_VALUE);
      id3v2Tag.setPublisherUrl(BLANK_VALUE);
      // id3v2Tag.setGenreDescription(null);
    }
  }


  boolean unwantedFieldsArePopulated(List<Mp3File> mp3Files) {
    boolean unwantedFieldsPopulated = false;
    for (Mp3File mp3File : mp3Files) {
      if (mp3File.hasId3v1Tag()) {
        boolean unwanted = unwantedFieldsArePopulated(mp3File.getId3v1Tag());
        unwantedFieldsPopulated = unwantedFieldsPopulated || unwanted;
      } else if (mp3File.hasId3v2Tag()) {
        boolean unwanted = unwantedFieldsArePopulated(mp3File.getId3v2Tag());
        unwantedFieldsPopulated = unwantedFieldsPopulated || unwanted;
      } else {
        LOGGER.error(String.format("No music tag found for file: %s", mp3File.getFilename()));
      }
    }
    return unwantedFieldsPopulated;
  }

  boolean unwantedFieldsArePopulated(ID3v1 tag) {
    boolean unwantedFieldsPopulated;
    LOGGER.trace("--------------------------------------------");
    boolean fieldPopulated = checkIfPopulated("Comment", tag.getComment());
    unwantedFieldsPopulated = fieldPopulated;

    if (tag instanceof ID3v2) {
      ID3v2 id3v2Tag = (ID3v2) tag;
      fieldPopulated = checkIfPopulated("Copyright", id3v2Tag.getCopyright());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Copyright URL", id3v2Tag.getCopyrightUrl());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Publisher", id3v2Tag.getPublisher());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Publisher URL", id3v2Tag.getPublisherUrl());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Artist URL", id3v2Tag.getArtistUrl());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Encoder", id3v2Tag.getEncoder());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Composer", id3v2Tag.getComposer());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("iTunes Comment", id3v2Tag.getItunesComment());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfTrue("Part of a Compilation", id3v2Tag.isCompilation());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Payment URL", id3v2Tag.getPaymentUrl());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
      fieldPopulated = checkIfPopulated("Genre Description",id3v2Tag.getGenreDescription());
      unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    }
    LOGGER.trace("--------------------------------------------");
    return unwantedFieldsPopulated;
  }

  boolean checkIfPopulated(String fieldName, String fieldValue) {
    if (fieldValue != null && fieldValue.trim().length() > 0) {
      LOGGER.trace(String.format("[%s] has a value: [%s]", fieldName, fieldValue));
      return true;
    }
    return false;
  }

  boolean checkIfTrue(String fieldName, boolean fieldValue) {
    if (fieldValue) {
      LOGGER.trace(String.format("[%s] is true", fieldName));
      return true;
    }
    return false;
  }

}
