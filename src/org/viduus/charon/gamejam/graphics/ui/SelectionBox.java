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
import org.viduus.charon.global.graphics.util.Size;

/**
 * 
 *
 * @author Ethan Toney
 */
public class SelectionBox extends UIElement {

	private final String
		animation_uid,
		displayed_text;
	private Animation<?> animation_on, animation_off;
	private boolean enabled;
	private Size image_size;
	
	public SelectionBox(String animation_uid, String displayed_text) {
		this.animation_uid = animation_uid;
		this.displayed_text = displayed_text;
		image_size = new Size();
		enabled = false;
	}
	
	/**
	 * @param animation_uid
	 */
	public SelectionBox(String animation_uid) {
		this(animation_uid, "");
	}

	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public boolean enabled() {
		return enabled;
	}
	
	private Animation<?> getActiveAnimation() {
		return enabled() ? animation_on : animation_off;
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		updateSize();
		getActiveAnimation().renderAnimation(graphics, d_sec, getX(), getY(), 1);
		updateSize();
		OpenGLFont.drawString2D(graphics, displayed_text, getX(), getY() + OpenGLFont.getLineHeight());
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
	
	public int getImageWidth() {
		return (int) image_size.width;
	}
	
	public int getImageHeight() {
		return (int) image_size.height;
	}
	
	private void setImageSize(int width, int height) {
		image_size.width = width;
		image_size.height = height;
	}
	
	@Override
	protected void updateSize() {
		Size curr_size = getActiveAnimation().getCurrentFrame().getBoundingSize();
		setImageSize((int) curr_size.width, (int) curr_size.height);
		setSize((int) (curr_size.width + OpenGLFont.getStringWidth(displayed_text)), (int) curr_size.height);
	}
	
}
