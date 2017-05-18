package com.ngini.music;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ngini.music.service.MusicModifierService;


public class MusicModifierApplication {
  private static final Logger log = LoggerFactory.getLogger(MusicModifierApplication.class);

  public static void main(String[] args) {
    printAsciiArt();
    log.info("Welcome to the Music Modifier!");
    new MusicModifierService().run();
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
