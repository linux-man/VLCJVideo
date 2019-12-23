/*
Volume and Position
Press ENTER, SPACE and CURSOR KEYS
*/

import VLCJVideo.*;

VLCJVideo video;

void setup() {
  size(640, 360);
  textSize(20);
  video = new VLCJVideo(this);
  video.open("https://www.sample-videos.com/video123/mp4/360/big_buck_bunny_360p_30mb.mp4");
  video.setRepeat(true);
  video.play();
  video.setVolume(100); //setVolume only works AFTER play()
}

void draw() {
  background(0);
  image(video, 0, 0);
  text("Press ENTER, SPACE and CURSOR KEYS", 5, 20);
  println("Volume:" + video.volume() + " Time:" + video.time());
}

void keyPressed() {
  if(key == ' ') {
    video.pause();
  }
  if(keyCode == ENTER) {
    if(video.isPlaying()) video.stop();
    else video.play();
  }
  if(keyCode == UP) {
    video.setVolume(video.volume() + 10);
  }
  if(keyCode == DOWN) {
    video.setVolume(video.volume() - 10);
  }
  if(keyCode == LEFT) {
    video.setTime(video.time() - 5000);
  }
  if(keyCode == RIGHT) {
    video.setTime(video.time() + 5000);
  }
  if(key == 'm') {
    video.setMute(!video.isMute());
  }
}
