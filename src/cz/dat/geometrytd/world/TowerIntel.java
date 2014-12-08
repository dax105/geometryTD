package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerIntel extends Tower {

	public TowerIntel(Game game, Level l) {
		super(game, 1, l);
		this.range = 20;
		this.damage = 5;
		this.level = 1;
	}

	@Override
	public void setLevel(int level) {
		
	}

	@Override
	public int getLevelCost(int level) {
		return 500;
	}

	@Override
	public int getNextRange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNextDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
