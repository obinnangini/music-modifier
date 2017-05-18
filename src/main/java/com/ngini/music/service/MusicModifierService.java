package com.ngini.music.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.ngini.music.model.MusicFields;
import com.ngini.music.model.MusicFieldsFactory;

public class MusicModifierService {

  private static final Logger log = LoggerFactory.getLogger(MusicModifierService.class);
  
  private MusicFilePopulater populator;
  private MusicFieldClearer clearer;
  private MusicFieldUpdater updater;
  private MusicFileSaver saver;
  
  public MusicModifierService() {
    populator = new MusicFilePopulater();
    clearer = new MusicFieldClearer();
    updater = new MusicFieldUpdater();
    saver = new MusicFileSaver();
  }
  
  public void run() {
    System.out.print("Please provide the path to a folder containing the music files: ");
    Scanner keyboard = new Scanner(System.in);
    String line = keyboard.nextLine();
    String filePath = line.trim();
    String trimmedFilePath = filePath.replaceAll("^\"|\"$", "");
    File musicFolder = new File(trimmedFilePath);
    if (musicFolder.exists()) {
      if (musicFolder.isDirectory()) {
        try {
          List<Mp3File> mp3Files = getPopulator().getMusicFiles(musicFolder);
          boolean clearUnwantedFields = handleUnwantedFields(keyboard, mp3Files);
          boolean updateSelectFields = handleUpdateSelectFields(keyboard, mp3Files);
          
          String textToRemoveFromFileNames = "";
          System.out.print("Would you like to strip text from the file names? (Y/N): ");
          boolean updateFileNames = positiveResponse(keyboard);
          if (updateFileNames) {
            System.out.print("String to strip from file names: ");
            textToRemoveFromFileNames = keyboard.nextLine();
          }
          
          if (clearUnwantedFields || updateSelectFields || updateFileNames) {
            File modifiedFolder = new File(musicFolder, "modified");
            if (modifiedFolder.mkdir()) {
              getSaver().saveMusicFiles(modifiedFolder, mp3Files, textToRemoveFromFileNames);
            } else {
              log.error(String.format("Error. Could not create directory [%s]", modifiedFolder.getAbsolutePath()));
            }
          }

        } catch (IOException | NotSupportedException e) {
          log.error(e.getMessage(), e);
        }
      } else {
        log.error(String.format("[%s] specified is not a directory!", musicFolder));
      }
    } else {
      log.error(String.format("[%s] specified does not exist!", musicFolder));
    }
    keyboard.close();
    log.info("Exiting....");
  }

  private boolean handleUpdateSelectFields(Scanner keyboard, List<Mp3File> mp3Files) {
    System.out.print("Would you like to update any MP3 fields? (Y/N): ");
    boolean updateSelectFields = positiveResponse(keyboard);
    if (updateSelectFields) {
      MusicFields fields = getMusicFields(keyboard);
      if (fields.isEmpty()) {
        return false;
      }
      getUpdater().setFields(mp3Files, fields);
    }
    return updateSelectFields;
  }

  private MusicFields getMusicFields(Scanner keyboard) {
    String albumArtist = getInput("Album artist", keyboard);
    String album = getInput("Album", keyboard);
    System.out.print(String.format("[%s]: ", "String to strip from titles"));
    String removeFromTitle =  keyboard.nextLine();
    String contributingArtist = getInput("Contributing artist", keyboard);
    String year = getInput("Album Year", keyboard);
    String genreDescription = getInput("Album Genre", keyboard);
    // Disabled since album art set is not working.
    String albumArtFilePath = "" ; //getInput("Path to album art", keyboard);
    MusicFields fields = MusicFieldsFactory.getMusicFields(
        albumArtist, album, contributingArtist, year,
        genreDescription, albumArtFilePath, removeFromTitle);
    return fields;
  }

  private boolean handleUnwantedFields(Scanner keyboard, List<Mp3File> mp3Files) {
    boolean clearUnwantedFields = false;
    if (getClearer().unwantedFieldsArePopulated(mp3Files)) {
      System.out.print("Unwanted fields can be cleared out. Continue? (Y/N): ");
      clearUnwantedFields = positiveResponse(keyboard);
      if (clearUnwantedFields) {
        getClearer().clearOutUnwantedFields(mp3Files);
      }
    }
    return clearUnwantedFields;
  }

  private String getInput(String fieldName, Scanner keyboard) {
    System.out.print(String.format("[%s]: ", fieldName));
    return keyboard.nextLine().trim();
  }

  private boolean positiveResponse(Scanner keyboard) {
    String line = keyboard.nextLine();
    return line.length() > 0 && Character.toLowerCase(line.trim().charAt(0)) == 'y';
  }

  public MusicFilePopulater getPopulator() {
    return populator;
  }

  public MusicFieldClearer getClearer() {
    return clearer;
  }

  public MusicFieldUpdater getUpdater() {
    return updater;
  }

  public MusicFileSaver getSaver() {
    return saver;
  }
  
}
