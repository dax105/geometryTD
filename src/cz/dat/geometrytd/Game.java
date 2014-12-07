package cz.dat.geometrytd;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Game implements Runnable {

	public static final int WINDOW_WIDTH = 854;
	public static final int WINDOW_HEIGHT = 480;
	public static final String GAME_NAME = "Geometry TD";
	
	private static final int TPS = 20;
	private static final double TICK_TIME = 1.0D / TPS;
	
	private void tick(int ticks) {
		
	}
	
	private void renderTick(float ptt) {
		
	}
	
	@Override
	public void run() {
		initDisplay();
		
		long time = System.nanoTime();
		long lastTime = time;
		long lastInfo = time;
		
		int fps = 0;
		int ticks = 0;
		int lastTicks = 0;
		
		while (!Display.isCloseRequested()) {
			int e = GL11.glGetError();
			if(e != 0) {
				System.err.println("GL ERROR: " + e + " - " + GLU.gluErrorString(e));
			}	
			
			this.renderTick(1);
			fps++;
			
			time = System.nanoTime();
			while (time - lastTime >= Game.TICK_TIME * 1000000000) {
				this.tick(ticks++);
				lastTime += Game.TICK_TIME * 1000000000;
			}

			if (time - lastInfo >= 1000000000) {
				lastInfo += 1000000000;
				Display.setTitle(GAME_NAME + " - " + fps + " fps - " + (ticks - lastTicks) + " tps");
				lastTicks = ticks;
				fps = 0;
			}
			
			Display.update();
		}
	}
	
	public static void initDisplay() {
		try {
			DisplayMode d = new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT);
			Display.setDisplayMode(d);
			Display.setTitle(GAME_NAME);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		Thread t = new Thread(new Game());
		t.start();
	}
	
}
