package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerNVidia extends Tower {

	public TowerNVidia(Game game, Level l) {
		super(game, 3, l);
		this.range = 128;
		this.damage = 20;
		this.cooldown = 5;
	}

	@Override
	public void setLevel(int level) {
		if(this.canUpgrade()) {
			this.range += level * 10;
			this.damage += level * 5;
			this.level = level;
		}
	}

	@Override
	public int getLevelCost(int level) {
		return 1000 + level * 100;
	}

	@Override
	public int getNextRange() {
		return this.range + ((this.level + 1) * 10);
	}

	@Override
	public int getNextDamage() {
		return this.damage + ((this.level + 1) * 5);
	}

	@Override
	public boolean canUpgrade() {
		return this.level < 5;
	}

}
