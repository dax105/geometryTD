package cz.dat.geometrytd.world;

import org.lwjgl.input.Keyboard;
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
	
	private int towerNameFontX, towerNameFontY;
	
	public World(Game game) {
		super(game);
		this.towerNameFontX = Display.getWidth() - 250 + 74;
		this.towerNameFontY = super.game.getFontManager().getFont(FontManager.BIG).getHeight() + 24;
		
		
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
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), 2, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
		
		GLUtil.drawRectangle(0.5f, 0.5f, 0.5f, 0.75f, Display.getWidth() - 260, Display.getWidth() - 20, 
				20, 300);
		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, this.currentTowerSelected, Display.getWidth() - 250,
				super.game.getFontManager().getFont(FontManager.BIG).getHeight() + 10);
		
		super.game.getFontManager().drawString("Current tower", Display.getWidth() - 250, 10, FontManager.BIG, Color.white);
		super.game.getFontManager().drawString(this.towerNames[this.currentTowerSelected - 1], this.towerNameFontX,
				this.towerNameFontY, Color.white);
	}
	
}
