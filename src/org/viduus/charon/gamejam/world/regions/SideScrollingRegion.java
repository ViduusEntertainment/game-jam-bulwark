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
import org.viduus.charon.global.graphics.opengl.shaders.ShaderProgram;
import org.viduus.charon.global.graphics.opengl.shaders.variables.ShaderAttribute;
import org.viduus.charon.global.graphics.opengl.shapes.Rectangle;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.regions.BaseRegion;

/**
 * 
 *
 * @author Ethan Toney
 */
public abstract class SideScrollingRegion extends BaseRegion {

	private static final Random RN_JESUS = new Random();
	
	private static class BackgroundSet {
		
		float
			dx = 0,
			speed,
			offset;
		int last_index = -1;
		Animation<?>[] base_set;
		List<Animation<?>> render_set;
		
		public BackgroundSet(float offset, float speed, Animation<?>[] base_set) {
			this.offset = offset;
			this.speed = speed;
			this.base_set = base_set;
			render_set = new LinkedList<>();
		}
		
	}
	
	private List<BackgroundSet> background_sets = new ArrayList<>();

	/**
	 * @param world_engine
	 * @param name
	 * @param size
	 */
	public SideScrollingRegion(AbstractWorldEngine world_engine, String name, Size size) {
		super(world_engine, name, size);
		// TODO Auto-generated constructor stub
	}
	
	protected void addBackgroundSet(float dy, int speed, Animation<?>[] animations) {
		background_sets.add(new BackgroundSet(dy, speed, animations));
	}

	@Override
	public void render(OpenGLGraphics graphics, float d_sec, AABB world_bounding_box) {
		float screen_width = (float) graphics.getCanvasDimension().width;
		float screen_height = (float) graphics.getCanvasDimension().height;
		
		graphics.view_matrix.reset();
		
		ErrorHandler.tryRun(() -> {
			// Grab the current active shader
			String prev_shader = graphics.shader_manager.getActiveShaderName();
			graphics.shader_manager.useShader("opengl_frame");
			
			graphics.pushMatricies();

			ShaderProgram active_program = graphics.shader_manager.getActiveShader();
			ShaderAttribute position = active_program.getAttributeVariable("in_Position");
			
			// Render background color
			active_program.getUniformVariable("set_Color").setValue(new float[] {0.6078431373f, 0.6784313725f, 0.7176470588f, 1});
			Rectangle.fillRectangle(graphics, position, 0, -8, screen_width, screen_height);
			
			graphics.shader_manager.useShader(prev_shader);
		});
		
		// For each background set
		for (BackgroundSet background_set : background_sets) {
			background_set.dx += background_set.speed * d_sec;
			float consumed_width = 0;
			
			// Render current backgrounds
			for (Iterator<Animation<?>> it = background_set.render_set.iterator(); it.hasNext(); ) {
				Animation<?> bg = it.next();
				bg.renderAnimation(graphics, d_sec, (int) (consumed_width-background_set.dx), (int) background_set.offset, 1);
				float background_width = bg.getCurrentFrame().getBoundingSize().width;
				consumed_width += background_width;
				if (consumed_width < background_set.dx) {
					consumed_width -= background_width;
					background_set.dx -= background_width;
					it.remove();
				}
			}
			
			// Add new backgrounds if necessary
			while (consumed_width <= background_set.dx + screen_width) {
				int next_index = RN_JESUS.nextInt(background_set.base_set.length);
				while (next_index == background_set.last_index) {
					next_index = RN_JESUS.nextInt(background_set.base_set.length);
				}
				background_set.last_index = next_index;
				Animation<?> bg = background_set.base_set[next_index];
				background_set.render_set.add(bg);
				bg.renderAnimation(graphics, d_sec, (int) (consumed_width-background_set.dx), (int) background_set.offset, 1);
				consumed_width += bg.getCurrentFrame().getBoundingSize().width;
			}
		}
		
		super.render(graphics, d_sec, world_bounding_box);
	}

}
