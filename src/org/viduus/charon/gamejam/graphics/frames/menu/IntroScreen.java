/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.audio.AudioCategory;
import org.viduus.charon.global.audio.Sound;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.util.IntDimension;
import org.viduus.charon.global.input.controller.ControllerState;

/**
 * 
 *
 * @author Ethan Toney
 */
public class IntroScreen extends AbstractJamScreen {

	private Animation<?> logo;
	private Sound intro_sound;

	/**
	 * @param graphics_frame
	 */
	public IntroScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec){
		super.render(graphics, d_sec);

		if( logo.animationIsFinished() ){
			game_systems.graphics_engine.showFrame(GraphicsEngine.MENU_SCREEN);
			return;
		}

		IntDimension canvas_dimension = graphics.getCanvasDimension();
		logo.renderAnimation(graphics, d_sec, canvas_dimension.width/2, canvas_dimension.height/2, 4);
	}
	
	@Override
	public void onControllerState(ControllerState e) {
		super.onControllerState(e);
		
		if(e.getKeyState(ControllerState.SELECT_KEY) == ControllerState.PRESSED_STATE) {
			game_systems.graphics_engine.showFrame(GraphicsEngine.MENU_SCREEN);
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(AbstractGameSystems game_systems) {
		game_frame.setTitle("Space!");
		game_frame.setLocationRelativeTo(null);
		game_frame.setDesiredFPS(30);
		
		intro_sound = game_systems.audio_engine.createSound(AudioCategory.MUSIC, "resources/audio/sfx/intro/viduus.ogg", true);
		game_systems.audio_engine.playSound(intro_sound);
		
		logo = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:viduus_logo.anim_0");
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		game_systems.audio_engine.stopSound(intro_sound);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
