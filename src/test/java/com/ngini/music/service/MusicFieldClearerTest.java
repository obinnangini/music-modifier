package com.ngini.music.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v22Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MusicFieldClearerTest {

  @Test
  public void testClearOutUnwantedFieldsListOfMp3File() {
    Mp3File mp3File1 = mock(Mp3File.class);
    expect(mp3File1.hasId3v2Tag()).andReturn(false);
    expect(mp3File1.hasId3v1Tag()).andReturn(true);
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
    
    new MusicFieldClearer().clearOutUnwantedFields(mp3Files);
    
    verify(mp3File1, mp3File2);
  }

  @Test
  public void testClearOutUnwantedFieldsID3v2() {
    ID3v2 id3v2Tag = new ID3v22Tag();
    String fieldValue = "to be removed";

    id3v2Tag.setComment(fieldValue);
    id3v2Tag.setArtistUrl(fieldValue);
    id3v2Tag.setEncoder(fieldValue);
    id3v2Tag.setComposer(fieldValue);
    id3v2Tag.setCompilation(true);
    id3v2Tag.setItunesComment(fieldValue);
    id3v2Tag.setCopyright(fieldValue);
    id3v2Tag.setCopyrightUrl(fieldValue);
    id3v2Tag.setPaymentUrl(fieldValue);
    id3v2Tag.setPublisher(fieldValue);
    id3v2Tag.setPublisherUrl(fieldValue);

    new MusicFieldClearer().clearOutUnwantedFields(id3v2Tag);

    String blankValue = " ";
    assertEquals(blankValue, id3v2Tag.getComment());
    assertEquals(blankValue, id3v2Tag.getArtistUrl());
    assertEquals(blankValue, id3v2Tag.getEncoder());
    assertEquals(blankValue, id3v2Tag.getComposer());
    assertFalse(id3v2Tag.isCompilation());
    assertEquals(blankValue, id3v2Tag.getItunesComment());
    assertEquals(blankValue, id3v2Tag.getCopyright());
    assertEquals(blankValue, id3v2Tag.getCopyrightUrl());
    assertEquals(blankValue, id3v2Tag.getPaymentUrl());
    assertEquals(blankValue, id3v2Tag.getPublisher());
    assertEquals(blankValue, id3v2Tag.getPublisherUrl());
  }

  @Test
  public void testClearOutUnwantedFieldsID3v1() {
    ID3v1 id3v1Tag = new ID3v1Tag();
    id3v1Tag.setComment("to be removed");
    new MusicFieldClearer().clearOutUnwantedFields(id3v1Tag);
    assertEquals(" ", id3v1Tag.getComment());
  }

  @Test
  public void testUnwantedFieldsArePopulatedListOfMp3File() {
    
    Mp3File mp3File1 = mock(Mp3File.class);
    expect(mp3File1.hasId3v2Tag()).andReturn(false);
    expect(mp3File1.hasId3v1Tag()).andReturn(true);
    ID3v1 id3v1Tag = new ID3v1Tag();
    expect(mp3File1.getId3v1Tag()).andReturn(id3v1Tag);
    
    Mp3File mp3File2 = mock(Mp3File.class);
    expect(mp3File2.hasId3v2Tag()).andReturn(true);
    ID3v2 id3v2Tag = new ID3v22Tag();
    expect(mp3File2.getId3v2Tag()).andReturn(id3v2Tag);
    
    Mp3File mp3File3 = mock(Mp3File.class);
    expect(mp3File3.hasId3v1Tag()).andReturn(false);
    expect(mp3File3.hasId3v2Tag()).andReturn(false);
    expect(mp3File3.getFilename()).andReturn("File with no tag");
    
    List<Mp3File> mp3Files = new ArrayList<>();
    mp3Files.add(mp3File1);
    mp3Files.add(mp3File2);
    mp3Files.add(mp3File3);
    
    replay(mp3File1, mp3File2, mp3File3);
    
    assertFalse(new MusicFieldClearer().unwantedFieldsArePopulated(mp3Files));
    
    verify(mp3File1, mp3File2, mp3File3);
  }

  @Test
  public void testUnwantedFieldsArePopulatedTrue1() {
    ID3v2 id3v2Tag = new ID3v22Tag();
    String fieldValue = "to be removed";
    
    id3v2Tag.setComment(fieldValue);
    id3v2Tag.setArtistUrl(fieldValue);
    id3v2Tag.setEncoder(fieldValue);
    id3v2Tag.setComposer(fieldValue);
    id3v2Tag.setCompilation(true);
    id3v2Tag.setItunesComment(fieldValue);
    id3v2Tag.setCopyright(fieldValue);
    id3v2Tag.setCopyrightUrl(fieldValue);
    id3v2Tag.setPaymentUrl(fieldValue);
    id3v2Tag.setPublisher(fieldValue);
    id3v2Tag.setPublisherUrl(fieldValue);
    
    assertTrue(new MusicFieldClearer().unwantedFieldsArePopulated(id3v2Tag));
  }
  
  @Test
  public void testUnwantedFieldsArePopulatedTrue2() {
    ID3v2 id3v2Tag = new ID3v22Tag();
    String fieldValue = "   ";
    
    id3v2Tag.setComment(fieldValue);
    id3v2Tag.setArtistUrl(fieldValue);
    id3v2Tag.setEncoder(fieldValue);
    id3v2Tag.setComposer(fieldValue);
    id3v2Tag.setCompilation(true);
    id3v2Tag.setItunesComment(fieldValue);
    id3v2Tag.setCopyright(fieldValue);
    id3v2Tag.setCopyrightUrl(fieldValue);
    id3v2Tag.setPaymentUrl(fieldValue);
    id3v2Tag.setPublisher(fieldValue);
    id3v2Tag.setPublisherUrl(fieldValue);
    
    assertTrue(new MusicFieldClearer().unwantedFieldsArePopulated(id3v2Tag));
  }
  
  @Test
  public void testUnwantedFieldsArePopulatedFalse() {
    ID3v2 id3v2Tag = new ID3v22Tag();
    String fieldValue = "   ";
    
    id3v2Tag.setComment(fieldValue);
    id3v2Tag.setArtistUrl(fieldValue);
    id3v2Tag.setEncoder(fieldValue);
    id3v2Tag.setComposer(fieldValue);
    id3v2Tag.setCompilation(false);
    id3v2Tag.setItunesComment(fieldValue);
    id3v2Tag.setCopyright(fieldValue);
    id3v2Tag.setCopyrightUrl(fieldValue);
    id3v2Tag.setPaymentUrl(fieldValue);
    id3v2Tag.setPublisher(fieldValue);
    id3v2Tag.setPublisherUrl(fieldValue);
    
    assertFalse(new MusicFieldClearer().unwantedFieldsArePopulated(id3v2Tag));
  }

  @Test
  public void testCheckIfPopulatedStringString() {
    assertTrue(new MusicFieldClearer().checkIfPopulated("Dummy boolean field", "Field"));
    assertFalse(new MusicFieldClearer().checkIfPopulated("Dummy boolean field", null));
    assertFalse(new MusicFieldClearer().checkIfPopulated("Dummy boolean field", ""));
    assertFalse(new MusicFieldClearer().checkIfPopulated("Dummy boolean field", "     "));
  }

  @Test
  public void testCheckIfPopulatedStringBoolean() {
    assertTrue(new MusicFieldClearer().checkIfTrue("Dummy boolean field", true));
    assertFalse(new MusicFieldClearer().checkIfTrue("Dummy boolean field", false));
  }

}
