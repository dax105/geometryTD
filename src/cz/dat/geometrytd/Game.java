package cz.dat.geometrytd;

import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import cz.dat.geometrytd.manager.FontManager;
import cz.dat.geometrytd.manager.TextureManager;
import cz.dat.geometrytd.world.World;

public class Game implements Runnable {

	public static final int WINDOW_WIDTH = 960;
	public static final int WINDOW_HEIGHT = 540;
	public static final String GAME_NAME = "Geometry TD";
	public static final String RES_DIR = "/cz/dat/geometrytd/resources/";
	public static final Logger log = Logger.getLogger("GeometryTD_log");
	
	private static final int TPS = 20;
	private static final double TICK_TIME = 1.0D / TPS;
	private static final int MAGIC_CONSTANT = 1000000000;
	
	private TextureManager textureManager;
	private FontManager fontManager;
	
	private World world;

	private void tick(int ticks) {
		this.world.onTick();
	}

	private void renderTick(float ptt) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		this.world.onRenderTick(ptt);
	}

	@Override
	public void run() {
		Game.initDisplay();
		
		this.textureManager = new TextureManager();
		this.fontManager = new FontManager();

		long time = System.nanoTime();
		long lastTime = time;
		long lastInfo = time;

		int fps = 0;
		int ticks = 0;
		int lastTicks = 0;
		
		this.world = new World(this);

		while (!Display.isCloseRequested()) {
			int e = GL11.glGetError();
			if (e != 0) {
				System.err.println("GL ERROR: " + e + " - "
						+ GLU.gluErrorString(e));
			}

			float ptt = (time - lastTime) / ((float) Game.TICK_TIME * Game.MAGIC_CONSTANT);

			this.renderTick(ptt);
			fps++;

			time = System.nanoTime();
			while (time - lastTime >= Game.TICK_TIME * Game.MAGIC_CONSTANT) {
				this.tick(ticks++);
				lastTime += Game.TICK_TIME * Game.MAGIC_CONSTANT;
			}

			if (time - lastInfo >= Game.MAGIC_CONSTANT) {
				lastInfo += Game.MAGIC_CONSTANT;
				Display.setTitle(Game.GAME_NAME + " - " + fps + " fps - "
						+ (ticks - lastTicks) + " tps");
				lastTicks = ticks;
				fps = 0;
			}

			Display.update();
		}
		
		this.textureManager.dispose();
	}

	public TextureManager getTextureManager() {
		return this.textureManager;
	}
	
	public FontManager getFontManager() {
		return this.fontManager;
	}
	
	public static void initDisplay() {
		try {
			DisplayMode d = new DisplayMode(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
			Display.setDisplayMode(d);
			Display.setTitle(Game.GAME_NAME);
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
