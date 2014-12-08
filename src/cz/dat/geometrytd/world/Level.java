package cz.dat.geometrytd.world;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;
import cz.dat.geometrytd.world.gui.YluminatyPlox;

public class Level extends TickListener {

	private LevelParser parser;
	private int pathTexture;
	private List<Tower> towers;
	private List<Enemy> enemies;
	
	private Rectangle placingRectangle = new Rectangle(0, 0, 64, 64);

	public Level(Game game, int pathTexture, LevelParser p) {
		super(game);
		this.children.add(new YluminatyPlox(game));
		this.towers = new ArrayList<Tower>();
		this.enemies = new ArrayList<Enemy>();
		
		this.parser = p;
		this.pathTexture = pathTexture;

		p.parse();
		
		enemies.add(new Enemy(game, p.getPoints(false), 10));
	}
	
	public boolean canPlace(Point p) {
		this.placingRectangle = new Rectangle(p.x - World.TOWER_WIDTH_HALF, p.y - World.TOWER_WIDTH_HALF, 64, 64);
		return !this.parser.getPathPolygon().intersects(this.placingRectangle);
	}
	
	public void addTower(Tower t) {
		for(Tower ct : towers) {
			if(ct.getRectangle().intersects(t.getRectangle()))
				return;
		}
		
		this.children.add(t);
		this.towers.add(t);
	}

	public Tower getTowerAt(Point p) {
		for(Tower t : this.towers) {
			if(t.getRectangle().contains(p))
				return t;
		}
		
		return null;
	}
	
	@Override
	protected void tick() {
		for (Enemy e : enemies) {
			e.onTick();
		}
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), this.pathTexture, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
		
		for (Enemy e : enemies) {
			e.onRenderTick(ptt);
		}
		
	}

}
