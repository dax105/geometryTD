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
	private String[] towerNames = new String[] { "Snower", "Smacker",
			"Shocker", "Slower" };
	private Rectangle box;
	private int boxY, titleY, towerNameFontY, bigFontHeight, towerWidthHalf,
			selectedTowerY;
	private boolean overBox = false;
	private Point mousePoint = new Point();
	private float fogOffset = 0;

	private List<Button> buttons = new ArrayList<Button>();

	public World(Game game) {
		super(game);

		this.bigFontHeight = super.game.getFontManager()
				.getFont(FontManager.BIG).getHeight();
		this.boxY = 20;
		this.box = new Rectangle(Display.getWidth() - 260, this.boxY, 240, 500);
		this.titleY = this.boxY - (this.bigFontHeight / 4);
		this.towerNameFontY = this.titleY + this.bigFontHeight
				+ super.game.getFontManager().getFont().getHeight() / 2;
		this.towerWidthHalf = (int) super.game.getTextureManager()
				.getTexture(1).SheetSize.x / 2;
		this.selectedTowerY = this.titleY + this.bigFontHeight;

		this.changeLevel(new Level(game, 3, new LevelParser(this.getClass()
				.getResourceAsStream(Game.RES_DIR + "levels/l1.txt"))));

		buttons.add(new TextButton(Display.getWidth() - 250,
				this.selectedTowerY + 72, "Test", this) {
			@Override
			public void onPress() {
				System.out.println("Pressed");
			}
		});

	}

	private void changeLevel(Level newLevel) {
		this.children.remove(this.currentLevel);
		this.currentLevel = newLevel;
		this.children.add(this.currentLevel);
	}

	@Override
	protected void tick() {
		this.overBox = this.box.contains(this.mousePoint);
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				int k = Keyboard.getEventKey();

				if (k == Keyboard.KEY_LEFT) {
					this.currentTowerSelected--;
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

		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {

					if (!this.overBox) {
						this.addTower();
					}
					
					for (Button b : buttons) {
						if (Mouse.getEventButtonState()) {
							b.onDown(this.mousePoint);
						} else {
							b.onUp(this.mousePoint);
						}
					}
				}
			}
		}
		
		fogOffset += 0.001f;

		while (fogOffset > 2f) {
			fogOffset -= 2f;
		}
	}
	
	private void addTower() {
		Tower t = new TowerSnower(super.game);
		t.getRectangle().setLocation(
				Mouse.getX() - this.towerWidthHalf,
				Display.getHeight() - Mouse.getY()
						- this.towerWidthHalf);
		 this.currentLevel.addTower(t);
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
				this.currentTowerSelected, Display.getWidth() - 250,
				this.selectedTowerY);

		for (Button b : buttons) {
			b.render();
		}

		super.game.getFontManager().drawString("Current tower",
				Display.getWidth() - 250, this.titleY, FontManager.BIG,
				Color.white);
		super.game.getFontManager()
				.drawString(this.towerNames[this.currentTowerSelected - 1],
						Display.getWidth() - 250 + 74, this.towerNameFontY,
						Color.white);

	}

	@Override
	public void onRenderTick(float ptt) {
		super.onRenderTick(ptt);
		this.mousePoint.setLocation(Mouse.getX(), Display.getHeight() - Mouse.getY());

		if (!this.overBox) {
			GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1,
					this.currentTowerSelected, this.mousePoint.x
							- this.towerWidthHalf,
					this.mousePoint.y - this.towerWidthHalf);
		}
	}

}
