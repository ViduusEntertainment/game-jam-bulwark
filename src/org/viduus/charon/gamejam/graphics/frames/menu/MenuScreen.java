/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.GameSystems;
import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.gamejam.graphics.SideScrollingBackground;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.gamejam.world.objects.weapons.range.DefaultGun;
import org.viduus.charon.gamejam.world.objects.weapons.range.MissileGun1;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameInfo;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.components.OpenGLButton;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.player.PlayerParty;
import org.viduus.charon.global.util.ResourceLoader;
import org.viduus.charon.global.util.logging.ErrorHandler;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 * 
 *
 * @author Ethan Toney
 */
public class MenuScreen extends AbstractJamScreen {

	private OpenGLButton start_button;
	private OpenGLButton exit_button;
	private Sound menu_sound;
	private OpenGLButton credits_button;
	private Size screen_size;
	private SideScrollingBackground background;

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
		
		credits_button = new OpenGLButton("Credits");
		credits_button.setBackgroundColor(182, 182, 182);
		credits_button.setHoverColor(83, 119, 215);
		add(credits_button);
		
		exit_button = new OpenGLButton("Exit");
		exit_button.setBackgroundColor(182, 182, 182);
		exit_button.setHoverColor(83, 119, 215);
		add(exit_button);
		
		background = new SideScrollingBackground();
		
		setOpaqueBackground(false);
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		background.render(graphics, d_sec);
		super.render(graphics, d_sec);
	}
	
	@Override
	public void onControllerState(ControllerState e) {
		super.onControllerState(e);
		
		if(e.getKeyState(ControllerState.SELECT_KEY) == ControllerState.PRESSED_STATE) {
			if (start_button.hasFocus()) {
				PlayerParty party = new PlayerParty();
				
				// add player to party
				PlayerCharacter character_1 = new PlayerCharacter((GameSystems) game_systems, "Sauran", new Vector2(screen_size.width/2+100, 200));
				DefaultGun character_1_primary = new DefaultGun(game_systems.world_engine, "Primary Weapon", character_1, 100);
				MissileGun1 character_1_secondary = new MissileGun1(game_systems.world_engine, "Secondary Weapon", character_1, 800);
				game_systems.world_engine.insert(character_1_primary);
				game_systems.world_engine.insert(character_1_secondary);
				game_systems.world_engine.insert(character_1);
				character_1.equipPrimaryWeapon(character_1_primary);
				character_1.equipSecondaryWeapon(character_1_secondary);
				party.add(character_1);
				
				// start the game
				GameInfo game_info = new GameInfo(GameSystems.GAME, party);
				game_systems.startGame(game_info);
				game_systems.graphics_engine.showFrame(GraphicsEngine.UPGRADE_SCREEN);
			}
			else if (this.exit_button.hasFocus()) {
				game_systems.closeApplication();
			}
			else if (credits_button.hasFocus()) {
				
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(AbstractGameSystems game_systems) {
		background.addBackgroundSet(-8, 20, new Animation<?>[] {
			(Animation<?>) game_systems.graphics_engine.resolve("vid:animation:backgrounds/city_landscape.back_1"),
			(Animation<?>) game_systems.graphics_engine.resolve("vid:animation:backgrounds/city_landscape.back_2"),
			(Animation<?>) game_systems.graphics_engine.resolve("vid:animation:backgrounds/city_landscape.back_3"),
		});
		background.addBackgroundSet(70, 30, new Animation<?>[] {
			(Animation<?>) game_systems.graphics_engine.resolve("vid:animation:backgrounds/city_landscape.front_1"),
			(Animation<?>) game_systems.graphics_engine.resolve("vid:animation:backgrounds/city_landscape.front_2"),
			(Animation<?>) game_systems.graphics_engine.resolve("vid:animation:backgrounds/city_landscape.front_3"),
		});
		
		screen_size = game_systems.graphics_engine.getScreenSize();
		menu_sound = ErrorHandler.tryRun(() -> TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/music/menu/main_menu.ogg")));
		menu_sound.play();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		menu_sound.unload();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {
		start_button.setBounds((width-200)/2, height/2, 200, 20);
		exit_button.setBounds((width-200)/2, height/2 + 35, 200, 20);
		credits_button.setBounds(width-100, height-30, 90, 20);
	}

}
