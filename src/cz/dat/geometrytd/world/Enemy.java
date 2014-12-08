package cz.dat.geometrytd.world;

import java.awt.Point;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class Enemy extends TickListener {

	boolean dead = false;
	public boolean attacked = false;
	
	Point[] points;
	float[] segLenghts;
	float[] segTotals;
	
	float x, y;
	float lastX, lastY;
	float totalDist = 0;
	float speed = 3;
	
	int maxLife;
	int life;
	
	public Enemy(Game game, Point[] points, int life) {
		super(game);
		
		this.maxLife = life;
		this.life = this.maxLife;
		
		this.points = points;
		x = points[0].x;
		y = points[0].y;
		
		segLenghts = new float[points.length-1];
		segTotals = new float[points.length-1];
		
		float total = 0;
		
		for (int i = 0; i < segLenghts.length; i++) {
			float dist = getDist(points[i], points[i+1]);
			segLenghts[i] = dist;
			total += dist;
			segTotals[i] = total;
		}
	}

	private float getDist(Point p0, Point p1) {
		return (float) p0.distance(p1);
	}
	
	@Override
	protected void tick() {
		this.lastX = x;
		this.lastY = y;
		
		this.totalDist += this.speed;
		updatePosition();
	}

	private void updatePosition() {
		int seg = getSeg(this.totalDist);	
		float segStart = seg == 0 ? 0 : segTotals[seg - 1];	
		float segLength = segLenghts[seg];	
		float distInSeg = totalDist - segStart;		
		float progress = distInSeg / segLength;
		
		Point xy0 = points[seg];
		Point xy1 = points[seg+1];
		
		this.x = xy0.x + (xy1.x - xy0.x)*progress;
		this.y = xy0.y + (xy1.y - xy0.y)*progress;
		
	}
	
	private int getSeg(float dist) {
		int seg = 0;
		
		while (segTotals[seg] < dist) {
			seg++;
			if (seg >= segTotals.length) {
				dead = true;
				attacked = true;
				return 0;
			}
		}

		return seg;
	}
	
	@Override
	protected void renderTick(float ptt) {
		if (!dead) {
			float cx = lastX + (x - lastX)*ptt;
			float cy = lastY + (y - lastY)*ptt;
			
			GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, 6, cx-16, cx+16, cy-16, cy+16);
		}	
	}

}
