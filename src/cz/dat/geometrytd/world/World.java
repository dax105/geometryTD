package cz.dat.geometrytd.world;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;
import cz.dat.geometrytd.manager.FontManager;

public class World extends TickListener {

	private Level currentLevel;
	
	private int currentTowerSelected = 1;
	private int towers = 4;
	private String[] towerNames = new String[] { "Snower", "Smacker", "Shocker", "Slower" };
	private int boxY, titleY, towerNameFontY, bigFontHeight, towerWidthHalf;
	
	public World(Game game) {
		super(game);
		
		this.bigFontHeight = super.game.getFontManager().getFont(FontManager.BIG).getHeight();
		this.boxY = 20;
		this.titleY = this.boxY - (this.bigFontHeight / 4);
		this.towerNameFontY = this.titleY + this.bigFontHeight
				+ super.game.getFontManager().getFont().getHeight() / 2;
		this.towerWidthHalf = (int)super.game.getTextureManager().getTexture(1).SheetSize.x / 2;
		
		this.changeLevel(new Level(game, 3, 
				new LevelParser(this.getClass().getResourceAsStream(Game.RES_DIR + "levels/l1.txt"))));
		
	}
	
	
	private void changeLevel(Level newLevel) {
		this.children.remove(this.currentLevel);
		this.currentLevel = newLevel;
		this.children.add(this.currentLevel);
	}
	
	@Override
	protected void tick() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				int k = Keyboard.getEventKey();
				
				if(k == Keyboard.KEY_LEFT) {
					this.currentTowerSelected--;
				}
				
				if(k == Keyboard.KEY_RIGHT) {
					this.currentTowerSelected++;
				}
				
				if(this.currentTowerSelected > this.towers)
					this.currentTowerSelected = 1;
				
				if(this.currentTowerSelected < 1)
					this.currentTowerSelected = this.towers;
			}
		}
		
		while(Mouse.next()) {
			if(Mouse.getEventButtonState()) {
				if(Mouse.getEventButton() == 0) {
					Tower t = new TowerSnower(super.game);
					t.getRectangle().setLocation(Mouse.getX() - this.towerWidthHalf,
							Display.getHeight() - Mouse.getY() -  this.towerWidthHalf);
					this.currentLevel.addTower(t);
				}
			}
		}
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), 2, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
		
		GLUtil.drawRectangle(0.5f, 0.5f, 0.5f, 0.75f, Display.getWidth() - 260, Display.getWidth() - 20, 
				this.boxY, 300);
		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, this.currentTowerSelected, Display.getWidth() - 250,
				this.titleY + this.bigFontHeight);
		
		super.game.getFontManager().drawString("Current tower", Display.getWidth() - 250, this.titleY,
				FontManager.BIG, Color.white);
		super.game.getFontManager().drawString(this.towerNames[this.currentTowerSelected - 1], Display.getWidth() - 250 + 74,
				this.towerNameFontY, Color.white);
	}
	
	@Override
	public void onRenderTick(float ptt) {
		super.onRenderTick(ptt);
		
		
		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, 
				this.currentTowerSelected, Mouse.getX() - this.towerWidthHalf, 
				Display.getHeight() - Mouse.getY() - this.towerWidthHalf);
	}
	
}
