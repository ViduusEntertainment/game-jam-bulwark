/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics;

import org.viduus.charon.gamejam.GameSystems;
import org.viduus.charon.gamejam.graphics.frames.menu.GameOverScreen;
import org.viduus.charon.gamejam.graphics.frames.menu.GameStartScreen;
import org.viduus.charon.gamejam.graphics.frames.menu.IntroScreen;
import org.viduus.charon.gamejam.graphics.frames.menu.MenuScreen;
import org.viduus.charon.gamejam.graphics.frames.menu.UpgradeScreen;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.screens.AbstractScreenManager;

/**
 * 
 *
 * @author Ethan Toney
 */
public class ScreenManager extends AbstractScreenManager {

	/**
	 * TODO
	 * @param game_systems
	 */
	public ScreenManager(GameSystems game_systems, OpenGLFrame graphics_frame) {
		super(new IntroScreen(graphics_frame));
		registerGameScreen(GraphicsEngine.MENU_SCREEN, new MenuScreen(graphics_frame));
		registerGameScreen(GraphicsEngine.UPGRADE_SCREEN, new UpgradeScreen(graphics_frame));
		registerGameScreen(GraphicsEngine.START_GAME_SCREEN, new GameStartScreen(graphics_frame));
		registerGameScreen(GraphicsEngine.PLAYER_DEATH_SCREEN, new GameOverScreen(graphics_frame));
	}

}
