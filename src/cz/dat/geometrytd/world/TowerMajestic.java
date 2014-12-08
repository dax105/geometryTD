package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerMajestic extends Tower {

	public TowerMajestic(Game game) {
		super(game, 4);
		
	}

	@Override
	public void setLevel(int level) {
		this.tID = 5 + level;
		this.damage = (level - 1) * 200;
		this.range = (level - 1) * 128;
		this.level = level;
	}

	@Override
	public int getNextLevelCost() {
		return this.level * 3000;
	}

}
