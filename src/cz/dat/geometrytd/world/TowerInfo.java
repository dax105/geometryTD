package cz.dat.geometrytd.world;

import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class TowerInfo extends TickListener {

	private World w;
	private Tower currentTower;
	private boolean show;
	
	int startX;
	
	public TowerInfo(World world) {
		super(world.getGame());
		this.w = world;
	}
	
	public void setShowed(boolean state) {
		this.show = state;
	}
	
	public void openTower(Tower t) {
		this.currentTower = t;
		this.setShowed(true);
	}

	@Override
	protected void tick() {

	}

	@Override
	protected void renderTick(float ptt) {
		if(this.show) {
			GLUtil.drawCircle(this.currentTower.rec.x + (this.currentTower.rec.width / 2),
					this.currentTower.rec.y + (this.currentTower.rec.height / 2), this.currentTower.range);
		}
	}

}
