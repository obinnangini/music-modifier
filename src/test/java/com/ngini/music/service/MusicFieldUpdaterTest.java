package com.ngini.music.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Genres;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v22Tag;
import com.mpatric.mp3agic.Mp3File;
import com.ngini.music.model.MusicFields;
import com.ngini.music.model.MusicFieldsFactory;


public class MusicFieldUpdaterTest {

  @Test
  public void testSetFieldsListOfMp3FileMusicFields() {
    
    Mp3File mp3File1 = mock(Mp3File.class);
    expect(mp3File1.hasId3v1Tag()).andReturn(true);
    expect(mp3File1.hasId3v2Tag()).andReturn(false);
    ID3v1 id3v1Tag = new ID3v1Tag();
    expect(mp3File1.getId3v1Tag()).andReturn(id3v1Tag);
    
    Mp3File mp3File2 = mock(Mp3File.class);
    expect(mp3File2.hasId3v2Tag()).andReturn(true);
    ID3v2 id3v2Tag = new ID3v22Tag();
    expect(mp3File2.getId3v2Tag()).andReturn(id3v2Tag);
    
    List<Mp3File> mp3Files = new ArrayList<>();
    mp3Files.add(mp3File1);
    mp3Files.add(mp3File2);
    
    replay(mp3File1, mp3File2);
    MusicFields fields = new MusicFields();
    fields.setAlbum("");
    fields.setContributingArtist("");
    fields.setGenreDescription("");
    fields.setYear("");
    fields.setAlbumArtist("");
    fields.setAlbumArt(null);
    fields.setRemoveFromTitle("");
    
    new MusicFieldUpdater().setFields(mp3Files, fields);
    
    verify(mp3File1, mp3File2);
  }

  @Test
  public void testSetFieldsID3v1MusicFields() {
    ID3v1 id3v1Tag = new ID3v1Tag();

    String dummyTitle = "Dummy title";
    id3v1Tag.setTitle(dummyTitle);

    String albumArtist = "Artist";
    String album = "Album";
    String contributingArtist = "Contribute";
    String year = "1956";
    String genreDescription = "Hip-Hop";
    String albumArtFilePath = "Wrong";
    String removeFromTitle = "Take It Out!";
    MusicFields fields = MusicFieldsFactory.getMusicFields(
      albumArtist, album, contributingArtist,
      year, genreDescription, albumArtFilePath, removeFromTitle);

    new MusicFieldUpdater().setFields(id3v1Tag, fields);
    assertEquals(dummyTitle, id3v1Tag.getTitle());
    assertEquals(album, id3v1Tag.getAlbum());
    assertEquals(contributingArtist, id3v1Tag.getArtist());
    assertEquals(ID3v1Genres.matchGenreDescription(genreDescription), id3v1Tag.getGenre());
    assertEquals(year, id3v1Tag.getYear());
  }

  @Test
  public void testSetFieldsID3v2MusicFields() {
    ID3v2 id3v2Tag = new ID3v22Tag();
    
    String dummyTitle = "Dummy title";
    id3v2Tag.setTitle(dummyTitle);
    
    String albumArtist = "Artist";
    String album = "Album";
    String contributingArtist = "Contribute";
    String year = "1956";
    String genreDescription = "Hip-Hop";
    String albumArtFilePath = "Wrong";
    String removeFromTitle = "Take It Out!";
    MusicFields fields = MusicFieldsFactory.getMusicFields(
        albumArtist, album, contributingArtist,
        year, genreDescription, albumArtFilePath, removeFromTitle);
    
    new MusicFieldUpdater().setFields(id3v2Tag, fields);
    assertEquals(dummyTitle, id3v2Tag.getTitle());
    assertEquals(album, id3v2Tag.getAlbum());
    assertEquals(contributingArtist, id3v2Tag.getArtist());
    assertEquals(ID3v1Genres.matchGenreDescription(genreDescription), id3v2Tag.getGenre());
    assertEquals(year, id3v2Tag.getYear());
    assertEquals(albumArtist, id3v2Tag.getAlbumArtist());

  }

  @Test
  public void testSetAlbumImage() throws IOException {
    File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    File dummyMusicFile = new File(tmpDir, "Dummy.mp3");
    try (FileWriter out = new FileWriter(dummyMusicFile.getPath())) {
      out.append("Dummy data");
    }
    ID3v2 id3v2Tag = new ID3v22Tag();
    MusicFields fields = new MusicFields();
    fields.setAlbumArt(dummyMusicFile);
    new MusicFieldUpdater().setAlbumImage(id3v2Tag, fields); 
    assertTrue(id3v2Tag.getAlbumImage().length > 0);
    try { 
      assertTrue(dummyMusicFile.delete()); 
    } finally { }
  }

}
