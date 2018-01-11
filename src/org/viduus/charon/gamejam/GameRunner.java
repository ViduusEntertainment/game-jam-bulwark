/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam;

import org.viduus.charon.gamejam.audio.AudioEngine;
import org.viduus.charon.gamejam.event.EventEngine;
import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.gamejam.item.ItemEngine;
import org.viduus.charon.gamejam.quest.QuestEngine;
import org.viduus.charon.gamejam.world.WorldEngine;
import org.viduus.charon.global.input.InputEngine;
import org.viduus.charon.global.input.controller.ControllerInputListener;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.input.controller.device.KeyboardMouseController;
import org.viduus.charon.global.util.systems.SystemsEngine;
import org.viduus.charon.global.world.dialog.DialogEngine;

/**
 * 
 *
 * @author Ethan Toney
 */
public class GameRunner {
	
	private GameSystems game_systems;

	/**
	 * TODO
	 */
	public GameRunner() {
		game_systems = new GameSystems(
				new AudioEngine(),
				new ItemEngine(),
				new QuestEngine(),
				new SystemsEngine(),
				new WorldEngine(60),
				new InputEngine(),
				new DialogEngine(),
				new EventEngine(),
				new GraphicsEngine()
		);
		
		game_systems.graphics_engine.bindController((KeyboardMouseController) game_systems.input_engine.getDefaultController());
		game_systems.input_engine.registerListener(0, "default-controls", new ControllerInputListener(){

			@Override
			public void onControllerState(ControllerState e) {
				if( e.getKeyState(ControllerState.EXIT_GAME_KEY) == ControllerState.PRESSED_STATE ){
					game_systems.closeApplication();
				}
			}
			
		});
		
		// Will enter a semi-infinite loop here until the main window is closed
		game_systems.graphics_engine.startEventQueue();
		
		game_systems.closeApplication();
		game_systems.joinApplicationCloseThread();
		
		// Force the game to close
		System.exit(0);
	}

	/**
	 * TODO
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		
		new GameRunner();
	}

}
