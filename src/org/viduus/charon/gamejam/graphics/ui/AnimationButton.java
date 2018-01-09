/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 8, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.util.Size;

/**
 * 
 *
 * @author Ethan Toney
 */
public class AnimationButton extends UIElement {

	private final String animation_uid;
	private Animation<?> animation_on, animation_off;
	
	public AnimationButton(String animation_uid) {
		this.animation_uid = animation_uid;
	}
	
	@Override
	protected void updateSize() {
		Size curr_size = getActiveAnimation().getCurrentFrame().getBoundingSize();
		setSize((int) curr_size.width, (int) curr_size.height);
	}
	
	private Animation<?> getActiveAnimation() {
		return hasFocus() ? animation_on : animation_off;
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		updateSize();
		getActiveAnimation().renderAnimation(graphics, d_sec, getX() + getWidth()/2, getY() + getHeight()/2, 1);
		updateSize();
	}
	
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		animation_on = (Animation<?>) game_systems.graphics_engine.resolve(animation_uid + "_on");
		animation_off = (Animation<?>) game_systems.graphics_engine.resolve(animation_uid + "_off");
		updateSize();
	}

	@Override
	public void onDeactivate(AbstractGameSystems game_systems) {
		game_systems.graphics_engine.release(animation_on);
		game_systems.graphics_engine.release(animation_off);
	}
	
}
