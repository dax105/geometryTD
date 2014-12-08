package cz.dat.geometrytd.world;

import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

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
		this.level = 1;
		this.tID = textureID;
		this.rec = new Rectangle();
		this.rec.setSize((int)this.game.getTextureManager().getTexture(1).SheetSize.x,
				(int)this.game.getTextureManager().getTexture(1).SheetSize.y);
	}
	
	public abstract void setLevel(int level);
	public abstract int getLevelCost(int level);
	public abstract int getNextRange();
	public abstract int getNextDamage();
	
	public void upgrade() {
		this.setLevel(this.level + 1);
	}
	
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
		GL11.glTranslatef(+this.rec.x + World.TOWER_WIDTH_HALF, +this.rec.y +World.TOWER_WIDTH_HALF, 0);
		GL11.glPushMatrix();
		GL11.glRotatef( (float) (System.nanoTime()/50000000D), 0, 0, 1);
		GL11.glTranslatef(-this.rec.x - World.TOWER_WIDTH_HALF, -this.rec.y -World.TOWER_WIDTH_HALF, 0);

		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, this.tID, this.rec.x, this.rec.y);
		GL11.glPopMatrix();
		GL11.glTranslatef(-this.rec.x - World.TOWER_WIDTH_HALF, -this.rec.y -World.TOWER_WIDTH_HALF, 0);
	}

}
