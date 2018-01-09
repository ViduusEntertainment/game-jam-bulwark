/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 8, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;

/**
 * 
 *
 * @author Ethan Toney
 */
public class VerticalUpgradeBox extends UIElement {

	private static final int MAX_LEVEL = 5;
	
	private String
		upgrade_text,
		animation_uid;
	private int level = 0;
	private Animation<?>
		active_animation_on,
		active_animation_off,
		animation_on,
		animation_off;

	public VerticalUpgradeBox(String animation_uid, String upgrade_text) {
		this.animation_uid = animation_uid;
		this.upgrade_text = upgrade_text;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	private Animation<?> getActiveAnimation() {
		return hasFocus() ? active_animation_on : active_animation_off;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#render(org.viduus.charon.global.graphics.opengl.OpenGLGraphics, float)
	 */
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		getActiveAnimation().renderAnimation(graphics, d_sec, getX() + 9, getY() + 9, 1);
		
		int dr = 14;
		int i=0;
		for (; i<level ; i++) {
			animation_on.renderAnimation(graphics, d_sec, getX()+9, getY() - 16 - i*dr + 9, 1);
		}
		for (; i<MAX_LEVEL ; i++) {
			animation_off.renderAnimation(graphics, d_sec, getX()+9, getY() - 16 - i*dr + 9, 1);
		}
		int bottom = getY() - 16 - i*dr + 9;

		OpenGLFont.drawString2D(graphics, upgrade_text, (int) (getX() + 9 - OpenGLFont.getStringWidth(upgrade_text)/2), bottom);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		active_animation_on = (Animation<?>) game_systems.graphics_engine.resolve(animation_uid + "_on");
		active_animation_off = (Animation<?>) game_systems.graphics_engine.resolve(animation_uid + "_off");
		animation_on = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.select_on");
		animation_off = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.select_off");
		updateSize();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onDeactivate(AbstractGameSystems game_systems) {
		game_systems.graphics_engine.release(active_animation_on);
		game_systems.graphics_engine.release(active_animation_off);
		game_systems.graphics_engine.release(animation_on);
		game_systems.graphics_engine.release(animation_off);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#updateSize()
	 */
	@Override
	protected void updateSize() {
		setSize(18, 18);
	}
	
}
