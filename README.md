# Music Modifier

This is a Java Utility that can be used to updated MP3's in a specified folder
## What does it do?
The tool can be used to:
1. Remove a common unwanted string from all song file names
1. Remove a common unwanted string from all song titles
1. Update fields: 
    1. Album Artist
    1. Album
    1. Contributing artist
    1. Genre
    1. Year
1. Clear out fields:
    1. AuthorURL
    1. Encoder By
    1. Comments
    1. Composers 
    1. Copyright
    1. Copyright URL
    1. Publisher
    1. Publisher URL
    1. Payment URL 

## How to Run
1. Download the code base.

* In the project root, run `mvn clean package` to generate an executable jar. 
Then run `java -jar .\target\music-modifier-<project.version>-jar-with-dependencies.jar`
For example: `java -jar .\target\music-modifier-0.0.1-SNAPSHOT-jar-with-dependencies.jar`
OR 
* Import into your favorite IDE and launch MusicModifierApplication.java

## How it works
ID3v1 and ID3v2 tags are updated using the [mp3agic](https://github.com/mpatric/mp3agic) library

## Extensions
1. Attach image provided via filePath to ID3 tags of all MP3s in a folder
