/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.regions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.dyn4j.geometry.AABB;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.regions.BaseRegion;

/**
 * 
 *
 * @author Ethan Toney
 */
public abstract class SideScrollingRegion extends BaseRegion {

	private static final float SIDE_SCROLLING_SPEED = 30; // pixel/sec
	private static final Random RN_JESUS = new Random();
	
	private float dx = 0;
	private Animation<?>[] backgrounds;
	private List<Animation<?>> rendered_backgrounds = new LinkedList<>();

	/**
	 * @param world_engine
	 * @param name
	 * @param size
	 */
	public SideScrollingRegion(AbstractWorldEngine world_engine, String name, Size size) {
		super(world_engine, name, size);
		// TODO Auto-generated constructor stub
	}
	
	protected void setBackgrounds(Animation<?>[] animations) {
		backgrounds = animations;
	}

	@Override
	public void render(OpenGLGraphics graphics, float d_sec, AABB world_bounding_box) {
		dx += SIDE_SCROLLING_SPEED * d_sec;
		float screen_width = (float) graphics.getCanvasDimension().width;
		float consumed_width = 0;
		
		// Render current backgrounds
		for (Iterator<Animation<?>> it = rendered_backgrounds.iterator(); it.hasNext(); ) {
			Animation<?> bg = it.next();
			bg.renderAnimation(graphics, d_sec, (int) (consumed_width-dx), 0, 1);
			float background_width = bg.getCurrentFrame().getBoundingSize().width;
			consumed_width += background_width;
			if (consumed_width < dx) {
				consumed_width -= background_width;
				dx -= background_width;
				it.remove();
			}
		}
		
		// Add new backgrounds if necessary
		while (consumed_width <= dx + screen_width) {
			Animation<?> bg = backgrounds[RN_JESUS.nextInt(backgrounds.length)];
			rendered_backgrounds.add(bg);
			bg.renderAnimation(graphics, d_sec, (int) (consumed_width-dx), 0, 1);
			consumed_width += bg.getCurrentFrame().getBoundingSize().width;
		}
		
		OutputHandler.println(""+rendered_backgrounds.size());
		
		super.render(graphics, d_sec, world_bounding_box);
	}

}
