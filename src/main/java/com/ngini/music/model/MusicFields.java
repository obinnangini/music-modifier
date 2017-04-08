package com.ngini.music.model;

import java.io.File;

public class MusicFields {

    private String albumArtist;
    private String album;
    private String contributingArtist;
    private String year;
    private String genreDescription;
    private File albumArt;
    private String removeFromTitle;
    public String getAlbumArtist() {
      return albumArtist;
    }
    public void setAlbumArtist(String albumArtist) {
      this.albumArtist = albumArtist;
    }
    public String getAlbum() {
      return album;
    }
    public void setAlbum(String album) {
      this.album = album;
    }
    public String getContributingArtist() {
      return contributingArtist;
    }
    public void setContributingArtist(String contributingArtist) {
      this.contributingArtist = contributingArtist;
    }
    public String getYear() {
      return year;
    }
    public void setYear(String year) {
      this.year = year;
    }
    public String getGenreDescription() {
      return genreDescription;
    }
    public void setGenreDescription(String genreDescription) {
      this.genreDescription = genreDescription;
    }
    public File getAlbumArt() {
      return albumArt;
    }
    public void setAlbumArt(File albumArt) {
      this.albumArt = albumArt;
    }
    public String getRemoveFromTitle() {
      return removeFromTitle;
    }
    public void setRemoveFromTitle(String removeFromTitle) {
      this.removeFromTitle = removeFromTitle;
    }
    public boolean isEmpty() {
      return this.getAlbumArtist().isEmpty()
          && this.album.isEmpty()
          && this.contributingArtist.isEmpty()
          && this.year.isEmpty()
          && this.genreDescription.isEmpty()
          && this.albumArt == null
          && this.removeFromTitle.isEmpty();
    }
   
}