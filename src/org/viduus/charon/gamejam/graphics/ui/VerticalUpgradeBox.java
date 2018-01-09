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
	
	private String
		upgrade_text,
		animation_uid;
	private int
		level = 0,
		max_level;
	private Animation<?>
		active_animation_on,
		active_animation_off,
		animation_on,
		animation_off,
		money;
	private int[] upgrade_cost;

	public VerticalUpgradeBox(String animation_uid, String upgrade_text, int[] upgrade_cost) {
		this.animation_uid = animation_uid;
		this.upgrade_text = upgrade_text;
		this.upgrade_cost = upgrade_cost;
		max_level = upgrade_cost.length;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	private Animation<?> getActiveAnimation() {
//		return hasFocus() ? active_animation_on : active_animation_off;r
		return active_animation_on;
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
		for (; i<max_level ; i++) {
			animation_off.renderAnimation(graphics, d_sec, getX()+9, getY() - 16 - i*dr + 9, 1);
		}
		i=5;
		int bottom = getY() - 16 - i*dr + 9;

		OpenGLFont.drawString2D(graphics, upgrade_text, (int) (getX() + 9 - OpenGLFont.getStringWidth(upgrade_text)/2), bottom);
		
		if (hasFocus()) {
			int width = 55;
			int height = 110;
			int left = getX() + (getWidth() - width)/2;
			int top = getY() + (getHeight() - height) + 2;
			
			renderColoredSquare(graphics, left+1, top+1, width-1, height-1, 0.1333333333f, 0.1254901961f, 0.2039215686f, 0.7f);
			renderSelect(graphics, d_sec, left, top, width, height);

			renderTextAndAnimationScreen(graphics, d_sec, left, top, width, height, "Upgrade", "x", upgrade_cost[level], money);
		}
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
		money = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.money");
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
		game_systems.graphics_engine.release(money);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#updateSize()
	 */
	@Override
	protected void updateSize() {
		setSize(18, 18);
	}
	
}
