package cz.dat.geometrytd.world;

import java.awt.Point;
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
	

	public Level(Game game, int pathTexture, LevelParser p) {
		super(game);
		this.towers = new ArrayList<Tower>();
		this.enemies = new ArrayList<Enemy>();
		
		this.parser = p;
		this.pathTexture = pathTexture;

		p.parse();
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
