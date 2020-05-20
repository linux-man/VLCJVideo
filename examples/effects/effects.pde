/*
VLC Factory Effects
Press 1, 2, 3 or 4
*/

import VLCJVideo.*;
String path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

VLCJVideo video;

void setup() {
  size(640, 360);
  textSize(20);
  video = new VLCJVideo(this);
  video.openAndPlay(path);
}

void draw() {
  background(0);
  image(video, 0, 0, width, height);
  text("Press 1, 2, 3 or 4", 5, 20);
}

void keyPressed() {
  if(key == '1') {
    video.dispose();
    video = new VLCJVideo(this);
    video.openAndPlay(path);
  }
  
  if(key == '2') {
    video.dispose();
    String[] options = {"--video-filter", "sepia"};
    video = new VLCJVideo(this, options);
    video.openAndPlay(path);
  }

  if(key == '3') {
    video.dispose();
    String[] options = {"--video-filter", "wave"};
    video = new VLCJVideo(this, options);
    video.openAndPlay(path);
  }

  if(key == '4') {
    video.dispose();
    String[] options = {"--video-filter", "rotate{angle=180}"};
    video = new VLCJVideo(this, options);
    video.openAndPlay(path);
  }
}
