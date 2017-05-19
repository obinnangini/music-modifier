package com.ngini.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ngini.music.service.MusicModifierService;
import com.ngini.music.util.Utils;


public class MusicModifierApplication {
  private static final Logger log = LoggerFactory.getLogger(MusicModifierApplication.class);

  public static void main(String[] args) {
    Utils.printAsciiArt();
    log.info("Welcome to the Music Modifier!");
    new MusicModifierService().run();
  }

}
