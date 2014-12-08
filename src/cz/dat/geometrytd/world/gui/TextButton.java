package cz.dat.geometrytd.world.gui;

import java.awt.Rectangle;

import org.newdawn.slick.Color;

import cz.dat.geometrytd.gl.GLUtil;
import cz.dat.geometrytd.manager.FontManager;
import cz.dat.geometrytd.world.World;

public class TextButton extends Button {
	int border = 3;

	private FontManager f;
	private String text;

	public TextButton(int x, int y, String text, World w) {
		super(new Rectangle(x, y, w.getGame().getFontManager().getFont()
				.getWidth(text) + 6, w.getGame().getFontManager().getFont()
				.getHeight(text) + 6), w);
		this.f = w.getGame().getFontManager();
		this.text = text;
	}

	@Override
	public void onPress() {

	}

	@Override
	public void render() {
		if (super.onDown) {
			GLUtil.drawRectangle(1, 1, 1, 0.3f, super.r.x, super.r.x
					+ super.r.width, super.r.y, super.r.y + super.r.height);
		}

		GLUtil.drawLine(super.r.x, super.r.x + super.r.width, super.r.y,
				super.r.y, this.border, 1, 1, 1, 0.9f);
		GLUtil.drawLine(super.r.x, super.r.x + super.r.width, super.r.y
				+ super.r.height, super.r.y + super.r.height, this.border, 1,
				1, 1, 0.9f);
		GLUtil.drawLine(super.r.x, super.r.x, super.r.y, super.r.y
				+ super.r.height, this.border, 1, 1, 1, 0.9f);
		GLUtil.drawLine(super.r.x + super.r.width, super.r.x + super.r.width,
				super.r.y, super.r.y + super.r.height, this.border, 1, 1, 1,
				0.9f);

		this.w.getGame().getTextureManager().clearBind();
		this.f.drawString(this.text, this.r.x + 3, this.r.y + 3, Color.white);
		this.w.getGame().getTextureManager().bind(1);
	}

}
