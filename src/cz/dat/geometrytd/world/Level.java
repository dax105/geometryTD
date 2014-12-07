package cz.dat.geometrytd.world;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class Level extends TickListener {

	private int pathTexture;
	private LevelParser parser;
	private Rectangle2D rct;
	private boolean isc = false;
	
	public Level(Game game, int pathTexture, LevelParser p) {
		super(game);
		
		this.pathTexture = pathTexture;
		this.parser = p;
		this.rct = new Rectangle(0, 0, 100, 100);
		p.parse();
	}

	@Override
	protected void tick() {
		this.isc = !this.parser.getPathPolygon().intersects(this.rct);
	}

	@Override
	protected void renderTick(float ptt) {
		this.rct.setRect(Mouse.getX() - 50, Display.getHeight() - Mouse.getY() - 50, 100, 100);
		java.awt.Polygon p = this.parser.getPathPolygon();
		GLUtil.drawTexture(game.getTextureManager(), this.pathTexture, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
		
		for(int i = 0; i < p.xpoints.length - 1; i++) {
			GLUtil.drawLine(p.xpoints[i], p.xpoints[i + 1],
					p.ypoints[i], p.ypoints[i + 1], 2, 1f, 0.2f, 0.2f, 1f);
		}
		
		GLUtil.drawRectangle(this.isc ? Color.green : Color.red, (float)this.rct.getX(),
				(float)this.rct.getX() + (float)this.rct.getWidth(), 
				(float)this.rct.getY(), (float)this.rct.getY() + (float)this.rct.getHeight());
	}
	
}
