/*
States: BUFFERING, ENDED, ERROR, NOTHING_SPECIAL, OPENING, PAUSED, PLAYING, STOPPED
Press any key to play/pause
*/

import VLCJVideo.*;
import uk.co.caprica.vlcj.player.base.State;

VLCJVideo video;
State state, prevState;

void setup() {
  size(640, 360);
  video = new VLCJVideo(this);
  video.open("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
}

void draw() {
  background(0);
  image(video, 0, 0, width, height);
  state = video.state();
  if(state != prevState) println(state);
  prevState = state;
}

void keyPressed() {
  if(video.isStopped()) video.play();
  else video.pause();
}
