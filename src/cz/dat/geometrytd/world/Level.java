package cz.dat.geometrytd.world;

import org.lwjgl.opengl.Display;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class Level extends TickListener {

	private int pathTexture;
	private LevelParser parser;
	
	public Level(Game game, int pathTexture, LevelParser p) {
		super(game);
		
		this.pathTexture = pathTexture;
		this.parser = p;
		
		p.parse();
	}

	@Override
	protected void tick() {

	}

	@Override
	protected void renderTick(float ptt) {
		java.awt.Polygon p = this.parser.getPathPolygon();
		GLUtil.drawTexture(game.getTextureManager(), this.pathTexture, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
		
		for(int i = 0; i < p.xpoints.length - 1; i++) {
			GLUtil.drawLine(p.xpoints[i], p.xpoints[i + 1],
					p.ypoints[i], p.ypoints[i + 1], 2, 1f, 0.2f, 0.2f, 1f);
		}
	}
	
}
