package cz.dat.geometrytd.world;

import cz.dat.geometrytd.Game;

public class TowerMajestic extends Tower {

	public TowerMajestic(Game game, Level l) {
		super(game, 4, l);
		this.range = 5;
		this.damage = 5;
		this.cooldown = 100;
	}

	int r2 = 128, r3 = 256;
	int d2 = 20, d3 = 500;

	@Override
	public void setLevel(int level) {
		if (this.canUpgrade()) {
			switch(level) {
			case 2:
				this.range = r2;
				this.damage = d2;
				this.tID = 8;
				break;
			case 3:
				this.range = r3;
				this.damage = d3;
				this.tID = 5;
				this.game.getSoundManager().playMusic("gaben", true);
				break;
			}
			
			this.level = level;
		}
	}

	@Override
	public boolean canUpgrade() {
		return this.level < 3;
	}


	@Override
	public int getLevelCost(int level) {
		return (level > 1) ? level * 5000 : 200;
	}


	@Override
	public int getNextRange() {
		switch(this.level + 1) {
		case 2:
			return r2;
		case 3:
			return r3;
		}
		
		return r3;
	}


	@Override
	public int getNextDamage() {
		switch(this.level + 1) {
		case 2:
			return d2;
		case 3:
			return d3;
		}
		
		return d3;
	}

}
