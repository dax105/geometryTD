package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerAMD extends Tower {

	public TowerAMD(Game game, Level l) {
		super(game, 2, l);
		this.cooldown = 10;
		this.range = 128;
		this.damage = 20;
	}

	@Override
	public void setLevel(int level) {
		if(this.canUpgrade()) {
			this.range += level * 5;
			this.damage += level * 10;
			this.level = level;
		}
	}

	@Override
	public int getLevelCost(int level) {
		return 1000 + level * 100;
	}

	@Override
	public int getNextRange() {
		return this.range + ((this.level + 1) * 5);
	}

	@Override
	public int getNextDamage() {
		return this.damage + ((this.level + 1) * 10);
	}

	@Override
	public boolean canUpgrade() {
		return this.level < 6;
	}

}
