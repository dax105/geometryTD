package cz.dat.geometrytd.world;

import java.awt.Point;
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
	protected int cooldown;
	protected Level l;
	
	protected int timer = 0;
	
	public Tower(Game game, int textureID, Level l) {
		super(game);
		this.l = l;
		this.level = 1;
		this.range = 130;
		this.damage = 1;
		this.cooldown = 5;
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
		
		timer--;
		
		if (timer <= 0) {
		
		float ld = 0;
		Enemy closest = null;
		
		Point pos = new Point(this.rec.x+World.TOWER_WIDTH_HALF, this.rec.y+World.TOWER_WIDTH_HALF);
		
		for (Enemy e : l.getEnemies()) {
			
			float d = (float) new Point((int)e.x, (int)e.y).distance(pos);
			
			if (d <= this.range) {
				if (closest == null) {
					closest = e;
					ld = d;
				} else if (d < ld) {
					closest = e;
					ld = d;
				}
			}
		}
		
		if (closest != null) {
			this.game.getSoundManager().playSound("laser");
			this.l.shoot(new ShootEffect(org.newdawn.slick.Color.yellow, pos, new Point((int)closest.x, (int)closest.y), 5, 2));
			closest.life -= this.damage;
			timer = cooldown;
		}
		}
		
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
