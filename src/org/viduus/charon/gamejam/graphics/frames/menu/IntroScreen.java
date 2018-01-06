/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import org.viduus.charon.gamejam.graphics.ScreenManager;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.GameFrame;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.screens.AbstractGameScreen;
import org.viduus.charon.global.graphics.util.IntDimension;

/**
 * 
 *
 * @author Ethan Toney
 */
public class IntroScreen extends AbstractGameScreen {

	private Animation<?> logo;

	/**
	 * @param game_frame
	 */
	public IntroScreen(GameFrame game_frame) {
		super(game_frame);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec){
		super.render(graphics, d_sec);

		if( logo.animationIsFinished() ){
			game_systems.graphics_engine.showFrame( ScreenManager.MENU_SCREEN );
			return;
		}

		IntDimension canvas_dimension = graphics.getCanvasDimension();
		logo.renderAnimation(graphics, d_sec, canvas_dimension.width/2, canvas_dimension.height/2, 4);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(AbstractGameSystems game_systems) {
		game_frame.setTitle("Space!");
		game_frame.setLocationRelativeTo(null);
		game_frame.setDesiredFPS(30);
		
		logo = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:viduus_logo.anim_0");
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
