/*
Binding Events
Press any key to play/pause
*/

import VLCJVideo.*;

VLCJVideo video;

void setup() {
  size(640, 360);
  video = new VLCJVideo(this);
  bindVideoEvents();
  video.open("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
}

void draw() {
  background(0);
  image(video, 0, 0, width, height);
}

void keyPressed() {
  if(video.isStopped()) video.play();
  else video.pause();
}

void bindVideoEvents() {

  video.bind( VLCJVideo.MediaPlayerEventType.BACKWARD, new Runnable() { public void run() {
    println("BACKWARD");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.BUFFERING, new Runnable() { public void run() {
    println("BUFFERING");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.ERROR, new Runnable() { public void run() {
    println("ERROR");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.FINISHED, new Runnable() { public void run() {
    println("FINISHED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.FORWARD, new Runnable() { public void run() {
    println("FORWARD");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.LENGTH_CHANGED, new Runnable() { public void run() {
    println("LENGTH_CHANGED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.MEDIA_CHANGED, new Runnable() { public void run() {
    println("MEDIA_CHANGED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.MEDIA_PLAYER_READY, new Runnable() { public void run() {
    println("MEDIA_PLAYER_READY");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.MUTED, new Runnable() { public void run() {
    println("MUTED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.OPENING, new Runnable() { public void run() {
    println("OPENING");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.PAUSABLE_CHANGED, new Runnable() { public void run() {
    println("PAUSABLE_CHANGED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.PAUSED, new Runnable() { public void run() {
    println("PAUSED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.PLAYING, new Runnable() { public void run() {
    println("PLAYING");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.POSITION_CHANGED, new Runnable() { public void run() {
    println("POSITION_CHANGED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.SEEKABLE_CHANGED, new Runnable() { public void run() {
    println("SEEKABLE_CHANGED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.STOPPED, new Runnable() { public void run() {
    println("STOPPED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.TIME_CHANGED, new Runnable() { public void run() {
    println("TIME_CHANGED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.TIME_CHANGED, new Runnable() { public void run() {
    println("TIME_CHANGED");
  } } );

  video.bind( VLCJVideo.MediaPlayerEventType.VOLUME_CHANGED, new Runnable() { public void run() {
    println("VOLUME_CHANGED");
  } } );

//Any Event
  video.bind( VLCJVideo.MediaPlayerEventType.ALL, new Runnable() { public void run() {
    //println( "ALL" );
  } } );
}
