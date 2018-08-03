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
      if (mp3File.hasId3v2Tag()) {
        clearOutUnwantedFields(mp3File.getId3v2Tag());
      } else if (mp3File.hasId3v1Tag()) {
        clearOutUnwantedFields(mp3File.getId3v1Tag());
      }
    }
    LOGGER.info("Unwanted fields cleared.");
  }

  void clearOutUnwantedFields(ID3v2 tag) {
    tag.setComment(BLANK_VALUE);
    tag.setArtistUrl(BLANK_VALUE);
    tag.setEncoder(BLANK_VALUE);
    tag.setComposer(BLANK_VALUE);
    tag.setCompilation(false);
    tag.setItunesComment(BLANK_VALUE);
    tag.setCopyright(BLANK_VALUE);
    tag.setCopyrightUrl(BLANK_VALUE);
    tag.setPaymentUrl(BLANK_VALUE);
    tag.setPublisher(BLANK_VALUE);
    tag.setPublisherUrl(BLANK_VALUE);
    // tag.setGenreDescription(null);
  }

  void clearOutUnwantedFields(ID3v1 tag) {
    tag.setComment(BLANK_VALUE);
  }

  boolean unwantedFieldsArePopulated(List<Mp3File> mp3Files) {
    boolean unwantedFieldsPopulated = false;
    for (Mp3File mp3File : mp3Files) {

      if (mp3File.hasId3v2Tag()) {
        boolean unwanted = unwantedFieldsArePopulated(mp3File.getId3v2Tag());
        unwantedFieldsPopulated = unwantedFieldsPopulated || unwanted;
      } else  if (mp3File.hasId3v1Tag()) {
        boolean unwanted = unwantedFieldsArePopulated(mp3File.getId3v1Tag());
        unwantedFieldsPopulated = unwantedFieldsPopulated || unwanted;
      }
      else {
        LOGGER.error(String.format("No music tag found for file: %s", mp3File.getFilename()));
      }
    }
    return unwantedFieldsPopulated;
  }

  boolean unwantedFieldsArePopulated(ID3v2 tag) {
    boolean unwantedFieldsPopulated;
    LOGGER.trace("--------------------------------------------");
    boolean fieldPopulated = checkIfPopulated("Comment", tag.getComment());
    unwantedFieldsPopulated = fieldPopulated;
    fieldPopulated = checkIfPopulated("Copyright", tag.getCopyright());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Copyright URL", tag.getCopyrightUrl());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Publisher", tag.getPublisher());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Publisher URL", tag.getPublisherUrl());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Artist URL", tag.getArtistUrl());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Encoder", tag.getEncoder());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Composer", tag.getComposer());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("iTunes Comment", tag.getItunesComment());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfTrue("Part of a Compilation", tag.isCompilation());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Payment URL", tag.getPaymentUrl());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    fieldPopulated = checkIfPopulated("Genre Description",tag.getGenreDescription());
    unwantedFieldsPopulated = unwantedFieldsPopulated || fieldPopulated;
    LOGGER.trace("--------------------------------------------");
    return unwantedFieldsPopulated;
  }

  boolean unwantedFieldsArePopulated(ID3v1 tag) {
    boolean unwantedFieldsPopulated;
    LOGGER.trace("--------------------------------------------");
    unwantedFieldsPopulated = checkIfPopulated("Comment", tag.getComment());
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
