package cz.dat.geometrytd.world;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import cz.dat.geometrytd.Game;
import cz.dat.geometrytd.TickListener;
import cz.dat.geometrytd.gl.GLUtil;
import cz.dat.geometrytd.manager.FontManager;

public class World extends TickListener {

	private Level currentLevel;
	
	private int currentTowerSelected = 1;
	private int towers = 4;
	private String[] towerNames = new String[] { "Snower", "Smacker", "Shocker", "Slower" };
	private int boxY, titleY, towerNameFontY, bigFontHeight, towerWidthHalf;
	private float fogOffset = 0;
	
	private List<Button> buttons = new ArrayList<Button>();
	
	public World(Game game) {
		super(game);
		
		this.bigFontHeight = super.game.getFontManager().getFont(FontManager.BIG).getHeight();
		this.boxY = 20;
		this.titleY = this.boxY - (this.bigFontHeight / 4);
		this.towerNameFontY = this.titleY + this.bigFontHeight
				+ super.game.getFontManager().getFont().getHeight() / 2;
		this.towerWidthHalf = (int)super.game.getTextureManager().getTexture(1).SheetSize.x / 2;
		
		this.changeLevel(new Level(game, 3, 
				new LevelParser(this.getClass().getResourceAsStream(Game.RES_DIR + "levels/l1.txt"))));
		
		
		// LITTLE COPY-PASTING NEVER KILLED NOBODY!
		// Copy as yer wish is ma frend...
		buttons.add(new Button(new Rectangle(0, 0, 64, 64), this ) {		
			int border = 5;
			
			@Override
			public void onPress() {
				// Some action, probably on w.
				System.out.println("Pressed button!");
			}

			@Override
			public void render() {
				if (super.onDown) {
				GLUtil.drawRectangle(1, 1, 1, 0.3f, super.r.x,
						super.r.x+super.r.width, super.r.y, super.r.y+super.r.height);
				}
				
				GLUtil.drawAtlasTexture(w.getGame().getTextureManager(), 1, 5, super.r.x,
						super.r.x+super.r.width, super.r.y, super.r.y+super.r.height);
				
				GLUtil.drawAtlasTexture(w.getGame().getTextureManager(), 1, 1, super.r.x + border,
						super.r.x+super.r.width - border, super.r.y + border, super.r.y+super.r.height - border);
			}
		});
		
	}
	
	
	private void changeLevel(Level newLevel) {
		this.children.remove(this.currentLevel);
		this.currentLevel = newLevel;
		this.children.add(this.currentLevel);
	}
	
	@Override
	protected void tick() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				int k = Keyboard.getEventKey();
				
				if(k == Keyboard.KEY_LEFT) {
					this.currentTowerSelected--;
				}
				
				if(k == Keyboard.KEY_RIGHT) {
					this.currentTowerSelected++;
				}
				
				if(this.currentTowerSelected > this.towers)
					this.currentTowerSelected = 1;
				
				if(this.currentTowerSelected < 1)
					this.currentTowerSelected = this.towers;
			}
		}
		
		while(Mouse.next()) {
			if(Mouse.getEventButtonState()) {
				if(Mouse.getEventButton() == 0) {
					Tower t = new TowerSnower(super.game);
					t.getRectangle().setLocation(Mouse.getX() - this.towerWidthHalf,
							Display.getHeight() - Mouse.getY() -  this.towerWidthHalf);
					this.currentLevel.addTower(t);
				}
			}
			if(Mouse.getEventButton() == 0) {
				for (Button b : buttons) {
					if (Mouse.getEventButtonState()) {
						b.onDown(new Point(Mouse.getX(), Game.WINDOW_HEIGHT-Mouse.getY()));
					} else {
						b.onUp(new Point(Mouse.getX(), Game.WINDOW_HEIGHT-Mouse.getY()));
					}
				}
			}
		}
		
		fogOffset += 0.001f;
		
		while (fogOffset > 2f) {
			fogOffset -= 2f;
		}
	}

	@Override
	protected void renderTick(float ptt) {
		GLUtil.drawTexture(game.getTextureManager(), 2, 0f, 1f, 0f, 1f, 0, Display.getWidth(),
				0, Display.getHeight());
		
		GL11.glColor4f(0.0f, 0.8f, 1.0f, 0.75f);
		GLUtil.drawTextureColored(game.getTextureManager(), 100, 0+fogOffset, 1+fogOffset, 0+fogOffset, 1*(Game.WINDOW_HEIGHT/(float)Game.WINDOW_WIDTH)+fogOffset, 0, Game.WINDOW_WIDTH, 0, Game.WINDOW_HEIGHT);
		
		GLUtil.drawRectangle(0.5f, 0.5f, 0.5f, 0.75f, Display.getWidth() - 260, Display.getWidth() - 20, 
				this.boxY, 300);
		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, this.currentTowerSelected, Display.getWidth() - 250,
				this.titleY + this.bigFontHeight);
		
		for (Button b : buttons) {
			b.render();
		}
		
		super.game.getFontManager().drawString("Current tower", Display.getWidth() - 250, this.titleY,
				FontManager.BIG, Color.white);
		super.game.getFontManager().drawString(this.towerNames[this.currentTowerSelected - 1], Display.getWidth() - 250 + 74,
				this.towerNameFontY, Color.white);
		
		
	}
	
	@Override
	public void onRenderTick(float ptt) {
		super.onRenderTick(ptt);
		
		
		GLUtil.drawAtlasTexture(super.game.getTextureManager(), 1, 
				this.currentTowerSelected, Mouse.getX() - this.towerWidthHalf, 
				Display.getHeight() - Mouse.getY() - this.towerWidthHalf);
	}
	
	abstract class Button{
		private Rectangle r;
		protected World w;
		
		private boolean onDown = false;
		
		public Button(Rectangle r, World w) {
			this.r = r;
			this.w = w;
		}
		
		public void onDown(Point p) {
			if (r.contains(p)) {
				onDown = true;
			}
		}
		
		public void onUp(Point p) {
			if (onDown && r.contains(p)) {
				onPress();
			}	
			onDown = false;
		}
		
		public abstract void onPress();
		
		public abstract void render();
	}
	
}
