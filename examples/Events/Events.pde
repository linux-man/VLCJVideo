//Using the keyboard: SPACE to pause, ENTER to stop, Cursor keys to jump and change volume
//How to bind Events
import VLCJVideo.*;

import uk.co.caprica.vlcj.player.events.MediaPlayerEventType;

VLCJVideo video;

void setup() {
  size(640, 360);
  video = new VLCJVideo(this);
  video.openMedia("https://www.sample-videos.com/video123/mp4/360/big_buck_bunny_360p_30mb.mp4");
  video.play();
  bindVideoEvents();
}

void bindVideoEvents() {
  video.bind( MediaPlayerEventType.FINISHED, new Runnable() { public void run() {
    println( "finished" );
  } } );
  video.bind( MediaPlayerEventType.OPENING, new Runnable() { public void run() {
    println( "opened" );
  } } );
  video.bind( MediaPlayerEventType.ERROR, new Runnable() { public void run() {
    println( "error" );
  } } );
  video.bind( MediaPlayerEventType.PAUSED, new Runnable() { public void run() {
    println( "paused" );
  } } );
  video.bind( MediaPlayerEventType.STOPPED, new Runnable() { public void run() {
    println( "stopped" );
  } } );
  video.bind( MediaPlayerEventType.PLAYING, new Runnable() { public void run() {
    println( "playing" );
  } } );
  video.bind( MediaPlayerEventType.MEDIA_STATE_CHANGED, new Runnable() { public void run() {
    println( "state changed" );
  } } );
}

void draw() {
  background(0);
  image(video, 0, 0);
  text("Volume: " + video.volume(), 20, 20);
  text("Time: " + video.time(), 20, 40);
}

void keyPressed() {
  if(key == ' ') {
    if(video.isPlaying()) video.pause();
    else video.play();
  }
  if(keyCode == ENTER) {
    if(video.isPlaying()) video.stop();
    else video.play();
  }
  if(keyCode == UP) {
    video.setVolume(video.volume() + 0.1);
  }
  if(keyCode == DOWN) {
    video.setVolume(video.volume() - 0.1);
  }
  if(keyCode == LEFT) {
    video.jump(video.time() - 10);
  }
  if(keyCode == RIGHT) {
    video.jump(video.time() + 10);
  }
}
