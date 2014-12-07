package cz.dat.geometrytd.world;

import org.lwjgl.opengl.Display;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class World extends TickListener {

	public World(Game game) {
		super(game);
	}
	
	@Override
	protected void tick() {
		
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), 2, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
	}
	
}
