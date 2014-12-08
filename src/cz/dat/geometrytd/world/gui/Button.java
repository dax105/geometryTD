package cz.dat.geometrytd.world.gui;

import java.awt.Point;
import java.awt.Rectangle;

import cz.dat.geometrytd.world.World;

public abstract class Button {
	protected Rectangle r;
	protected World w;
	
	protected boolean onDown = false;
	
	public Button(Rectangle r, World w) {
		this.r = r;
		this.w = w;
	}
	
	public void onDown(Point p) {
		if (r.contains(p)) {
			onDown = true;
		}
	}
	
	public void onUp(Point p) {
		if (onDown && r.contains(p)) {
			onPress();
		}	
		onDown = false;
	}
	
	public abstract void onPress();
	
	public abstract void render();
}
