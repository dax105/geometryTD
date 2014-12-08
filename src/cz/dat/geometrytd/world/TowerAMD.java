package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerAMD extends Tower {

	public TowerAMD(Game game) {
		super(game, 2);
	}

	@Override
	public void setLevel(int level) {
		// TODO Auto-generated method stub
		
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
