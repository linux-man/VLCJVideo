# VLCJVideo
## VLCJ binding for Processing.

Simple wrapper with unified processing video interface.

## Dependencies

[vlcj-4](http://capricasoftware.co.uk/projects/vlcj)

[VLC Media Player](https://www.videolan.org/) 3.x or later.

## Example

```
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
```
## Methods

Constructor:

### VLCJVideo(this, options[])
options is a string array (optional).

```
String[] options = {"--video-filter", "sepia:wave"};
video = new VLCJVideo(this, options);
```

### void open(String mrl)

### void openAndPlay(String mrl)

### void play()

### void stop()

### void pause()

### void setPause(boolean pause)

### void setTime(long time)
time in miliseconds

### void setPosition(float position)
between 0.0 and 1.0 as percentage

### void setRepeat(boolean repeat)

### void setVolume(int volume)
0-200 as percentage

### void setMute(boolean mute)

### State state()
See "states" example

### long time()
In miliseconds

### float position()

### long duration()
In miliseconds

### long length()
same as duration()

### int volume()

### boolean isReady()

### boolean isPlaying()

### boolean isPaused()

### boolean isStopped()

### boolean isPlayable()

### boolean isSeekable()

### boolean canPause()

### boolean getRepeat()

### boolean isMute()

### void bind(MediaPlayerEventType type, Runnable handler)
See "bind" example

### void dispose()

## Equalizer Methods

### void setEqualizer()

### void setEqualizer(String presetName)

### void setEqualizer(int presetIndex)

### void noEqualizer()

### void setPreamp(float newPreamp)

### void setAmp(int index, float newAmp)

### void setAmps(float[] newAmps)

### boolean hasEqualizer()

### String[] presets()

### boolean isPreset(String name)

### int presetIndex()

### String preset()

### float preamp()

### float amp(int index)

### float[] amps()

