package com.ngini.music.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
  
  private MusicFilePopulator populator;
  private MusicFieldClearer clearer;
  private MusicFieldUpdater updater;
  private MusicFileSaver saver;
  
  public MusicModifierService() {
    populator = new MusicFilePopulator();
    clearer = new MusicFieldClearer();
    updater = new MusicFieldUpdater();
    saver = new MusicFileSaver();
  }
  
  public void run() {
    System.out.print("Please provide the path to a folder containing the music files: ");
    handleInput(System.in);
    log.info("Exiting....");
  }

  public void handleInput(InputStream in) {
    Scanner keyboard = new Scanner(in);
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
  }

  public boolean handleUpdateSelectFields(Scanner scanner, List<Mp3File> mp3Files) {
    System.out.print("Would you like to update any MP3 fields? (Y/N): ");
    boolean updateSelectFields = positiveResponse(scanner);
    if (updateSelectFields) {
      MusicFields fields = getMusicFields(scanner);
      if (fields.isEmpty()) {
        return false;
      }
      getUpdater().setFields(mp3Files, fields);
    }
    return updateSelectFields;
  }

  public MusicFields getMusicFields(Scanner scanner) {
    String albumArtist = getInput("Album artist", scanner);
    String album = getInput("Album", scanner);
    System.out.print(String.format("[%s]: ", "String to strip from titles"));
    String removeFromTitle =  scanner.nextLine();
    String contributingArtist = getInput("Contributing artist", scanner);
    String year = getInput("Album Year", scanner);
    String genreDescription = getInput("Album Genre", scanner);
    // Disabled since album art set is not working.
    String albumArtFilePath = "" ; //getInput("Path to album art", keyboard);
    return MusicFieldsFactory.getMusicFields(
        albumArtist, album, contributingArtist, year,
        genreDescription, albumArtFilePath, removeFromTitle);
  }

  public boolean handleUnwantedFields(Scanner scanner, List<Mp3File> mp3Files) {
    boolean clearUnwantedFields = false;
    if (getClearer().unwantedFieldsArePopulated(mp3Files)) {
      System.out.print("Unwanted fields can be cleared out. Continue? (Y/N): ");
      clearUnwantedFields = positiveResponse(scanner);
      if (clearUnwantedFields) {
        getClearer().clearOutUnwantedFields(mp3Files);
      }
    }
    return clearUnwantedFields;
  }

  public String getInput(String fieldName, Scanner scanner) {
    System.out.print(String.format("[%s]: ", fieldName));
    return scanner.nextLine().trim();
  }

  public boolean positiveResponse(Scanner scanner) {
    String line = scanner.nextLine();
    return line.length() > 0 && Character.toLowerCase(line.trim().charAt(0)) == 'y';
  }

  public MusicFilePopulator getPopulator() {
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
