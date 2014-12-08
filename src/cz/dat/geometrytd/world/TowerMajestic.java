package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerMajestic extends Tower {

	public TowerMajestic(Game game) {
		super(game, 4);
	}

	int r2 = 128, r3 = 256;
	int d2 = 20, d3 = 500;

	@Override
	public void setLevel(int level) {
		if (this.level < 3) {
			this.tID = 3 + (level == 2 ? 5 : 2);
			this.damage = (level == 2 ? d2 : d3);
			this.range = (level == 2 ? r2 : r3);
			this.level = level;

			if (level > 2)
				super.game.getSoundManager().playMusic("gaben", true);
		}
	}

	@Override
	public int getLevelCost(int level) {
		return level == 3 ? 5000 : (level == 2 ? 1000 : 200);
	}

	@Override
	public int getNextRange() {
		return level == 3 ? r3 : (level == 2 ? r2 : 0);
	}

	@Override
	public int getNextDamage() {
		return level == 3 ? d3 : (level == 2 ? d2 : 0);
	}

}
