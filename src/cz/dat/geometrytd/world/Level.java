package cz.dat.geometrytd.world;

import org.lwjgl.opengl.Display;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class Level extends TickListener {

	private int pathTexture;
	
	public Level(Game game, int pathTexture) {
		super(game);
		
		this.pathTexture = pathTexture;
	}

	@Override
	protected void tick() {

	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), this.pathTexture, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
	}
	
}
