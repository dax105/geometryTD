package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerMajestic extends Tower {

	public TowerMajestic(Game game, Level l) {
		super(game, 4, l);
	}
	

	@Override
	public void setLevel(int level) {
		this.tID = 3 + (level == 2 ? 5 : 2);
		this.damage =  (level == 2 ? 20 : 500);
		this.range =  (level == 2 ? 32 : 128);
		this.level = level;
		
		if(level > 2)
			super.game.getSoundManager().playMusic("gaben", true);
	}

	@Override
	public int getLevelCost(int level) {
		return level == 3 ? 5000 : (level == 2 ? 1000 : 200);
	}

	@Override
	public int getNextRange() {
		return level == 3 ? 128 : (level == 2 ? 32 : 0);
	}

	@Override
	public int getNextDamage() {
		return level == 3 ? 500 : (level == 2 ? 20 : 0);
	}

}
