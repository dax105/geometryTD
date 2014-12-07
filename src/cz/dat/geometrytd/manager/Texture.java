package cz.dat.geometrytd.manager;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

public class Texture {
	public int TextureID;
	public Rectangle Size;
	public Vector2f SheetSize;
	
	public float getX1(int image) {
		float pos = (this.SheetSize.x * (image - 1));
		return pos / this.Size.getWidth();
	}
	
	public float getX2(int image) {
		float pos = (this.SheetSize.x * image);
		return pos / this.Size.getWidth();
	}
}
