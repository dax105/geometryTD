package cz.dat.geometrytd.world;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.Display;

public class LevelParser {
	InputStream stream;

	private Polygon path;
	private Rectangle[] starts;
	private Rectangle[] ends;

	public LevelParser(InputStream file) {
		this.stream = file;
	}

	public void parse() {
		this.path = new Polygon();
		float widthRatio = Display.getWidth() / 1280f;
		float heightRatio = Display.getHeight() / 720f;

		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					this.stream));

			String line;
			int part = 0;
			while ((line = r.readLine()) != null) {
				if (line.equalsIgnoreCase("DEF")) {
					part++;
					continue;
				}

				switch (part) {
				case 0:
					String[] parts = line.split(";");
					this.path.addPoint((int)(Integer.parseInt(parts[0]) * widthRatio), 
							(int)(Integer.parseInt(parts[1]) * heightRatio));
					break;
				}
			}

			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Polygon getPathPolygon() {
		return this.path;
	}

}
