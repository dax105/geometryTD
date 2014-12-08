package cz.dat.geometrytd;

import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

import cz.dat.geometrytd.gl.FramebufferObject;
import cz.dat.geometrytd.gl.ShaderProgram;
import cz.dat.geometrytd.gl.Texture2D;
import cz.dat.geometrytd.manager.FontManager;
import cz.dat.geometrytd.manager.TextureManager;
import cz.dat.geometrytd.manager.sound.SoundManager;
import cz.dat.geometrytd.world.World;

public class Game implements Runnable {

	public static final int WINDOW_WIDTH = 960;
	public static final int WINDOW_HEIGHT = 576;
	public static final String GAME_NAME = "Geometry TD";
	public static final String RES_DIR = "/cz/dat/geometrytd/resources/";
	public static final Logger log = Logger.getLogger("GeometryTD_log");
	
	private static final int TPS = 20;
	private static final double TICK_TIME = 1.0D / TPS;
	private static final int MAGIC_CONSTANT = 1000000000;
	
	private TextureManager textureManager;
	private FontManager fontManager;
	private SoundManager soundManager;
	
	private World world;
	
	public World getWorld() {
		return world;
	}

	private FramebufferObject[] frameBuffers = new FramebufferObject[2];
	private Texture2D[] fboTextures = new Texture2D[2];
	
	private ShaderProgram verticalBlur;

	private void tick(int ticks) {
		this.world.onTick();
	}

	private void renderTick(float ptt) {
		/*
		 * DRAW BASE
		 */
		
		frameBuffers[0].checkBind();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
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
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		/*
		 * GLOW
		 */
		
		frameBuffers[1].checkBind();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		bindTex(fboTextures[0].getId());
		GL20.glUseProgram(verticalBlur.getProgramID());
		drawHalfFullscreenQuad();
		
		/*
		 * COMPOSE
		 */
		
		FramebufferObject.bindNone();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL20.glUseProgram(0);
		
		GL11.glColor4f(1, 1, 1, 1);
		bindTex(fboTextures[0].getId());
		drawFullscreenQuad();
		
		GL11.glColor4f(1, 1, 1, 0.4f);
		bindTex(fboTextures[1].getId());
		drawFullscreenQuad();
	}
	
	private void drawFullscreenQuad() {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(Game.WINDOW_WIDTH, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, Game.WINDOW_HEIGHT);
		GL11.glEnd();
	}
	
	private void drawHalfFullscreenQuad() {
		int halfh = WINDOW_HEIGHT-WINDOW_HEIGHT/4;
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, halfh);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(Game.WINDOW_WIDTH/4, halfh);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(Game.WINDOW_WIDTH/4, Game.WINDOW_HEIGHT);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, Game.WINDOW_HEIGHT);
		GL11.glEnd();
	}

	@Override
	public void run() {
		Game.initDisplay();
		
		this.textureManager = new TextureManager();
		this.fontManager = new FontManager();
		this.soundManager = new SoundManager();
		this.soundManager.updateVolume(true, 0.6f);
		
		long time = System.nanoTime();
		long lastTime = time;
		long lastInfo = time;

		int fps = 0;
		int ticks = 0;
		int lastTicks = 0;
		
		this.verticalBlur = new ShaderProgram("cz/dat/geometrytd/gl/shaders/glow");
		
		for (int i = 0; i < this.frameBuffers.length; i++) {
			FramebufferObject f; 
			if (i != 1) { 
				f = new FramebufferObject(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
			} else {
				f = new FramebufferObject(Game.WINDOW_WIDTH/4, Game.WINDOW_HEIGHT/4);
			}
			Texture2D t = new Texture2D(f, GL11.GL_RGB8, GL11.GL_RGB, GL11.GL_INT);
			f.attachTexture(t, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
			frameBuffers[i] = f;
			fboTextures[i] = t;
		}
		
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
		
		this.exit();
	}
	
	public void exit() {
		this.textureManager.dispose();
		this.soundManager.shutdown();
		System.exit(0);
	}

	public void bindTex(int id) {
		this.textureManager.clearBind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public TextureManager getTextureManager() {
		return this.textureManager;
	}
	
	public FontManager getFontManager() {
		return this.fontManager;
	}
	
	public SoundManager getSoundManager() {
		return this.soundManager;
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
