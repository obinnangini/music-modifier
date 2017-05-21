package com.ngini.music.service;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import com.mpatric.mp3agic.Mp3File;
import com.ngini.music.model.MusicFields;

public class MusicModifierServiceTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testHandleInput() throws IOException {
    String tmpDirPath = System.getProperty("java.io.tmpdir");
    ByteArrayInputStream in = new ByteArrayInputStream((tmpDirPath +"\n  Yeow\nyeow\nf\n\n\n\n\n\n\n\n").getBytes());
    
    MusicModifierService service = createMockBuilder(MusicModifierService.class)
        .addMockedMethod("getPopulator")
        .addMockedMethod("getSaver")
        .addMockedMethod("handleUnwantedFields")
        .addMockedMethod("handleUpdateSelectFields")
        .addMockedMethod("positiveResponse")
        .createMock();
    
    MusicFilePopulater populator = mock(MusicFilePopulater.class);
    MusicFileSaver saver = mock(MusicFileSaver.class);
    
    expect(populator.getMusicFiles(isA(File.class)))
    .andReturn(new ArrayList<Mp3File>());
    
    expect(service.getPopulator()).andReturn(populator);
    expect(service.getSaver()).andReturn(saver);
    expect(service.handleUnwantedFields(isA(Scanner.class), (List<Mp3File>) isA(List.class))).andReturn(true);
    expect(service.handleUpdateSelectFields(isA(Scanner.class), (List<Mp3File>) isA(List.class))).andReturn(true);

    expect(service.positiveResponse(isA(Scanner.class))).andReturn(false);

    
    replay(populator, saver, service);
    
    service.handleInput(in);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHandleUpdateSelectFields() {
    ByteArrayInputStream in = new ByteArrayInputStream("  Yeow\nyeow\nf\n\n\n\n\n\n\n\n".getBytes());
    
    MusicModifierService service = createMockBuilder(MusicModifierService.class)
        .addMockedMethod("getUpdater")
        .createMock();
    
    MusicFieldUpdater updater = mock(MusicFieldUpdater.class);
    updater.setFields((List<Mp3File>) isA(List.class), isA(MusicFields.class));
    expectLastCall();
    
    expect(service.getUpdater()).andReturn(updater);
    
    replay(updater, service);
    
    assertTrue(service.
        handleUpdateSelectFields(new Scanner(in), new ArrayList<Mp3File>()));
  }

  @Test
  public void testGetMusicFields() {
    ByteArrayInputStream in = new ByteArrayInputStream("  Bad\nYeow\nyeow\nf\n\n\n\n\n\n\n\n".getBytes());
    Scanner scanner = new Scanner(in);
    
    MusicFields fields = new MusicModifierService().getMusicFields(scanner);
    assertEquals("Bad", fields.getAlbumArtist());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testHandleUnwantedFieldsTrue() {
    MusicModifierService service = createMockBuilder(MusicModifierService.class)
        .addMockedMethod("positiveResponse")
        .addMockedMethod("getClearer")
        .createMock();
    
    MusicFieldClearer clearer = mock(MusicFieldClearer.class);
    expect(clearer.unwantedFieldsArePopulated((List<Mp3File>) isA(List.class))).andReturn(true);
    clearer.clearOutUnwantedFields(((List<Mp3File>) isA(List.class)));
    expectLastCall();
    
    expect(service.positiveResponse(isA(Scanner.class))).andReturn(true);
    expect(service.getClearer()).andReturn(clearer).times(2);
    replay(clearer, service);
    
    assertTrue(service.
        handleUnwantedFields(new Scanner(System.in), new ArrayList<Mp3File>()));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHandleUnwantedFieldsFalse() {
    MusicModifierService service = createMockBuilder(MusicModifierService.class)
        .addMockedMethod("positiveResponse")
        .addMockedMethod("getClearer")
        .createMock();
    
    MusicFieldClearer clearer = mock(MusicFieldClearer.class);
    expect(clearer.unwantedFieldsArePopulated((List<Mp3File>) isA(List.class))).andReturn(false);
    
    expect(service.positiveResponse(isA(Scanner.class))).andReturn(true);
    expect(service.getClearer()).andReturn(clearer);
    replay(clearer, service);
    
    assertFalse(service.
        handleUnwantedFields(new Scanner(System.in), new ArrayList<Mp3File>()));
  }

  @Test
  public void testGetInput() {
    ByteArrayInputStream in = new ByteArrayInputStream("  Bad\nYeow\nyeow\nf".getBytes());
    Scanner scanner = new Scanner(in);
    
    assertEquals("Bad", new MusicModifierService().getInput("Field",scanner));
  }
  

  @Test
  public void testPositiveResponse() {
    ByteArrayInputStream in = new ByteArrayInputStream("Bad\nYeow\nyeow\nf".getBytes());
    Scanner scanner = new Scanner(in);
    
    MusicModifierService service = new MusicModifierService();
    service.getPopulator();
    service.getClearer();
    service.getUpdater();
    service.getSaver();
    
    assertFalse(new MusicModifierService().positiveResponse(scanner));
    assertTrue(new MusicModifierService().positiveResponse(scanner));
    assertTrue(new MusicModifierService().positiveResponse(scanner));
    assertFalse(new MusicModifierService().positiveResponse(scanner));
  }

}
