package cz.dat.geometrytd.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cz.dat.geometrytd.Game;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureManager {
	private Map<Integer, Texture> textures;
	private String texturesDir = Game.RES_DIR + "textures/";
	private int lastBindID = -1;

	public TextureManager() {
		this.textures = new HashMap<Integer, Texture>();
		this.loadTextures();
	}

	public void loadTextures() {
		this.addTexture(1, "all.png");
		this.setSpritesheet(1, new Vector2f(64, 64));
		
		this.addTexture(2, "back.png");
		this.addTexture(3, "l1.png");
		this.addTexture(666, "il1.png");
		this.addTexture(6927, "il2.png");
		this.addTexture(100, "clouds.png");
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);
		
		
	}

	public Texture getTexture(int id) {
		return this.textures.get(id);
	}

	public void setSpritesheet(int id, Vector2f size) {
		if (this.textures.containsKey(id)) {
			this.textures.get(id).SheetSize = size;
		} else {
			Game.log.warning("Trying to set sheet size on texture that doesn't exist");
		}
	}

	public void addTexture(int id, String fileName) {
		Texture t;

		try {
			t = loadTexture(new File(this.getClass()
					.getResource(this.texturesDir + fileName).toURI()));

			if (this.textures.containsKey(id))
				Game.log.warning("Rewriting texture ID");

			this.textures.put(id, t);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}

	}

	public void bind(int id, boolean force) {
		if (this.textures.containsKey(id)) {
			int tID = this.textures.get(id).TextureID;
			if ((tID != this.lastBindID) || force) {
				this.lastBindID = tID;
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, tID);
			}
		}
	}

	public void bind(int id) {
		this.bind(id, false);
	}

	public void clearBind() {
		this.lastBindID = -1;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void dispose() {
		this.clearBind();

		for (Texture t : this.textures.values()) {
			GL11.glDeleteTextures(t.TextureID);
		}
	}

	private Texture loadTexture(File file) throws IOException,
			IllegalArgumentException {
		if (!file.exists())
			throw new IllegalArgumentException(file.getAbsolutePath());

		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;

		InputStream in = new FileInputStream(file);
		PNGDecoder decoder = new PNGDecoder(in);

		tWidth = decoder.getWidth();
		tHeight = decoder.getHeight();

		buf = ByteBuffer.allocateDirect(4 * decoder.getWidth()
				* decoder.getHeight());
		decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
		buf.flip();

		in.close();

		int id = 0;

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight,
				0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL12.GL_CLAMP_TO_EDGE);

		Texture ret = new Texture();
		ret.TextureID = id;
		ret.Size = new Rectangle(0, 0, tWidth, tHeight);

		return ret;
	}
}
