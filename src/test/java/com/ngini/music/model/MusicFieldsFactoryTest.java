package com.ngini.music.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class MusicFieldsFactoryTest {

  @Test
  public void testGetMusicFields() {
    String albumArtist = "Artist";
    String album = "Album";
    String contributingArtist = "Contribute";
    String year = "1956";
    String genreDescription = "A Genre";
    String albumArtFilePath = "Wrong";
    String removeFromTitle = "Take It Out!";
    MusicFields fields = MusicFieldsFactory.getMusicFields(
        albumArtist, album, contributingArtist,
        year, genreDescription, albumArtFilePath, removeFromTitle);
    assertEquals(albumArtist, fields.getAlbumArtist());
    assertEquals(album, fields.getAlbum());
    assertEquals(contributingArtist, fields.getContributingArtist());
    assertEquals(year, fields.getYear());
    assertEquals("", fields.getGenreDescription());
    assertEquals(null, fields.getAlbumArt());
    assertEquals(removeFromTitle, fields.getRemoveFromTitle());
  }
  
  @Test
  public void testGetAlbumFilePathSuccess() throws IOException {
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    File dummyMusicFile = new File(tmpDir, "Dummy.jpg");
    try (FileWriter out = new FileWriter(dummyMusicFile.getPath())) {
      out.append("Dummy data");
      assertNotNull(MusicFieldsFactory.getAlbumFilePath(dummyMusicFile.getPath())); 
    }
    try { 
      assertTrue(dummyMusicFile.delete()); 
    } finally { }
  }
  
  @Test
  public void testGetAlbumFilePathFailureWrongFileType() throws IOException {
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    File dummyMusicFile = new File(tmpDir, "Dummy.mp3");
    try (FileWriter out = new FileWriter(dummyMusicFile.getPath())) {
      out.append("Dummy data");
      assertNull(MusicFieldsFactory.getAlbumFilePath(dummyMusicFile.getPath())); 
    }
    try { 
      assertTrue(dummyMusicFile.delete()); 
    } finally { }
  }
  
  @Test
  public void testGetAlbumFilePathFailureNoFile() throws IOException {
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    File dummyMusicFile = new File(tmpDir, "Dummy.mp3");
    assertNull(MusicFieldsFactory.getAlbumFilePath(dummyMusicFile.getPath())); 
  }
  
  @Test
  public void testGetAlbumFilePathFailureEmptyString() {
    assertEquals(null, MusicFieldsFactory.getAlbumFilePath(""));
  }
  
  @Test
  public void testGetGenreDesc() {
    assertEquals("Dance", MusicFieldsFactory.getGenreDesc("Dance"));
    assertEquals("", MusicFieldsFactory.getGenreDesc("Wrong Genre"));
    assertEquals("", MusicFieldsFactory.getGenreDesc(""));
  }

  @Test
  public void testGetYearSuccess() {
    assertEquals("2008", MusicFieldsFactory.getYear("2008"));
    assertEquals("", MusicFieldsFactory.getYear("Dummy"));
  }

  @Test
  public void testGetYearFailureEmptyString() {
    assertEquals("", MusicFieldsFactory.getYear(""));
  }


}
