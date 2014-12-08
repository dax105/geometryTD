package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerNVidia extends Tower {

	public TowerNVidia(Game game) {
		super(game, 3);
	}

	@Override
	public void setLevel(int level) {

	}

	@Override
	public int getLevelCost(int level) {
		return 1000;
	}

}
