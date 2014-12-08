package cz.dat.geometrytd.world;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.lwjgl.opengl.Display;

public class LevelParser {
	InputStream stream;

	private Polygon path;
	private Rectangle start;
	private Rectangle end;
	
	ArrayList<Point> points1;
	ArrayList<Point> points2;

	public LevelParser(InputStream file) {
		this.stream = file;
	}

	public void parse() {
		this.path = new Polygon();
		this.start = new Rectangle();
		this.end = new Rectangle();

		float widthRatio = Display.getWidth() / 961f;
		float heightRatio = Display.getHeight() / 578f;

		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					this.stream));

			String line;
			int part = 0;
			points1 = new ArrayList<Point>();
			points2 = new ArrayList<Point>();
			while ((line = r.readLine()) != null) {
				if (line.startsWith("DEF")) {
					part++;
					continue;
				}

				switch (part) {
				case 0:
					String[] parts = line.split(";");
					this.path.addPoint(
							(int) (Integer.parseInt(parts[0]) * widthRatio),
							(int) (Integer.parseInt(parts[1]) * heightRatio));
					break;
				case 1:
					parts = line.split(";");
					this.start.setLocation(
							(int) (Integer.parseInt(parts[0]) * widthRatio),
							(int) (Integer.parseInt(parts[1]) * heightRatio));
					break;
				case 2:
					parts = line.split(";");
					this.start.setSize(
							(int) (Integer.parseInt(parts[0]) * widthRatio),
							(int) (Integer.parseInt(parts[1]) * heightRatio));
					break;
				case 3:
					parts = line.split(";");
					this.end.setLocation(
							(int) (Integer.parseInt(parts[0]) * widthRatio),
							(int) (Integer.parseInt(parts[1]) * heightRatio));
					break;
				case 4:
					parts = line.split(";");
					this.end.setSize(
							(int) (Integer.parseInt(parts[0]) * widthRatio),
							(int) (Integer.parseInt(parts[1]) * heightRatio));
					break;
				case 5:
					parts = line.split("!");
					for (int i = 0; i < 2; i++) {
						String[] parts2 = parts[i].split(";");
						(i == 0 ? points1 : points2).add(new Point(
								(int) (Integer.parseInt(parts2[0]) * widthRatio),
								(int) (Integer.parseInt(parts2[1]) * heightRatio)));
					}
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
	
	public Rectangle getStart() {
		return this.start;
	}
	
	public Rectangle getEnd() {
		return this.end;
	}
	
	public Point[] getPoints(boolean second) {
		return (second ? this.points2 : this.points1).toArray(new Point[1]);
	}

}
