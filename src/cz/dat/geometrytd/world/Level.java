package cz.dat.geometrytd.world;

import java.awt.Point;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class Level extends TickListener {

	private LevelParser parser;
	private int pathTexture;
	private Point[] pts;
	private Point[] pts2;

	public Level(Game game, int pathTexture, LevelParser p) {
		super(game);

		this.parser = p;
		this.pathTexture = pathTexture;

		p.parse();
		this.pts = p.getPoints(false);
		this.pts2 = p.getPoints(true);
	}

	@Override
	protected void tick() {
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), this.pathTexture, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
		
		GLUtil.drawRectangle(Color.green, parser.getStart().x, parser.getStart().x + parser.getStart().width, 
				parser.getStart().y, parser.getStart().y + parser.getStart().height);
		
		GLUtil.drawRectangle(Color.red, parser.getEnd().x, parser.getEnd().x + parser.getEnd().width, 
				parser.getEnd().y, parser.getEnd().y + parser.getEnd().height);
		
		for (int i = 0; i < pts.length - 1; i++) {
			Point p1 = pts[i];
			Point p2 = pts[i + 1];
			GLUtil.drawLine(p1.x, p2.x, p1.y, p2.y, 2, 1f, 0.2f, 0.2f, 1f);
		}
		
		for (int i = 0; i < pts2.length - 1; i++) {
			Point p1 = pts2[i];
			Point p2 = pts2[i + 1];
			GLUtil.drawLine(p1.x, p2.x, p1.y, p2.y, 2, 1f, 0.2f, 0.2f, 1f);
		}
	}

}
