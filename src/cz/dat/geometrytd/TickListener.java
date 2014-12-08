package cz.dat.geometrytd;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class TickListener {
	protected List<TickListener> children;
	protected Game game;
	
	public TickListener(Game game) {
		children = new LinkedList<TickListener>();
		this.game = game;
	}
	
	public void onTick() {
		this.tick();
		
		for (Iterator<TickListener> iterator = this.children.iterator(); iterator
				.hasNext();) {
			TickListener t = iterator.next();
			t.onTick();
		}
	}
	
	public void onRenderTick(float ptt) {
		this.renderTick(ptt);
		
		for (Iterator<TickListener> iterator = this.children.iterator(); iterator
				.hasNext();) {
			TickListener t = iterator.next();
			t.onRenderTick(ptt);
		}
	}
	
	public Game getGame() {
		return this.game;
	}
	
	protected abstract void tick();
	protected abstract void renderTick(float ptt);
}
