/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics;

import org.viduus.charon.gamejam.GameSystems;
import org.viduus.charon.gamejam.graphics.ui.CharacterHUD;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.AbstractGameSystems.PauseType;
import org.viduus.charon.global.GameInfo;
import org.viduus.charon.global.graphics.AbstractGraphicsEngine;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.screens.AbstractScreenManager;
import org.viduus.charon.global.graphics.screens.PauseMenu;
import org.viduus.charon.global.util.logging.OutputHandler;

/**
 * 
 *
 * @author Ethan Toney
 */
public class GraphicsEngine extends AbstractGraphicsEngine {

	private ScreenManager screen_manager;
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.AbstractGraphicsEngine#getAnimationResolverClassPath()
	 */
	@Override
	protected String getAnimationResolverClassPath() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.AbstractGraphicsEngine#getScreenManager()
	 */
	@Override
	protected AbstractScreenManager getScreenManager() {
		return screen_manager;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.AbstractGraphicsEngine#getPauseMenu(org.viduus.charon.global.AbstractGameSystems.PauseType)
	 */
	@Override
	protected PauseMenu getPauseMenu(PauseType pause_type) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.GameEngine#onLoadGame(org.viduus.charon.global.GameInfo)
	 */
	@Override
	protected void onLoadGame(GameInfo game_info) {
		super.onLoadGame(game_info);
		
		setHeadsUpDisplay(new CharacterHUD());
	}
	
	@Override
	protected void onLoadEngine(final AbstractGameSystems game_systems) {
		super.onLoadEngine(game_systems);
		// Setup an error callback. The default implementation
		// will print the error message in System.err.

		OutputHandler.startTimedPrint("Creating FrameManager...");
		screen_manager = new ScreenManager((GameSystems) game_systems, graphics_frame);
		OutputHandler.endTimedPrint("Finished creating FrameManager");
		
		OutputHandler.startTimedPrint("Assigning default game screen");
		showFrame(DEFAULT_SCREEN);
		OutputHandler.endTimedPrint("Finished assigning default game screen");
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.AbstractGraphicsEngine#createInitialGameFrame()
	 */
	@Override
	protected OpenGLFrame createInitialGameFrame() {
		return new GameFrame();
	}

}
