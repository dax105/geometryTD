package cz.dat.geometrytd.world;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;
import cz.dat.geometrytd.manager.FontManager;
import cz.dat.geometrytd.world.gui.Button;
import cz.dat.geometrytd.world.gui.TextButton;

public class World extends TickListener {

	private Level currentLevel;

	private int currentTowerSelected = 1;
	private int towers = 4;
	private boolean towerBought = false;
	private String[] towerNames = new String[] { "Intel", "AMD", "nVidia", "IE" };
	private Rectangle box;
	private Rectangle gridBox;

	public static final int TOWER_WIDTH_HALF = 32;

	private int boxY, titleY, towerNameFontY, bigFontHeight, towerWidthHalf,
			selectedTowerY, nextWaveFontY, scoreFontY, livesFontY, waveFontY,
			skipFontY;
	private boolean overBox = false;
	private Point mousePoint = new Point();
	private Point newTowerPoint = new Point();
	private float fogOffset = 0;

	private int score = 3500;
	private TowerInfo infoPanel;
	private int wave = 0;

	private List<Button> buttons = new ArrayList<Button>();

	public World(Game game) {
		super(game);

		this.bigFontHeight = super.game.getFontManager()
				.getFont(FontManager.BIG).getHeight();
		this.boxY = 20;
		this.box = new Rectangle(Display.getWidth() - 250, this.boxY,
				250 - this.boxY, Display.getHeight() - this.boxY * 2);
		this.gridBox = new Rectangle(0, 0, this.box.x - 10, Display.getHeight());
		this.titleY = this.boxY - (this.bigFontHeight / 4);
		this.towerNameFontY = this.titleY + this.bigFontHeight
				+ super.game.getFontManager().getFont().getHeight() / 2;
		this.towerWidthHalf = (int) super.game.getTextureManager()
				.getTexture(1).SheetSize.x / 2;
		this.selectedTowerY = this.titleY + this.bigFontHeight;
		this.nextWaveFontY = this.towerNameFontY + World.TOWER_WIDTH_HALF + 10;
		this.scoreFontY = this.nextWaveFontY
				+ game.getFontManager().getFont().getHeight();
		this.livesFontY = this.scoreFontY
				+ game.getFontManager().getFont().getHeight() - 12;
		this.waveFontY = this.livesFontY
				+ game.getFontManager().getFont().getHeight() - 12;
		this.skipFontY = this.waveFontY
				+ game.getFontManager().getFont().getHeight()-6;

		this.changeLevel(new Level(game, 3, new LevelParser(this.getClass()
				.getResourceAsStream(Game.RES_DIR + "levels/l1.txt"))));

		buttons.add(new TextButton(this.box.x + this.box.width - 50,
				this.selectedTowerY + 14, "Buy", this) {
			@Override
			public void onPress() {
				if (!towerBought) {
					Tower t = generateTower(currentTowerSelected);
					if (score >= t.getLevelCost(1)) {
						towerBought = true;
						score -= t.getLevelCost(1);
					} else {
						super.w.getGame().getSoundManager().playSound("error");
						towerBought = false;
					}
				}
			}
		});

		this.infoPanel = new TowerInfo(this);
		this.children.add(this.infoPanel);

	}

	private void changeLevel(Level newLevel) {
		this.children.remove(this.currentLevel);
		this.currentLevel = newLevel;
		this.children.add(this.currentLevel);
	}

	int tts = 0;
	int next = 25;

	private void nextWave() {
		wave++;
		currentLevel.wave(wave);
	}

	@Override
	protected void tick() {
		tts++;
		if (tts == 20) {
			tts = 0;
			next--;
		}

		if (next <= 0) {
			nextWave();
			tts = 0;
			next = 25;
		}

		this.overBox = this.gridBox.contains(this.mousePoint)
				&& this.currentLevel.canPlace(this.newTowerPoint);

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (!this.towerBought) {
					int k = Keyboard.getEventKey();

					if (k == Keyboard.KEY_LEFT) {
						this.currentTowerSelected--;
					}

					if (k == Keyboard.KEY_SPACE && this.currentLevel.canSkip()) {
						this.next = 0;
					}

					if (k == Keyboard.KEY_RIGHT) {
						this.currentTowerSelected++;
					}

					if (this.currentTowerSelected > this.towers)
						this.currentTowerSelected = 1;

					if (this.currentTowerSelected < 1)
						this.currentTowerSelected = this.towers;
				}
			}
		}

		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {

					if (this.overBox) {
						if (this.towerBought) {
							this.addTower();
							this.towerBought = false;
						} else {
							Tower t = this.currentLevel
									.getTowerAt(this.mousePoint);
							if (t != null) {
								t.setLevel(2);
							} else {
								this.infoPanel.openTower(t);
							}
						}
					}
				}
			}

			for (Button b : buttons) {
				if (Mouse.getEventButtonState()) {
					b.onDown(this.mousePoint);
				} else {
					b.onUp(this.mousePoint);
				}
			}
		}

		fogOffset += 0.001f;

		while (fogOffset > 2f) {
			fogOffset -= 2f;
		}
	}

	private int getFold(int fold) {
		int d = fold / 64;

		d *= 64;

		return d;
	}

	private void addTower() {
		Tower t = this.generateTower(this.currentTowerSelected);
		t.getRectangle().setLocation(this.getFold(this.newTowerPoint.x),
				this.getFold(this.newTowerPoint.y));
		this.currentLevel.addTower(t);
	}

	private Tower generateTower(int num) {
		switch (num) {
		case 1:
			return new TowerIntel(this.game);
		case 2:
			return new TowerAMD(this.game);
		case 3:
			return new TowerNVidia(this.game);
		case 4:
			return new TowerMajestic(this.game);
		}

		return null;
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), 2, 0f, 1f, 0f, 1f, 0,
				Display.getWidth(), 0, Display.getHeight());

		GL11.glColor4f(0.0f, 0.8f, 1.0f, 0.75f);
		GLUtil.drawTextureColored(game.getTextureManager(), 100, 0 + fogOffset,
				1 + fogOffset, 0 + fogOffset, 1
						* (Game.WINDOW_HEIGHT / (float) Game.WINDOW_WIDTH)
						+ fogOffset, 0, Game.WINDOW_WIDTH, 0,
				Game.WINDOW_HEIGHT);

		GLUtil.drawRectangle(0.5f, 0.5f, 0.5f, 0.75f, this.box.x, this.box.x
				+ this.box.width, this.box.y, this.box.y + this.box.height);
		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1,
				this.currentTowerSelected, this.box.x + 10, this.selectedTowerY);

		for (Button b : buttons) {
			b.render();
		}

		for (int i = 0; i < this.gridBox.width; i += 64) {
			GLUtil.drawLine(i, i, 0, this.gridBox.height, 2, 0.5f, 0.5f, 0.5f,
					0.5f);
		}

		for (int i = 0; i < this.gridBox.height; i += 64) {
			GLUtil.drawLine(0, this.gridBox.width, i, i, 2, 0.5f, 0.5f, 0.5f,
					0.5f);
		}

		super.game.getFontManager().drawString("Current tower", this.box.x + 5,
				this.titleY, FontManager.BIG, Color.white);
		super.game.getFontManager().drawString(
				this.towerNames[this.currentTowerSelected - 1],
				this.box.x + 80, this.towerNameFontY, Color.white);
		super.game.getFontManager().drawString("Score: " + this.score,
				this.box.x + 5, this.scoreFontY, Color.white);
		super.game.getFontManager().drawString("Lives: " + this.currentLevel.lives,
				this.box.x + 5, this.livesFontY, Color.white);
		super.game.getFontManager().drawString("Wave: " + this.wave,
				this.box.x + 5, this.waveFontY, Color.white);
		super.game.getFontManager().drawString("Next wave in: " + next + "s",
				this.box.x + 5, this.nextWaveFontY - 2, FontManager.BIG,
				Color.white);
		
		if (this.currentLevel.canSkip() && this.tts % 20 > 5) {
			super.game.getFontManager().drawString("-PRESS SPACE TO SKIP-", this.box.x + 12, this.skipFontY, FontManager.SMALL, Color.white);
		}
	}

	private boolean started = false;
	private String screenString = "Use arrows to change tower. Destroy all the potato consoles. Click to start.";
	private boolean isEnd = false;

	@Override
	public void onRenderTick(float ptt) {
		super.onRenderTick(ptt);

		if (started) {
			this.mousePoint.setLocation(Mouse.getX(), Display.getHeight()
					- Mouse.getY());
			this.newTowerPoint
					.setLocation(this.mousePoint.x, this.mousePoint.y);

			if (this.towerBought) {
				GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1,
						this.currentTowerSelected, this.newTowerPoint.x
								- towerWidthHalf, this.newTowerPoint.y
								- towerWidthHalf);
				
				if (!this.overBox) {
					GLUtil.drawRectangle(1, 0, 0, 0.5f, this.newTowerPoint.x - towerWidthHalf, this.newTowerPoint.x + towerWidthHalf, this.newTowerPoint.y - towerWidthHalf, this.newTowerPoint.y + towerWidthHalf);
				}
			}
		} else {
			GLUtil.drawRectangle(0.3f, 0.3f, 0.3f, 0.85f, 0,
					Display.getWidth(), 0, Display.getHeight());
			int width = this.game.getFontManager().getFont(FontManager.BIG).getWidth(this.screenString);
			int height = this.game.getFontManager().getFont(FontManager.BIG).getHeight(this.screenString);
			this.game.getFontManager().drawString(this.screenString, (Display.getWidth() / 2) - (width / 2),
					(Display.getHeight() / 2) - (height / 2), FontManager.BIG, Color.white);
		}
	}

	@Override
	public void onTick() {
		if (started) {
			super.onTick();
		} else {
			while(Mouse.next()) {
				if(Mouse.getEventButtonState()) {
					if(Mouse.getEventButton() == 0) {
						if(isEnd) {
							System.exit(0);
						} else {
							this.started = true;
							this.isEnd = true;
						}
					}
				}
			}
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
