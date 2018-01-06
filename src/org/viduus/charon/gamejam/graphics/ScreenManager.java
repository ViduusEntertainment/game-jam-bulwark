/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics;

import org.viduus.charon.gamejam.GameSystems;
import org.viduus.charon.gamejam.graphics.frames.menu.IntroScreen;
import org.viduus.charon.gamejam.graphics.frames.menu.MenuScreen;
import org.viduus.charon.global.graphics.GameFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.screens.AbstractScreenManager;

/**
 * 
 *
 * @author Ethan Toney
 */
public class ScreenManager extends AbstractScreenManager {

	public static final int INTRO_SCREEN = 100;
	public static final int MENU_SCREEN = 200;

	/**
	 * TODO
	 * @param game_systems
	 */
	public ScreenManager(GameSystems game_systems, OpenGLFrame graphics_frame) {
		super(new IntroScreen(graphics_frame));
		registerGameScreen(MENU_SCREEN, new MenuScreen(graphics_frame));
	}

}
