/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;
import org.viduus.charon.global.util.identification.Uid;

/**
 * 
 *
 * @author Ethan Toney
 */
public class IconCounter {
	
	private final Animation<?> icon;
	private int count;
	
	public IconCounter(AbstractGameSystems game_systems, Uid icon_uid, int count) {
		this.count = count;
		icon = (Animation<?>) game_systems.graphics_engine.resolve(icon_uid);
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void render(OpenGLGraphics graphics, int x, int y, float d_sec) {
		icon.renderAnimation(graphics, d_sec, x, y, 1);

		OpenGLFont.setFont(graphics, "pixel_font");
		OpenGLFont.setFontColor(1, 1, 1, 1);
		OpenGLFont.drawString2D(graphics, "x"+count, (int) (x+icon.getCurrentFrame().getBoundingSize().width+2), y+OpenGLFont.getLineHeight());
	}
	
}
