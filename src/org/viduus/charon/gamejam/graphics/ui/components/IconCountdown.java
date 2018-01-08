/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui.components;

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
public class IconCountdown {
	
	private final Animation<?>
		active_icon,
		inactive_icon;
	private float count;
	
	public IconCountdown(AbstractGameSystems game_systems, Uid active_icon_uid, Uid inactive_icon_uid, int count) {
		this.count = count;
		active_icon = (Animation<?>) game_systems.graphics_engine.resolve(active_icon_uid);
		inactive_icon = (Animation<?>) game_systems.graphics_engine.resolve(inactive_icon_uid);
	}
	
	public void setCount(float count) {
		this.count = count;
	}
	
	public void render(OpenGLGraphics graphics, int x, int y, float d_sec) {
		if (count > 0) {
			inactive_icon.renderAnimation(graphics, d_sec, x, y, 2);
			
			OpenGLFont.setFont(graphics, "pixel_font");
			OpenGLFont.setFontColor(1, 1, 1, 1);
			String text = String.format("%.1f", count);
			OpenGLFont.drawString2D(graphics, text, (int) (x+(32-OpenGLFont.getStringWidth(text))/2), y+OpenGLFont.getLineHeight()+5);
		} else {
			active_icon.renderAnimation(graphics, d_sec, x, y, 2);
		}
	}
}
