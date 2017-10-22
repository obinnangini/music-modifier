package com.ngini.music.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {

  @Test
  public void testStripSubstringFromTextSuccess() {
    String title = "Title - Text To Remove";
    String substring = " - Text To Remove";
    String expected = "Title";
    assertEquals(expected, Utils.stripSubstringFromText(title, substring));
  }
  
  @Test
  public void testStripSubstringFromTextFailure() {
    String title = "- Text To Remove";
    assertEquals(title, Utils.stripSubstringFromText(title, title));
  }
  
  @Test
  public void testStripSubstringFromTextNullValue() {
    assertEquals("", Utils.stripSubstringFromText(null, "text"));
  }

}
