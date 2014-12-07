package cz.dat.geometrytd.world;

import java.awt.Rectangle;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public abstract class Tower extends TickListener {

	private int tID;
	private int range;
	private Rectangle rec;
	
	public Tower(Game game, int textureID) {
		super(game);
		
		this.tID = textureID;
		this.rec = new Rectangle();
		this.rec.setSize((int)this.game.getTextureManager().getTexture(tID).SheetSize.x / 2,
				(int)this.game.getTextureManager().getTexture(tID).SheetSize.y / 2);
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
