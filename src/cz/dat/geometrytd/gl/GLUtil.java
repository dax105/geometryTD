package cz.dat.geometrytd.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.manager.Texture;
import cz.dat.geometrytd.manager.TextureManager;

public class GLUtil {

	public static void drawTexture(TextureManager t, int texture,
			float textureX1, float textureX2, float textureY1, float textureY2,
			float x1, float x2, float y1, float y2) {
		t.bind(texture);
		GL11.glColor4f(1f, 1f, 1f, 1f);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(textureX1, textureY1);
		GL11.glVertex2f(x1, y1);

		GL11.glTexCoord2f(textureX2, textureY1);
		GL11.glVertex2f(x2, y1);

		GL11.glTexCoord2f(textureX2, textureY2);
		GL11.glVertex2f(x2, y2);

		GL11.glTexCoord2f(textureX1, textureY2);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}
	
	public static void drawTextureColored(TextureManager t, int texture,
			float textureX1, float textureX2, float textureY1, float textureY2,
			float x1, float x2, float y1, float y2) {
		t.bind(texture);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(textureX1, textureY1);
		GL11.glVertex2f(x1, y1);

		GL11.glTexCoord2f(textureX2, textureY1);
		GL11.glVertex2f(x2, y1);

		GL11.glTexCoord2f(textureX2, textureY2);
		GL11.glVertex2f(x2, y2);

		GL11.glTexCoord2f(textureX1, textureY2);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();
	}

	public static void drawTexture(TextureManager t, int texture, float x,
			float y) {
		Texture tex = t.getTexture(texture);
		GLUtil.drawTexture(t, texture, 0f, 1f, 0f, 1f, x,
				x + tex.Size.getWidth(), y, y + tex.Size.getHeight());
	}

	public static void drawAtlasTexture(TextureManager t, int texture,
			int image, float x1, float x2, float y1, float y2) {
		Texture tex = t.getTexture(texture);
		GLUtil.drawTexture(t, texture, tex.getX1(image), tex.getX2(image), 0,
				1, x1, x2, y1, y2);
	}

	public static void drawAtlasTexture(TextureManager t, int texture,
			int image, float x, float y) {
		Vector2f size = t.getTexture(texture).SheetSize;
		GLUtil.drawAtlasTexture(t, texture, image, x, x + size.x, y, y + size.y);
	}

	public static void drawLine(float x1, float x2, float y1, float y2,
			int thickness, float r, float g, float b, float a) {
		GL11.glColor4f(r, g, b, a);
		GL11.glLineWidth(thickness);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);

		GL11.glEnd();
	}

	public static void drawRectangle(float r, float g, float b, float a,
			float x1, float x2, float y1, float y2) {
		GL11.glColor4f(r, g, b, a);

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(x1, y1);

		GL11.glVertex2f(x2, y1);

		GL11.glVertex2f(x2, y2);

		GL11.glVertex2f(x1, y2);

		GL11.glEnd();
	}

	public static void drawRectangle(float r, float g, float b, float x1,
			float x2, float y1, float y2) {
		GLUtil.drawRectangle(r, g, b, 1, x1, x2, y1, y2);
	}

	public static void drawRectangle(Color color, float x1, float x2, float y1,
			float y2) {
		GLUtil.drawRectangle(color.r, color.g, color.b, color.a, x1, x2, y1, y2);
	}
	
}
