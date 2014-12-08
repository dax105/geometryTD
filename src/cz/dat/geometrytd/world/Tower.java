package cz.dat.geometrytd.world;

import java.awt.Rectangle;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public abstract class Tower extends TickListener {

	protected int tID;
	protected int range;
	protected Rectangle rec;
	protected int level;
	protected int damage;
	
	
	public Tower(Game game, int textureID) {
		super(game);
		
		this.tID = textureID;
		this.rec = new Rectangle();
		this.rec.setSize((int)this.game.getTextureManager().getTexture(1).SheetSize.x / 2,
				(int)this.game.getTextureManager().getTexture(1).SheetSize.y / 2);
	}
	
	public abstract void setLevel(int level);
	public abstract int getNextLevelCost();
	
	
	public int getLevel() {
		return this.level;
	}
	
	public int getRange() {
		return this.range;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public Rectangle getRectangle() {
		return this.rec;
	}

	@Override
	protected void tick() {
		
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, this.tID, this.rec.x, this.rec.y);
	}

}
