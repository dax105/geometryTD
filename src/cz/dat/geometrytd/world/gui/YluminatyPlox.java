package cz.dat.geometrytd.world.gui;

import java.util.Random;

import org.lwjgl.opengl.Display;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;

public class YluminatyPlox extends TickListener {

	Random rnd = new Random();

	public YluminatyPlox(Game game) {
		super(game);
	}

	int ticks = 0;
	int ticksDrawed = 0;
	boolean draw = false;
	boolean drawLord = false;
	int x, y;

	@Override
	protected void tick() {
		ticks++;

		if (ticks == 300) {
			draw = true;
			if (rnd.nextInt(2) == 100) {
				drawLord = true;
			}
			x = rnd.nextInt(900);
			y = rnd.nextInt(400);
		}

		if (draw) {
			ticksDrawed++;
		}

		if (ticksDrawed >= 5) {
			draw = false;
			drawLord = false;
			ticks = 0;
			ticksDrawed = 0;
		}
	}

	@Override
	protected void renderTick(float ptt) {
		if (this.draw) {
			GLUtil.drawTexture(super.game.getTextureManager(), 666, 0f, 1f, 0f,
					1f, x, x + 119, y, y + 120);

			if (this.drawLord)
				GLUtil.drawTexture(super.game.getTextureManager(), 6927, 0f,
						1f, 0f, 1f, 0, Display.getWidth(), 0,
						Display.getHeight());
		}
	}
}
