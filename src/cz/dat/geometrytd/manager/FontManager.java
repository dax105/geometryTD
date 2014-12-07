package cz.dat.geometrytd.manager;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import cz.dat.geometrytd.Game;

public class FontManager {
	public static final int BIG = 48;
	public static final int SMALL = 32;
	
	private TrueTypeFont defaultFont;
	private Map<Integer, TrueTypeFont> fonts;
	
	public FontManager() {
		this.fonts = new HashMap<>();
		this.loadDefaults();
	}
	
	public void drawString(String text, int x, int y, int size, Color color) {
		TextureImpl.bindNone();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.getFont(size).drawString(x, y, text, color);
	}

	public void drawString(String text, int x, int y, Color color) {
		TextureImpl.bindNone();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.defaultFont.drawString(x, y, text, color);
	}
	
	public void loadDefaults() {
		this.getFont(FontManager.BIG);
		this.defaultFont = this.getFont(FontManager.SMALL);
	}
	
	public TrueTypeFont getFont() {
		return this.defaultFont;
	}
	
	public TrueTypeFont getFont(int size) {
		if(this.fonts.containsKey(size)) {
			return this.fonts.get(size);
		} else {
			TrueTypeFont f = this.loadFont(this.getClass().getResourceAsStream(Game.RES_DIR + "thin_pixel-7.ttf"), Font.PLAIN, size, true);
			this.fonts.put(size, f);
			return f;
		}	
	}
	
	private TrueTypeFont loadFont(InputStream in, int style, int size,
			boolean antiAlias) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(style, size);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return this.loadFont(font, antiAlias);
	}
	
	private TrueTypeFont loadFont(Font font, boolean antiAlias) {
		return new TrueTypeFont(font, antiAlias);
	}
	
	public void dispose() {
		for(TrueTypeFont f : this.fonts.values()) {
			
		}
	}
}
