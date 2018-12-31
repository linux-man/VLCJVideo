# VLCJVideo
## VLCJ binding for Processing.

Simple wrapper with unified processing video interface.

Forked from the [VLCJVideo library](https://github.com/icanhazbroccoli/VLCJVideo) by Oleg Sidorov.

## Example

```
import VLCJVideo.*;

VLCJVideo video;

void setup() {
  size(640, 360);
  video = new VLCJVideo(this);
  video.openMedia("https://www.sample-videos.com/video123/mp4/360/big_buck_bunny_360p_30mb.mp4");
  video.loop();
  video.play();
}

void draw() {
  background(0);
  image(video, 0, 0);
}
```
## Methods

Constructor:

### VLCJVideo(this, options[])
options is a string array (optional). By default VLCJVideo is initialized with "-V dummy" and "--no-video-title-show"

### void openMedia(String mrl)

### void play()

### void stop()

### void pause()

### void jump(float pos)
pos in seconds

### void loop()

### void noLoop()

### void mute()

### void setVolume(float volume)
volume between 0.0 and 1.0

### float time()

### float duration()

### float volume()

### boolean isReady()

### boolean isPlaying()

### boolean isPlayable()

### boolean isSeekable()

### boolean canPause()

### void bind(MediaPlayerEventType type, Runnable handler)
See "Events" example

### void setVLCLibPath(String path)

