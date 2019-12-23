/*
 * VLCJVideo
 * VLCJ binding for Processing.
 * http://github.com/linux-man/VLCJVideo
 *
 * Copyright (C) 2019 Caldas Lopes http://softlab.pt
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author      Caldas Lopes http://softlab.pt
 * @modified    23/12/2019
 * @version     0.4.0
 */

package VLCJVideo;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.State;
import uk.co.caprica.vlcj.media.MediaRef;
//import uk.co.caprica.vlcj.media.TrackType;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VLCJVideo extends PImage implements PConstants {

	public static enum MediaPlayerEventType {
		  ALL,                           //All Events
		  //AUDIO_DEVICE_CHANGED,          //The audio device changed
		  BACKWARD,                      //Media skipped backward
		  BUFFERING,                     //Buffering media
		  //CHAPTER_CHANGED,               //The chapter changed
		  //CORKED,                        //The media player was corked/un-corked
		  //ELEMENTARY_STREAM_ADDED,       //An elementary stream was added
		  //ELEMENTARY_STREAM_DELETED,     //An elementary stream was deleted
		  //ELEMENTARY_STREAM_SELECTED,    //An elementary stream was selected
		  ERROR,                         //An error occurred
		  FINISHED,                      //Media finished playing (i.e. the end was reached without being stopped)
		  FORWARD,                       //Media skipped forward
		  LENGTH_CHANGED,                //Media length changed
		  MEDIA_CHANGED,                 //The media changed
		  MEDIA_PLAYER_READY,            //Media player is ready after the media has started playing
		  MUTED,                         //The audio was muted/un-muted
		  OPENING,                       //Opening the media
		  PAUSABLE_CHANGED,              //Media pausable status changed
		  PAUSED,                        //Media paused
		  PLAYING,                       //The media started playing
		  POSITION_CHANGED,              //Media play-back position changed
		  //SCRAMBLED_CHANGED,             //Program scrambled changed
		  SEEKABLE_CHANGED,              //Media seekable status changed
		  //SNAPSHOT_TAKEN,                //A snapshot was taken
		  STOPPED,                       //Media stopped
		  TIME_CHANGED,                  //Media play-back time changed
		  //TITLE_CHANGED,                 //Media title changed
		  //VIDEO_OUTPUT,                  //The number of video outputs changed
		  VOLUME_CHANGED                 //The volume changed
		}

  public final static String VERSION = "0.4.0";

  protected PApplet parent = null;

  protected String filename;
  protected boolean firstFrame;

  protected MediaPlayerFactory factory;
  protected EmbeddedMediaPlayer mediaPlayer;

  protected final List<State> readyStates = Arrays.asList(new State[] {State.ENDED, State.PAUSED, State.PLAYING, State.STOPPED});
  protected final HashMap<MediaPlayerEventType, ArrayList<Runnable>> handlers = new HashMap<MediaPlayerEventType, ArrayList<Runnable>>();
  protected final ExecutorService mainExec = Executors.newSingleThreadExecutor();
  protected final ExecutorService volExec = Executors.newSingleThreadExecutor();
  protected int[] rgbBuffer;

//------------------------- PRIVATE METHODS & CLASSES ------------------------//

  private final class TestRenderCallback implements RenderCallback {

    @Override
    public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
      ByteBuffer bb = nativeBuffers[0];
      IntBuffer ib = bb.asIntBuffer();
      ib.get(rgbBuffer);
      pixels = rgbBuffer;
      updatePixels();
    }

  }

  private final class TestBufferFormatCallback implements BufferFormatCallback {

    @Override
    public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
      if(firstFrame) {
        firstFrame = false;
        init(sourceWidth, sourceHeight, parent.ARGB);
        rgbBuffer = new int[sourceWidth * sourceHeight];
      }
      return new RV32BufferFormat(sourceWidth, sourceHeight);
    }

  }

  private void handleEvent(MediaPlayerEventType type) {
    if(handlers.containsKey(type)) {
      ArrayList<Runnable> eventHandlers = handlers.get(type);
      Iterator<Runnable> it = eventHandlers.iterator();
      while(it.hasNext()) {
        it.next().run();
      }
    }
  }

  private void bindMediaPlayerEvents() {
    mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

      @Override
      public void backward(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.BACKWARD);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void buffering(MediaPlayer mediaPlayer, float newCache) {
        handleEvent(MediaPlayerEventType.BUFFERING);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void error(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.ERROR);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void finished(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.FINISHED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void forward(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.FORWARD);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
        handleEvent(MediaPlayerEventType.LENGTH_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {
        handleEvent(MediaPlayerEventType.MEDIA_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void mediaPlayerReady(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.MEDIA_PLAYER_READY);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void muted(MediaPlayer mediaPlayer, boolean muted) {
        handleEvent(MediaPlayerEventType.MUTED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void opening(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.OPENING);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {
        handleEvent(MediaPlayerEventType.PAUSABLE_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void paused(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.PAUSED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void playing(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.PLAYING);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
        handleEvent(MediaPlayerEventType.POSITION_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {
        handleEvent(MediaPlayerEventType.SEEKABLE_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void stopped(MediaPlayer mediaPlayer) {
        handleEvent(MediaPlayerEventType.STOPPED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        handleEvent(MediaPlayerEventType.TIME_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
        handleEvent(MediaPlayerEventType.VOLUME_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }
// UNBINDED EVENTS
/*
      @Override
      public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {
        handleEvent(MediaPlayerEventType.AUDIO_DEVICE_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {
        handleEvent(MediaPlayerEventType.CHAPTER_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void corked(MediaPlayer mediaPlayer, boolean corked) {
        handleEvent(MediaPlayerEventType.CORKED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType type, int id) {
        handleEvent(MediaPlayerEventType.ELEMENTARY_STREAM_ADDED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType type, int id) {
        handleEvent(MediaPlayerEventType.ELEMENTARY_STREAM_DELETED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType type, int id) {
        handleEvent(MediaPlayerEventType.ELEMENTARY_STREAM_SELECTED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {
        handleEvent(MediaPlayerEventType.SCRAMBLED_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
        handleEvent(MediaPlayerEventType.SNAPSHOT_TAKEN);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {
        handleEvent(MediaPlayerEventType.TITLE_CHANGED);
        handleEvent(MediaPlayerEventType.ALL);
      }

      @Override
      public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
        handleEvent(MediaPlayerEventType.VIDEO_OUTPUT);
        handleEvent(MediaPlayerEventType.ALL);
      }
*/
    });
  }

//-------------------------------- CONSTRUCTOR -------------------------------//

  public VLCJVideo(PApplet parent, String... options) {
    super(0, 0, PApplet.RGB);
    this.parent = parent;
    factory = new MediaPlayerFactory(options);
    mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
    mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(new TestBufferFormatCallback(), new TestRenderCallback(), true));
    bindMediaPlayerEvents();
  }

//------------------------------ PUBLIC METHODS ------------------------------//

  public void bind(MediaPlayerEventType type, Runnable handler) {
    ArrayList<Runnable> eventHandlers;
    if(!handlers.containsKey(type)) {
      eventHandlers = new ArrayList<Runnable>();
      handlers.put(type, eventHandlers);
    }
    else {
      eventHandlers = handlers.get(type);
    }
    eventHandlers.add(handler);
  }

  public void dispose() {
    mainExec.shutdownNow();
    volExec.shutdownNow();
    mediaPlayer.release();
    factory.release();
  }

  public void open(String mrl) {
    try {
      filename = parent.dataPath(mrl);
      File f = new File(filename);
      if(!f.exists()) filename = mrl;
    }
    finally {
      init(0, 0, PApplet.RGB);
      firstFrame = true;
      if(mediaPlayer.media().start(mrl)) mediaPlayer.controls().stop();
    }
  }

  public void openAndPlay(String mrl) {
    open(mrl);
    play();
  }
/*
  public void start() {
    if(isReady()) mediaPlayer.controls().start();
  }
*/
  public void play() {
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.controls().play();
      }
    });
  }

  public void pause() {
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.controls().pause();
      }
    });
  }

  public void stop() {
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.controls().stop();
        loadPixels();
        for (int i = 0; i < width * height; i++) pixels[i] = 0;
        updatePixels();
      }
    });
  }

  public void setPause(final boolean pause) {
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.controls().setPause(pause);
      }
    });
  }

  public void setTime(final long time) {
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.controls().setTime(time);
      }
    });
  }

  public void setPosition(float pos) {
    final float position = Math.max(0, Math.min(1, pos));
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.controls().setPosition(position);
      }
    });    
  }

  public void setRepeat(final boolean repeat) {
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.controls().setRepeat(repeat);
      }
    });    
  }

  public void setVolume(int vol) {
    final int volume = Math.max(0, Math.min(200, vol));
    volExec.execute(new Runnable() {
      public void run() {
        while(volume() != volume) {
          mediaPlayer.audio().setVolume(Math.max(0, Math.min(200, volume)));
        }
      }
    });
  }

  public void setMute(final boolean mute) {
    mainExec.execute(new Runnable() {
      public void run() {
        while(!isReady());
        mediaPlayer.audio().setMute(mute);
      }
    });    
  }

  public State state() {
    return mediaPlayer.status().state();
    //return mediaPlayer.media().info().state(); //Another way
  }

  public long time() {
    return mediaPlayer != null ? mediaPlayer.status().time() : 0;
  }

  public float position() {
    return mediaPlayer != null ? mediaPlayer.status().position() : 0.0f;
  }

  public long length() {
    return mediaPlayer != null ? Math.max(mediaPlayer.status().length(), mediaPlayer.media().info().duration()) : 0;
  }

  public long duration() {
    return length();
  }

  public int volume() {
    return mediaPlayer != null ? mediaPlayer.audio().volume() : -1;
  }

  public boolean isReady() {
    return mediaPlayer != null && readyStates.contains(mediaPlayer.status().state());
  }

  public boolean isPlaying() {
    return mediaPlayer != null && mediaPlayer.status().isPlaying();
  }

  public boolean isPaused() {
    return mediaPlayer != null && mediaPlayer.status().state() == State.PAUSED;
  }

  public boolean isStopped() {
    return mediaPlayer != null && mediaPlayer.status().state() == State.STOPPED;
  }

  public boolean isPlayable() {
    return mediaPlayer != null && mediaPlayer.status().isPlayable();
  }

  public boolean isSeekable() {
    return mediaPlayer != null && mediaPlayer.status().isSeekable();
  }

  public boolean canPause() {
    return mediaPlayer != null && mediaPlayer.status().canPause();
  }

  public boolean getRepeat() {
    return mediaPlayer != null && mediaPlayer.controls().getRepeat();
  }

  public boolean isMute() {
    return mediaPlayer != null && mediaPlayer.audio().isMute();
  }

}
