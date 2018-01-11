/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 9, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.shaders.ShaderProgram;
import org.viduus.charon.global.graphics.opengl.shaders.variables.ShaderAttribute;
import org.viduus.charon.global.graphics.opengl.shapes.Rectangle;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.global.util.logging.OutputHandler;

/**
 * 
 *
 * @author Ethan Toney
 */
public class SideScrollingBackground {

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
	
	private static final Random RN_JESUS = new Random();
	
	private float bg_speed_mod = 1;
	private List<BackgroundSet> background_sets = new ArrayList<>();
	
	/**
	 * @param graphics_frame
	 */
	public SideScrollingBackground() {}

	
	public void addBackgroundSet(float dy, int speed, Animation<?>[] animations) {
		background_sets.add(new BackgroundSet(dy, speed, animations));
	}
	
	public void setBackgroundSpeedMod(float mod) {
		bg_speed_mod = mod;
	}
	
	public void render(OpenGLGraphics graphics, float d_sec) {
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
			background_set.dx += bg_speed_mod * background_set.speed * d_sec;
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
	}
}
