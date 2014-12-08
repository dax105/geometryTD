package cz.dat.geometrytd.world;

import java.awt.Point;

import org.newdawn.slick.Color;

import cz.dat.geometrytd.gl.GLUtil;

public class ShootEffect {
	
	private Point p0, p1;
	private Color c;
	
	public int duration, fade, time = 0;
	
	public void draw(float ptt) {
		if (time <= (duration-fade)) {
			GLUtil.drawLine(p0.x, p1.x, p0.y, p1.y, 5, c.r, c.g, c.b, c.a);
		} else {
			float f = (duration - time) / (float)fade;
			float a = c.a * f;
			GLUtil.drawLine(p0.x, p1.x, p0.y, p1.y, 5, c.r, c.g, c.b, a);
		}
	}
	
	public void tick() {
		time++;
	}
	
	public ShootEffect(Color c, Point p0, Point p1, int duration, int fade) {
		this.p0 = p0;
		this.p1 = p1;
		this.c = c;
		this.duration = duration;
		this.fade = fade;
	}
}
