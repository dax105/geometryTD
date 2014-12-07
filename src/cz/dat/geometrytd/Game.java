package cz.dat.geometrytd;

import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.gl.FramebufferObject;
import cz.dat.geometrytd.gl.ShaderProgram;
import cz.dat.geometrytd.gl.Texture2D;
import cz.dat.geometrytd.manager.FontManager;
import cz.dat.geometrytd.manager.TextureManager;

public class Game implements Runnable {

	public static final int WINDOW_WIDTH = 854;
	public static final int WINDOW_HEIGHT = 480;
	public static final String GAME_NAME = "Geometry TD";
	public static final String RES_DIR = "/cz/dat/geometrytd/resources/";
	public static final Logger log = Logger.getLogger("GeometryTD_log");
	
	private static final int TPS = 20;
	private static final double TICK_TIME = 1.0D / TPS;
	private static final int MAGIC_CONSTANT = 1000000000;
	
	private TextureManager textureManager;
	private FontManager fontManager;
	
	private FramebufferObject[] frameBuffers = new FramebufferObject[4];
	private Texture2D[] fboTextures = new Texture2D[4];
	
	private ShaderProgram verticalBlur;

	private void tick(int ticks) {

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
		
		int tex = 1;
		
		float x0 = textureManager.getTexture(1).getX1(tex);
		float x1 = textureManager.getTexture(1).getX2(tex);
		float y0 = 0;
		float y1 = 1;
		
		this.textureManager.bind(tex);
		
		GL11.glColor3f(1, 0, 1);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(x0, y0);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(x1, y0);
		GL11.glVertex2f(256, 0);
		GL11.glTexCoord2f(x1, y1);
		GL11.glVertex2f(256, 256);
		GL11.glTexCoord2f(x0, y1);
		GL11.glVertex2f(0, 256);
		GL11.glEnd();
		
		this.textureManager.clearBind();
		
		fontManager.drawString("helhjghjhgjhgjghjhglo", 200, 200, Color.blue);
		
		/*
		 * GLOW
		 */
		
		frameBuffers[1].checkBind();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		bindTex(fboTextures[0].getId());
		GL20.glUseProgram(verticalBlur.getProgramID());
		drawFullscreenQuad();
		
		/*
		 * COMPOSE
		 */
		
		FramebufferObject.bindNone();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL20.glUseProgram(0);
		
		bindTex(fboTextures[1].getId());
		drawFullscreenQuad();
	}
	
	private void drawFullscreenQuad() {
		GL11.glColor3f(1, 1, 1);
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
		
		this.verticalBlur = new ShaderProgram("cz/dat/geometrytd/gl/shaders/glow");
		
		for (int i = 0; i < this.frameBuffers.length; i++) {
			FramebufferObject f = new FramebufferObject(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
			Texture2D t = new Texture2D(f, GL11.GL_RGB8, GL11.GL_RGB, GL11.GL_INT);
			f.attachTexture(t, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
			frameBuffers[i] = f;
			fboTextures[i] = t;
		}

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
		this.fontManager.dispose();
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
