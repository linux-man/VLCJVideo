/*
VLC Equalizer
*/

import VLCJVideo.*;
import java.util.Arrays;

VLCJVideo video;
int preset = 0;

void setup() {
  size(640, 360);
  textSize(14);
  video = new VLCJVideo(this);
  video.setEqualizer(preset);
  video.openAndPlay("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
  print("Equalizer Presets: ");
  println(video.presets());
  println("Active Equalizer Preset: " + video.preset());
  println("PreAmp: " + video.preamp());
  println(Arrays.toString(video.amps()));
}

void draw() {
  background(0);
  image(video, 0, 0, width, height);
  text("Press CURSOR KEYS, 1 to 0 ,Q to P, J and M (Bands), A and Z (PreAmp), S and D (Volume)", 5, 20);
}

void keyPressed() {
  if(keyCode == 'S') {
    video.setVolume(video.volume() + 10);
    println("Volume: " + video.volume());
  }
  if(keyCode == 'X') {
    video.setVolume(video.volume() - 10);
    println("Volume: " + video.volume());
  }
  if(keyCode == 'A') {
    video.setPreamp(video.preamp() + 1);
    println("PreAmp: " + video.preamp());
  }
  if(keyCode == 'Z') {
    video.setPreamp(video.preamp() - 1);
    println("PreAmp: " + video.preamp());
  }

  if(keyCode == '1') {
    video.setAmp(0, video.amp(0) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'Q') {
    video.setAmp(0, video.amp(0) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '2') {
    video.setAmp(1, video.amp(1) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'W') {
    video.setAmp(1, video.amp(1) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '3') {
    video.setAmp(2, video.amp(2) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'E') {
    video.setAmp(2, video.amp(2) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '4') {
    video.setAmp(3, video.amp(3) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'R') {
    video.setAmp(3, video.amp(3) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '5') {
    video.setAmp(4, video.amp(4) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'T') {
    video.setAmp(4, video.amp(4) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '6') {
    video.setAmp(5, video.amp(5) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'Y') {
    video.setAmp(5, video.amp(5) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '7') {
    video.setAmp(6, video.amp(6) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'U') {
    video.setAmp(6, video.amp(6) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '8') {
    video.setAmp(7, video.amp(7) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'I') {
    video.setAmp(7, video.amp(7) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '9') {
    video.setAmp(8, video.amp(8) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'O') {
    video.setAmp(8, video.amp(8) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == '0') {
    video.setAmp(9, video.amp(9) + 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'P') {
    video.setAmp(9, video.amp(9) - 1);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'J') {
    float[] amps = video.amps();
    for(int n = 0; n < amps.length; n++) amps[n]++;
    video.setAmps(amps);
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == 'M') {
    float[] amps = video.amps();
    for(int n = 0; n < amps.length; n++) amps[n]--;
    video.setAmps(amps);
    println(Arrays.toString(video.amps()));
  }

  if(keyCode == DOWN) {
    video.noEqualizer();
    println("No Active Equalizer");
  }

  if(keyCode == UP) {
    video.setEqualizer();
    println("Active Equalizer Preset: " + video.preset());
    println("PreAmp: " + video.preamp());
    println(Arrays.toString(video.amps()));
  }

  if(keyCode == RIGHT) {
    preset++;
    if(preset >= video.presets().length) preset = 0;
    video.setEqualizer(preset);
    println("Active Equalizer Preset: " + video.preset());
    println("PreAmp: " + video.preamp());
    println(Arrays.toString(video.amps()));
  }
  if(keyCode == LEFT) {
    preset--;
    if(preset < 0) preset = video.presets().length - 1;
    video.setEqualizer(preset);
    println("Active Equalizer Preset: " + video.preset());
    println("PreAmp: " + video.preamp());
    println(Arrays.toString(video.amps()));
 }
}
