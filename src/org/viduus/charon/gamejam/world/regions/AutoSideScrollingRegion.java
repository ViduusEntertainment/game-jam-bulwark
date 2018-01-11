/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.regions;

import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.graphics.SideScrollingBackground;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.regions.BaseRegion;

/**
 * 
 *
 * @author Ethan Toney
 */
public abstract class AutoSideScrollingRegion extends BaseRegion {

	SideScrollingBackground background;

	/**
	 * @param world_engine
	 * @param name
	 * @param size
	 */
	public AutoSideScrollingRegion(AbstractWorldEngine world_engine, String name, Size size, Vector2 location) {
		super(world_engine, name, size, location);
		background = new SideScrollingBackground();
	}
	
	protected void addBackgroundSet(float dy, int speed, Animation<?>[] animations) {
		background.addBackgroundSet(dy, speed, animations);
	}
	
	public void setBackgroundSpeedMod(float mod) {
		background.setBackgroundSpeedMod(mod);
	}

	@Override
	public void render(OpenGLGraphics graphics, float d_sec, AABB world_bounding_box) {
		background.render(graphics, d_sec);
		super.render(graphics, d_sec, world_bounding_box);
	}

}
