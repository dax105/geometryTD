package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerMajestic extends Tower {

	public TowerMajestic(Game game) {
		super(game, 4);
		
	}

	@Override
	public void setLevel(int level) {
		this.tID = 3 + level;
		this.damage = 500;
		this.range = 128;
		this.level = level;
		
		super.game.getSoundManager().playMusic("gaben", true);
	}

	@Override
	public int getLevelCost(int level) {
		return level == 2 ? 2000 : 100;
	}

}
