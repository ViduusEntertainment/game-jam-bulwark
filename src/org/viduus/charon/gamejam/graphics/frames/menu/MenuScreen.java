/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.GameSystems;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameInfo;
import org.viduus.charon.global.audio.AudioCategory;
import org.viduus.charon.global.audio.Sound;
import org.viduus.charon.global.graphics.AbstractGraphicsEngine;
import org.viduus.charon.global.graphics.GameFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.components.OpenGLButton;
import org.viduus.charon.global.graphics.screens.AbstractGameScreen;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.player.PlayerParty;

/**
 * 
 *
 * @author Ethan Toney
 */
public class MenuScreen extends AbstractGameScreen {

	private OpenGLButton start_button;
	private OpenGLButton exit_button;
	private Sound menu_sound;

	/**
	 * @param graphics_frame
	 */
	public MenuScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame);
		
		graphics_frame.setDesiredFPS(30);
		
		start_button = new OpenGLButton("Start Game");
		start_button.setBackgroundColor(182, 182, 182);
		start_button.setHoverColor(83, 119, 215);
		add(start_button);
		
		exit_button = new OpenGLButton("Exit");
		exit_button.setBackgroundColor(182, 182, 182);
		exit_button.setHoverColor(83, 119, 215);
		add(exit_button);
	}
	
	@Override
	public void onControllerState(ControllerState e) {
		super.onControllerState(e);
		
		if(e.getKeyState(ControllerState.SELECT_KEY) == ControllerState.PRESSED_STATE) {
			if(start_button.hasFocus()) {
				PlayerParty party = new PlayerParty();
				
				// add player to party
				PlayerCharacter character_1 = new PlayerCharacter((GameSystems) game_systems, "Sauran", new Vector2(100, 100));
				game_systems.world_engine.insert(character_1);
				party.add(character_1);
				
				// start the game
				GameInfo game_info = new GameInfo(GameSystems.GAME, party);
				game_systems.startGame(game_info);
				game_systems.graphics_engine.showFrame(AbstractGraphicsEngine.GAME_SCREEN);
			}
			else if(this.exit_button.hasFocus()) {
				game_systems.closeApplication();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(AbstractGameSystems game_systems) {
		menu_sound = game_systems.audio_engine.createSound(AudioCategory.MUSIC, "resources/audio/music/menu/start_menu.ogg", true);
		menu_sound.setToLoop(true);
		game_systems.audio_engine.playSound(menu_sound);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		game_systems.audio_engine.stopSound(menu_sound);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {
		start_button.setBounds((width-200)/2, height/2, 200, 20);
		exit_button.setBounds((width-200)/2, height/2 + 35, 200, 20);
	}

}
