package cz.dat.geometrytd.manager.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import cz.dat.geometrytd.Game;
import paulscode.sound.ListenerData;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {

	private SoundSystem system;
	private MusicProvider provider;
	private boolean isWorking = true;
	
	public static Map<String, String> sounds;
	public static Map<String, String> music;


	private String musicPlaying = null;
	private boolean isMusicPlaying = false;
	private boolean isMusicPaused = false;

	private static void loadSounds(SoundSystem system) {
		SoundManager.sounds = new HashMap<>();
		SoundManager.music = new HashMap<>();
		

		ListenerData d = system.getListenerData();
		for(Entry<String, String> sound : SoundManager.sounds.entrySet()) {
			system.newSource(false, sound.getKey(), sound.getValue(), false,
					d.position.x, d.position.y, d.position.z,
					SoundSystemConfig.ATTENUATION_NONE, 0);
		}

		SoundManager.sortSounds();
	}

	private static void sortSounds() {
	}

	public SoundManager() {
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", paulscode.sound.codecs.CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setSoundFilesPackage(Game.RES_DIR + "sounds/");
		} catch (SoundSystemException e) {
			Logger.getGlobal().warning("Sound system cannot be initialized!");
			this.isWorking = false;
			return;
		}
		
		this.system = new SoundSystem();
		SoundManager.loadSounds(this.system);
		this.provider = new MusicProvider(this);
	}
	
	public MusicProvider getMusicProvider() {
		return this.provider;
	}

	public boolean isMusicPlaying() {
		return this.isMusicPlaying;
	}
	
	public void playMusic(String name, boolean loop) {
		if(SoundManager.music.get(name) != null) {
			this.stopMusic();
			this.musicPlaying = name;
			this.isMusicPlaying = true;
			if(this.isWorking)
				this.system.backgroundMusic(name, SoundManager.music.get(name), loop);
		} else {
			Game.log.warning("Music called " + name + " does not exist");
		}
	}
	
	public void updatePlaying() {
		if(this.musicPlaying == null) {
			this.isMusicPlaying = false;
			return;
		}
		
		if(this.isWorking && !this.system.playing(musicPlaying) && !this.isMusicPaused) {
			this.isMusicPlaying = false;
			return;
		}
		
		this.isMusicPlaying = true;
	}

	public void pauseMusic() {
		if(this.isMusicPlaying && !this.isMusicPaused) {
			if(this.isWorking)
				this.system.pause(this.musicPlaying);
			this.isMusicPaused = true;
		}
	}

	public void playMusic() {
		if(this.musicPlaying != null && this.isMusicPaused) {
			if(this.isWorking)
				this.system.play(this.musicPlaying);
			this.isMusicPaused = false;
		}
	}

	public void stopMusic() {
		if(this.isMusicPlaying) {
			if(this.isWorking)
				this.system.stop(musicPlaying);
			this.musicPlaying = null;
			this.isMusicPlaying = false;
		}
	}

	public void updateVolume(boolean soundOn, float soundVolume) {
		if(this.isWorking) {
			if(soundOn) {
				this.system.setMasterVolume(soundVolume);
				this.playMusic();
			} else {
				this.system.setMasterVolume(0);
				this.pauseMusic();
			}
		}
	}

	public void shutdown() {
		this.stopMusic();
		if(this.isWorking) {
			this.system.cleanup();
		}
	}
	
	public void playSound(String name) {
		this.playSound(name, 1);
	}
	
	public void playSound(String name, float pitch) {
		this.playSound(name, pitch, this.system.getMasterVolume());
	}

	public void playSound(String name, float pitch, float volume) {
		ListenerData d = this.system.getListenerData();
		this.playSound(name, pitch, volume, d.position.x, d.position.y, d.position.z, false);
	}

	public void playSound(String name, float pitch, float volume, float x,
			float y, float z, boolean loop) {
		if(this.isWorking && SoundManager.sounds.get(name) != null) {
			this.system.setVolume(name, volume);
			this.system.setPitch(name, pitch);
			this.system.setPosition(name, x, y, z);
			this.system.setLooping(name, loop);
			
			if(this.system.playing(name))
				this.system.stop(name);
			
			this.system.play(name);
			
			ListenerData d = this.system.getListenerData();
			this.system.setPosition(name, d.position.x, d.position.y, d.position.z);
		}
	}

	public void playSound(String[] names) {
		this.playSound(names, 1);
	}
	
	public void playSound(String[] names, float pitch) {
		this.playSound(names, pitch, this.system.getMasterVolume());
	}

	public void playSound(String[] names, float pitch, float volume) {
		ListenerData d = this.system.getListenerData();
		this.playSound(names, pitch, volume, d.position.x, d.position.y, d.position.z);
	}

	public void playSound(String[] names, float pitch, float volume, float x, float y, float z) {
		int index = this.system.randomNumberGenerator.nextInt(names.length);
		this.playSound(names[index], pitch, volume, x, y, z, false);
	}
	
	public SoundSystem getSoundSystem() {
		return this.system;
	}
}
