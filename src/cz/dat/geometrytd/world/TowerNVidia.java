package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerNVidia extends Tower {

	public TowerNVidia(Game game, Level l) {
		super(game, 3, l);
	}

	@Override
	public void setLevel(int level) {

	}

	@Override
	public int getLevelCost(int level) {
		return 1000;
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
