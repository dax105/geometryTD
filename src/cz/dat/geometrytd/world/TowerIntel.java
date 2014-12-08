package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerIntel extends Tower {

	public TowerIntel(Game game, Level l) {
		super(game, 1, l);
		this.range = 100;
		this.damage = 10;
		this.cooldown = 20;
		this.level = 1;
	}

	@Override
	public void setLevel(int level) {
		if(this.canUpgrade()) {
			this.range = 150;
			this.damage = 15;
			this.cooldown = 15;
			this.level = level;
		}
	}

	@Override
	public int getLevelCost(int level) {
		return 500 + level * 250;
	}

	@Override
	public int getNextRange() {
		return 150;
	}

	@Override
	public int getNextDamage() {
		return 15;
	}

	@Override
	public boolean canUpgrade() {
		return this.level < 2;
	}

}
