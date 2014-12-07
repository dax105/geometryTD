package cz.dat.geometrytd.world;

import org.newdawn.slick.Color;

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
		GLUtil.drawRectangle(Color.pink, 50, 100, 50, 100);
	}
	
}
