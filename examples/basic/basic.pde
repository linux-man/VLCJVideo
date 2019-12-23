//A basic example

import VLCJVideo.*;

VLCJVideo video;

void setup() {
  size(640, 360);
  video = new VLCJVideo(this);
  video.openAndPlay("https://www.sample-videos.com/video123/mp4/360/big_buck_bunny_360p_30mb.mp4");
}

void draw() {
  background(0);
  image(video, 0, 0);
}
