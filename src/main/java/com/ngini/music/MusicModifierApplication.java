package com.ngini.music;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.ngini.music.model.MusicFields;
import com.ngini.music.model.MusicFieldsFactory;
import com.ngini.music.util.MusicFieldClearer;
import com.ngini.music.util.MusicFieldUpdater;
import com.ngini.music.util.MusicFilePopulater;
import com.ngini.music.util.MusicFileSaver;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MusicModifierApplication {
  private static final Logger log = LoggerFactory.getLogger(MusicModifierApplication.class);

  public static void main(String[] args) {
    printAsciiArt();
    log.info("Welcome to the Music Modifier!");
    System.out.print("Please provide the path to a folder containing the music files: ");
    Scanner keyboard = new Scanner(System.in);
    String line = keyboard.nextLine();
    String filePath = line.trim();
    
    File musicFolder = new File(filePath);
    if (musicFolder.exists()) {
      if (musicFolder.isDirectory()) {
        try {
          List<Mp3File> mp3Files = MusicFilePopulater.getMusicFiles(musicFolder);
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
              MusicFileSaver.saveMusicFiles(modifiedFolder, mp3Files, textToRemoveFromFileNames);
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

  private static boolean handleUpdateSelectFields(Scanner keyboard, List<Mp3File> mp3Files) {
    System.out.print("Would you like to update any MP3 fields? (Y/N): ");
    boolean updateSelectFields = positiveResponse(keyboard);
    if (updateSelectFields) {
      MusicFields fields = getMusicFields(keyboard);
      if (fields.isEmpty()) {
        return false;
      }
      MusicFieldUpdater.setFields(mp3Files, fields);
    }
    return updateSelectFields;
  }

  private static MusicFields getMusicFields(Scanner keyboard) {
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

  private static boolean handleUnwantedFields(Scanner keyboard, List<Mp3File> mp3Files) {
    boolean clearUnwantedFields = false;
    if (MusicFieldClearer.unwantedFieldsArePopulated(mp3Files)) {
      System.out.print("Unwanted fields can be cleared out. Continue? (Y/N): ");
      clearUnwantedFields = positiveResponse(keyboard);
      if (clearUnwantedFields) {
        MusicFieldClearer.clearOutUnwantedFields(mp3Files);
      }
    }
    return clearUnwantedFields;
  }

  private static String getInput(String fieldName, Scanner keyboard) {
    System.out.print(String.format("[%s]: ", fieldName));
    return keyboard.nextLine().trim();
  }

  private static boolean positiveResponse(Scanner keyboard) {
    String line = keyboard.nextLine();
    return line.length() > 0 && Character.toLowerCase(line.trim().charAt(0)) == 'y';
  }
  
  private static void printAsciiArt() {
    Path asciiArtPath = Paths.get("src/main/resources", "asciiArt.txt");
    try {
      List<String> lines = Files.readAllLines(asciiArtPath);
      for (String string : lines) {
        System.out.println(string);
      }
    } catch (IOException e1) { }
   
//    try(BufferedReader in = new BufferedReader(new InputStreamReader(
//        MusicModifierApplication.class.getClassLoader().getResourceAsStream("asciiArt.txt")))) {
//      String line = null;
//      while ((line = in.readLine()) != null) {
//        System.out.println(line);
//      }
//    } catch (IOException e1) { }
  }

}
