package cz.dat.geometrytd.world;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class Level extends TickListener {

	private LevelParser parser;
	private int pathTexture;
	private List<Tower> towers;
	private List<Enemy> enemies;
	
	private Rectangle placingRectangle = new Rectangle(0, 0, 64, 64);

	public Level(Game game, int pathTexture, LevelParser p) {
		super(game);
		this.towers = new ArrayList<Tower>();
		this.enemies = new ArrayList<Enemy>();
		
		this.parser = p;
		this.pathTexture = pathTexture;

		p.parse();
	}
	
	public boolean canPlace(Point p) {
		this.placingRectangle = new Rectangle(p.x - World.TOWER_WIDTH_HALF, p.y - World.TOWER_WIDTH_HALF, 64, 64);
		return !this.parser.getPathPolygon().intersects(this.placingRectangle);
	}
	
	public void addTower(Tower t) {
		this.children.add(t);
		this.towers.add(t);
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
		
	}

}
