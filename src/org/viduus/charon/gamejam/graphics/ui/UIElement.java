/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 8, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.components.OpenGLButton;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;
import org.viduus.charon.global.graphics.opengl.shaders.ShaderProgram;
import org.viduus.charon.global.graphics.opengl.shaders.exceptions.ShaderException;
import org.viduus.charon.global.graphics.opengl.shaders.variables.ShaderAttribute;
import org.viduus.charon.global.graphics.opengl.shapes.Rectangle;
import org.viduus.charon.global.util.logging.ErrorHandler;

/**
 * 
 *
 * @author Ethan Toney
 */
public abstract class UIElement extends OpenGLButton {

	private static boolean animations_loaded = false;
	private static Animation<?>
		border_lt,
		border_t,
		border_rt,
		border_r,
		border_rb,
		border_b,
		border_lb,
		border_l,
		money;

	public UIElement() {
		setAlphaComponent(0.0f);
	}
	
	public abstract void render(OpenGLGraphics graphics, float d_sec);
	
	public final void activate(AbstractGameSystems game_systems) {
		if (!animations_loaded) {
			animations_loaded = true;
			border_lt = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_lt");
			border_t = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_t");
			border_rt = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_rt");
			border_r = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_r");
			border_rb = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_rb");
			border_b = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_b");
			border_lb = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_lb");
			border_l = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/ui_boxes.select_l");
			money = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.money");
		}
		
		onActivate(game_systems);
	}
	
	public abstract void onActivate(AbstractGameSystems game_systems);
	
	public final void deactivate(AbstractGameSystems game_systems) {
		if (animations_loaded) {
			animations_loaded = false;
			game_systems.graphics_engine.release(border_lt);
			game_systems.graphics_engine.release(border_t);
			game_systems.graphics_engine.release(border_rt);
			game_systems.graphics_engine.release(border_r);
			game_systems.graphics_engine.release(border_rb);
			game_systems.graphics_engine.release(border_b);
			game_systems.graphics_engine.release(border_lb);
			game_systems.graphics_engine.release(border_l);
			game_systems.graphics_engine.release(money);
		}
		
		onDeactivate(game_systems);
	}
	
	public abstract void onDeactivate(AbstractGameSystems game_systems);
	
	protected abstract void updateSize();
	
	public void renderSelect(OpenGLGraphics graphics, float d_sec, int x, int y, int width, int height) {
		// render corners
		border_lt.renderAnimation(graphics, d_sec, x, y, 1);
		border_rt.renderAnimation(graphics, d_sec, x+width-1, y, 1);
		border_lb.renderAnimation(graphics, d_sec, x, y+height-1, 1);
		border_rb.renderAnimation(graphics, d_sec, x+width-1, y+height-1, 1);
		
		// render top and bottom
		for (int tx=2 ; tx<=width-2 ; tx++) {
			border_t.renderAnimation(graphics, d_sec, x+tx, y, 1);
			border_b.renderAnimation(graphics, d_sec, x+tx, y+height-1, 1);
		}
		
		// render left and right
		for (int ty=2 ; ty<=height-2 ; ty++) {
			border_l.renderAnimation(graphics, d_sec, x, y+ty, 1);
			border_r.renderAnimation(graphics, d_sec, x+width-1, y+ty, 1);
		}
	}
	
	protected static void renderTextAndAnimationScreen(OpenGLGraphics graphics, float d_sec, int x, int y, int width, int height, String text1, String text2, int number, Animation<?> icon) {
		int dr = 14;
		int ds = OpenGLFont.getLineHeight();
		int top = y + (height - (OpenGLFont.getLineHeight()+17))/2;
		
		OpenGLFont.drawString2D(graphics, text1, (int) (x + (width - OpenGLFont.getStringWidth(text1))/2), top+ds-1);
		
		String disp_str = text2+number;
		int line_2_width = (int) (OpenGLFont.getStringWidth(disp_str) + 17);
		int left = x + (width - line_2_width) / 2;
		icon.renderAnimation(graphics, d_sec, left, top+dr, 1);
		OpenGLFont.drawString2D(graphics, disp_str, left+17, top+dr+ds-1);
	}

	/**
	 * @param graphics
	 * @param i
	 * @param j
	 * @param k
	 * @param l
	 */
	public static void renderColoredSquare(OpenGLGraphics graphics, int x, int y, int width, int height, float r, float g, float b, float a) {
		try{
			String prev_shader = graphics.shader_manager.getActiveShaderName();
			graphics.shader_manager.useShader("opengl_frame");
			
			graphics.pushMatricies();

			ShaderProgram active_program = graphics.shader_manager.getActiveShader();
			ShaderAttribute position = active_program.getAttributeVariable("in_Position");

			// Render transparent background
			active_program.getUniformVariable("set_Color").setValue(new float[] {r, g, b, a});
			Rectangle.fillRectangle(graphics, position, x, y, width, height);
			
			graphics.shader_manager.useShader(prev_shader);
			
		}catch( ShaderException e ){
			ErrorHandler.handleError(e);
		}
	}
	
}
