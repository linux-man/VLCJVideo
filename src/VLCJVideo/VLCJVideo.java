/*
 * VLCJVideo
 * VLCJ binding for Processing.
 * http://github.com/linux-man/VLCJVideo
 *
 * Copyright (C) 2012 Oleg Sidorov http://4pcbr.com
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
 * @author      Oleg Sidorov http://4pcbr.com
 * @contributor Caldas Lopes http://softlab.pt
 * @modified    03/01/2019
 * @version     0.3.1
 */

package VLCJVideo;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.TrackInfo;
import uk.co.caprica.vlcj.player.VideoTrackInfo;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.player.events.MediaPlayerEventType;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class VLCJVideo extends PImage implements PConstants {

	public final static String VERSION = "0.3.1";

	protected PApplet parent = null;

	public int width;
	public int height;

	protected String filename;
	protected boolean firstFrame;
	protected boolean ready = false;
	protected boolean repeat = false;
	protected float volume = 1.0f;

	protected MediaPlayerFactory factory;
	protected DirectMediaPlayer mediaPlayer;
	protected HeadlessMediaPlayer headlessMediaPlayer;

	protected static boolean inited = false;

	protected final HashMap<MediaPlayerEventType, ArrayList<Runnable>> handlers;
	protected final Stack<Runnable> tasks;

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

	public void handleEvent(MediaPlayerEventType type) {
		if(handlers.containsKey(type)) {
			ArrayList<Runnable> eventHandlers = handlers.get(type);
			Iterator<Runnable> it = eventHandlers.iterator();
			while(it.hasNext()) {
				it.next().run();
			}
		}
	}

	protected static void init() {
		if(inited) return;

		inited = true;
		new NativeDiscovery().discover();
	}

	public VLCJVideo(PApplet parent, String... options) {
		super(0, 0, PApplet.RGB);
		width = 0;
		height = 0;
		VLCJVideo.init();

		tasks = new Stack<Runnable>();
		handlers = new HashMap<MediaPlayerEventType, ArrayList<Runnable>>();

		initVLC(parent, options);
	}

	protected void initVLC(PApplet parent, String... options) {
		String[] fullOptions = new String[options.length + 2];
		fullOptions[0] = "-V dummy";
		fullOptions[1] = "--no-video-title-show";
		System.arraycopy(options, 0, fullOptions, 2, options.length);
		this.parent = parent;
		firstFrame = true;
		factory = new MediaPlayerFactory(fullOptions);
		headlessMediaPlayer = factory.newHeadlessMediaPlayer();
		bindHeadlessMediaPlayerEvents(headlessMediaPlayer);
	}

	protected void scheduleTask(Runnable task) {
		this.tasks.push(task);
		if(isReady()) runTasks();
	}

	protected void runTasks() {
		while(!tasks.empty()) tasks.pop().run();
	}

	protected void bindHeadlessMediaPlayerEvents(HeadlessMediaPlayer hmp) {

		hmp.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

			public void mediaChanged(MediaPlayer mp, libvlc_media_t media, String mrl) {
				setReady(false);
			}

			public void error(MediaPlayer mediaPlayer) {
				handleEvent(MediaPlayerEventType.ERROR);
			}

			public void videoOutput(MediaPlayer mp, int newCount) {
				List<TrackInfo> info = mp.getTrackInfo();
				Iterator<TrackInfo> it = info.iterator();

				boolean dim_parsed = false;

				while(it.hasNext()) {
					TrackInfo ti = it.next();
					if(ti instanceof VideoTrackInfo) {
						width = ((VideoTrackInfo) ti).width();
						height = ((VideoTrackInfo) ti).height();
						if(width == 0) width = parent.width;
						if(height == 0) height = parent.height;
						dim_parsed = true;
						break;
					}
				}
				if(!dim_parsed) {
					System.out.println(String.format("Unable to parse media data, %s could not be played", filename));
					handleEvent(MediaPlayerEventType.ERROR);
				}
				else {
					mp.stop();
					setReady(true);
					initNewMediaPlayer();
				}
			}

		});

	}

	protected void initNewMediaPlayer() {
		if(mediaPlayer != null) releaseMediaPlayer(mediaPlayer);
		mediaPlayer = factory.newDirectMediaPlayer(new TestBufferFormatCallback(), new TestRenderCallback());
		firstFrame = true;
		bindMediaPlayerEvents(mediaPlayer);
		mediaPlayer.prepareMedia(filename);
		mediaPlayer.setRepeat(repeat);
		setVolume(volume);
		runTasks();
	}

	protected void bindMediaPlayerEvents(MediaPlayer mp1) {

		mp1.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

			public void opening(MediaPlayer mp) {
				handleEvent(MediaPlayerEventType.OPENING);
			}

			public void error(MediaPlayer mediaPlayer) {
				handleEvent(MediaPlayerEventType.ERROR);
			}

			public void finished(MediaPlayer mediaPlayer) {
				handleEvent(MediaPlayerEventType.FINISHED);
			}

			public void paused(MediaPlayer mediaPlayer) {
				handleEvent(MediaPlayerEventType.PAUSED);
			}

			public void stopped(MediaPlayer mediaPlayer) {
				handleEvent(MediaPlayerEventType.STOPPED);
			}

			public void playing(MediaPlayer mediaPlayer) {
				handleEvent(MediaPlayerEventType.PLAYING);
			}

			public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
				handleEvent(MediaPlayerEventType.MEDIA_STATE_CHANGED);
			}

		});

	}

	public void openMedia(String mrl) {
		try {
			filename = parent.dataPath(mrl);
			File f = new File(filename);
			if(!f.exists()) filename = mrl;
		}
		finally {
			headlessMediaPlayer.prepareMedia(filename);
			headlessMediaPlayer.parseMedia();
			headlessMediaPlayer.start();
		}
	}

	public void play() {
		scheduleTask(new Runnable() {
			public void run() {
				if(isReady()) mediaPlayer.play();
			}
		});
	}

	public void stop() {
		scheduleTask(new Runnable() {
			public void run() {
				if(isReady()) mediaPlayer.stop();
			}
		});
	}

	public void pause() {
		scheduleTask(new Runnable() {
			public void run() {
				if(isReady()) mediaPlayer.pause();
			}
		});
	}

	public float time() {
		return isReady() ? (float) ((float) mediaPlayer.getTime() / 1000.0) : 0.0f;
	}

	public float duration() {
		return isReady() ? (float) ((float) mediaPlayer.getLength() / 1000.0) : 0.0f;
	}

	public float volume() {
		return isReady() ? (float) ((float) volume) : 0.0f;
	}

	public void jump(final float pos) {
		scheduleTask(new Runnable() {
			public void run() {
				if(isReady()) mediaPlayer.setTime(Math.round(pos * 1000));
			}
		});
	}

	public boolean isReady() {
		return mediaPlayer != null && ready;
	}

	protected void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isPlaying() {
		return isReady() && mediaPlayer.isPlaying();
	}

	public boolean isPlayable() {
		return isReady() && mediaPlayer.isPlayable();
	}

	public boolean isSeekable() {
		return isReady() && mediaPlayer.isSeekable();
	}

	public boolean canPause() {
		return isReady() && mediaPlayer.canPause();
	}

	public void loop() {
		repeat = true;
		if(isReady()) mediaPlayer.setRepeat(true);
	}

	public void noLoop() {
		repeat = false;
		if(isReady()) mediaPlayer.setRepeat(false);
	}

	public void mute() {
		setVolume(0.0f);
	}

	public void setVolume(float volume) {
		if(volume < 0.0) volume = (float) 0.0;
		else if(volume > 1.0) volume = (float) 1.0;
		this.volume = volume;
		if(isReady()) mediaPlayer.setVolume(Math.round((float) (200.0) * volume));
	}

	public void dispose() {
		if(isReady()) releaseMediaPlayer(mediaPlayer);
		if(isReady()) releaseMediaPlayer(headlessMediaPlayer);
		factory.release();
	}

	protected void releaseMediaPlayer(MediaPlayer mp) {
		if(mp.isPlaying()) mp.stop();
		mp.release();
	}

	protected void finalize() throws Throwable {
		try {
			dispose();
		}
		finally {
			super.finalize();
		}
	}

	private final class TestRenderCallback extends RenderCallbackAdapter {

		public TestRenderCallback() {
			super(new int[width * height]);
		}

		@Override
		public void onDisplay(DirectMediaPlayer mediaPlayer, int[] data) {
			if(firstFrame) {
				init(width, height, parent.ARGB);
				firstFrame = false;
			}
			pixels = data;
			updatePixels();
		}

	}

	private final class TestBufferFormatCallback implements BufferFormatCallback {

		@Override
		public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
			return new RV32BufferFormat(width, height);
		}

	}

}
