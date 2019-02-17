//How to use vlcj AudioMediaPlayerComponent to play audio

import VLCJVideo.*;
import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

AudioMediaPlayerComponent amp;
MediaPlayer audio;
MediaMeta mm;

void setup() {
  new NativeDiscovery().discover();
  amp = new AudioMediaPlayerComponent();
  audio = amp.getMediaPlayer();
  audio.prepareMedia("https://sample-videos.com/audio/mp3/wave.mp3");
  audio.parseMedia();
  mm = audio.getMediaMeta();
  println(mm.getTitle());
  audio.play();
}

void draw() {

}
