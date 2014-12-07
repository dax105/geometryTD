package cz.dat.geometrytd.manager.sound;

import java.util.Random;

public class MusicProvider {
	private String[] gameMusic;
	private String[] menuMusic;

	private String[] menuQueue;
	private String[] gameQueue;

	private int menuIndex = 0;
	private int gameIndex = 0;

	private final int startGameMusicProbability = 10;

	private SoundManager sound;

	private Random rand = new Random();
	private boolean isGame = false;
	private boolean wasInGame = false;

	public MusicProvider(SoundManager soundManager) {
		this.sound = soundManager;
		this.loadMusic();
		this.sortMusic();
	}

	private void loadMusic() {

	}

	private void sortMusic() {
		this.gameMusic = new String[] { "game1", "game2", "game3" };
		this.gameQueue = new String[this.gameMusic.length];
		for(String music : this.gameMusic) {
			this.addMusicToField(this.gameQueue, music);
		}

		this.menuMusic = new String[] { "menu1" };
		this.menuQueue = new String[this.menuMusic.length];
		for(String music : this.menuMusic) {
			this.addMusicToField(this.menuQueue, music);
		}
	}

	private void addMusicToField(String[] queue, String name) {
		int i = this.rand.nextInt(queue.length);
		if(queue[i] != null) {
			this.addMusicToField(queue, name);
		} else {
			queue[i] = name;
		}
	}

	public void updateMusic(boolean isInGame) {
		this.wasInGame = this.isGame;
		this.isGame = isInGame;

		if(this.isGame ^ this.wasInGame) {
			this.stopMusic();
			this.sound.updatePlaying();
			return;
		}

		if(!this.sound.isMusicPlaying()) {
			if(this.isGame) {
				if(this.rand.nextInt(this.startGameMusicProbability) == 1) {
					this.sound.playMusic(gameQueue[gameIndex], false);

					this.gameIndex++;

					if(this.gameIndex > (this.gameQueue.length - 1)) {
						this.gameIndex = 0;
					}
				}
			} else {
				this.sound.playMusic(this.menuQueue[this.menuIndex], false);

				this.menuIndex++;

				if(this.menuIndex > (this.menuQueue.length - 1)) {
					this.menuIndex = 0;
				}

			}
		}
	}

	public void stopMusic() {
		this.sound.stopMusic();
	}
}
