//A basic example

import VLCJVideo.*;

VLCJVideo video;

void setup() {
  size(640, 360);
  video = new VLCJVideo(this);
  video.openAndPlay("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
}

void draw() {
  background(0);
  image(video, 0, 0, width, height);
}
