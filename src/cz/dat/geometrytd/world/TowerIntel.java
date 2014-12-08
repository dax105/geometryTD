package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerIntel extends Tower {

	public TowerIntel(Game game) {
		super(game, 1);
		this.range = 20;
		this.damage = 5;
		this.level = 1;
	}

	@Override
	public void setLevel(int level) {
		
	}

	@Override
	public int getNextLevelCost() {
		// TODO Auto-generated method stub
		return 0;
	}

}
