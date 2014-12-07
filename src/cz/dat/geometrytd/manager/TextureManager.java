package cz.dat.geometrytd.manager;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

import cz.dat.geometrytd.Game;

public class TextureManager {
	private Map<Integer, Texture> textures;
	private String texturesDir = Game.RES_DIR + "textures";
	private int lastBindID = -1;
	
	public TextureManager() {
		this.textures = new HashMap<Integer, Texture>();
	}
	
	public void loadTextures() {
		
	}
	
	public Texture getTexture(int id) {
		return this.textures.get(id);
	}
	
	public void setSpritesheet(int id, Vector2f size) {
		
	}
	
	public void addTexture(int id, String file) {
		Rectangle r;
		int tID;
	}
	
	
}
