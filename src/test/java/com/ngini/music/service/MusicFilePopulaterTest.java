package com.ngini.music.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.Test;

public class MusicFilePopulaterTest {

  @Test
  public void testGetMusicFiles() {
    File dir = mock(File.class);
    expect(dir.getPath()).andReturn("dirPath");
    
    File musicFile1 = mock(File.class);
    expect(musicFile1.isFile()).andReturn(true);
    expect(musicFile1.getName()).andReturn("temp1.mp3");
    expect(musicFile1.getPath()).andReturn("temp1.mp3").times(2);
    expect(musicFile1.getAbsolutePath()).andReturn("temp1.mp3");
    
    File musicFile2 = EasyMock.mock(File.class);
    expect(musicFile2.isFile()).andReturn(true);
    expect(musicFile2.getPath()).andReturn("temp2.jpg").times(2);
    
    File [] files = {musicFile1, musicFile2};
    expect(dir.listFiles()).andReturn(files);
    
    replay(dir, musicFile1, musicFile2);
    
    try {
      new MusicFilePopulater().getMusicFiles(dir);
      fail();
    } catch (IOException e) { }
  }

}
