/*
VLC Factory Effects
Press 1, 2, 3 or 4
*/

import VLCJVideo.*;

VLCJVideo video;

void setup() {
  size(640, 360);
  textSize(20);
  video = new VLCJVideo(this);
  video.openAndPlay("https://www.sample-videos.com/video123/mp4/360/big_buck_bunny_360p_30mb.mp4");
}

void draw() {
  background(0);
  image(video, 0, 0);
  text("Press 1, 2, 3 or 4", 5, 20);
}

void keyPressed() {
  if(key == '1') {
    video.dispose();
    video = new VLCJVideo(this);
    video.openAndPlay("https://www.sample-videos.com/video123/mp4/240/big_buck_bunny_240p_5mb.mp4");
  }
  
  if(key == '2') {
    video.dispose();
    String[] options = {"--video-filter", "sepia"};
    video = new VLCJVideo(this, options);
    video.openAndPlay("https://www.sample-videos.com/video123/mp4/240/big_buck_bunny_240p_5mb.mp4");
  }

  if(key == '3') {
    video.dispose();
    String[] options = {"--video-filter", "wave"};
    video = new VLCJVideo(this, options);
    video.openAndPlay("https://www.sample-videos.com/video123/mp4/240/big_buck_bunny_240p_5mb.mp4");
  }

  if(key == '4') {
    video.dispose();
    String[] options = {"--video-filter", "rotate{angle=180}"};
    video = new VLCJVideo(this, options);
    video.openAndPlay("https://www.sample-videos.com/video123/mp4/240/big_buck_bunny_240p_5mb.mp4");
  }
}
