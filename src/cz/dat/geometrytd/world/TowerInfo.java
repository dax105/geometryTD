package cz.dat.geometrytd.world;

import java.awt.Point;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;
import cz.dat.geometrytd.world.gui.TextButton;

public class TowerInfo extends TickListener {

	private World w;
	private Tower currentTower;
	private boolean show;
	private TextButton upgradeButton;
	private Point mousePoint = new Point();

	private int startX, levelY, rangeY, damageY;

	public TowerInfo(World world, int startX, int startY) {
		super(world.getGame());
		this.w = world;

		this.startX = startX;
		this.levelY = startY + 32;
		this.rangeY = this.levelY
				+ this.game.getFontManager().getFont().getHeight() - 12;
		this.damageY = this.rangeY
				+ this.game.getFontManager().getFont().getHeight() - 12;
		
		this.upgradeButton = new TextButton(this.startX, this.damageY + 32, "Upgrade", this.w) {
			@Override
			public void onPress() {
				if(this.w.score >= currentTower.getLevelCost(currentTower.level + 1)) {
					this.w.score -= currentTower.getLevelCost(currentTower.level + 1);
					currentTower.upgrade();
				}
			}
		};
	}

	public void setShowed(boolean state) {
		this.show = state;
	}

	public void openTower(Tower t) {
		this.currentTower = t;
		this.setShowed(true);
	}

	@Override
	protected void tick() {
	}

	@Override
	protected void renderTick(float ptt) {
		if (this.show) {
			GLUtil.drawCircle(this.currentTower.rec.x
					+ (this.currentTower.rec.width / 2),
					this.currentTower.rec.y
							+ (this.currentTower.rec.height / 2),
					this.currentTower.range);

			this.game
					.getFontManager()
					.drawString(
							"Level: "
									+ this.currentTower.getLevel()
									+ (this.currentTower.canUpgrade() ? (" (next: "
											+ this.currentTower.getLevelCost(this.currentTower
													.getLevel() + 1) + " pts)")
											: " (max)"), this.startX,
							this.levelY, Color.white);
			this.game
					.getFontManager()
					.drawString(
							"Range: "
									+ this.currentTower.getRange()
									+ " (+"
									+ (this.currentTower.getNextRange() - this.currentTower.getRange())
									+ ")", this.startX, this.rangeY,
							Color.white);
			this.game
					.getFontManager()
					.drawString(
							"Damage: "
									+ this.currentTower.getDamage()
									+ " (+"
									+ (this.currentTower.getNextDamage() - this.currentTower.getDamage())
									+ ")", this.startX, this.damageY,
							Color.white);
			
			
			this.game.getTextureManager().clearBind();
			this.upgradeButton.render();
		}
	}
}
