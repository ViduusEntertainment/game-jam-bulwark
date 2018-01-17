/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 9, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import org.dyn4j.geometry.Vector2;
import org.joml.Math;
import org.viduus.charon.gamejam.GameConstants.EngineFlags;
import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.gamejam.world.regions.AutoSideScrollingRegion;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameConstants.GlobalEngineFlags;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.world.objects.twodimensional.DynamicObject2D.NPC_MOVEMENT;
import org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D;

/**
 * 
 *
 * @author Ethan Toney
 */
public class GameStartScreen extends AbstractJamScreen {

	private PlayableCharacter2D main_character;
	boolean accelerating = true;
	boolean stopped = false;
	private Animation<?>
		player_accelerate,
		player_zoomin,
		previous_animation;
	private float
		curr_mod = 1,
		target_mod = 1,
		previous_speed;

	/**
	 * @param graphics_frame
	 */
	public GameStartScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame);
		
		setAlphaComponent(0);
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		super.render(graphics, d_sec);
		
		// update background scroll speed
		if (main_character.getLocation().x < 85) {
			target_mod = 1;
			if (accelerating) {
				accelerating = false;
				player_accelerate.start();
				main_character.set(Property.CURRENT_ANIMATION, player_accelerate);
			}
		}
		
		if (Math.abs(curr_mod - target_mod) > 0.2) {
			float diff = target_mod - curr_mod;
			curr_mod += 2 * d_sec * (diff * 0.9f);
			main_character.<AutoSideScrollingRegion>get(Property.CURRENT_REGION).setBackgroundSpeedMod(curr_mod);
		} else if (curr_mod != target_mod) {
			curr_mod = target_mod;
			main_character.<AutoSideScrollingRegion>get(Property.CURRENT_REGION).setBackgroundSpeedMod(curr_mod);
		}
		
		if (curr_mod == target_mod && stopped) {
			game_systems.graphics_engine.showFrame(GraphicsEngine.GAME_SCREEN);
		}
		
		// update player animation
		if (accelerating) {
			if (player_accelerate.animationIsFinished()) {
				main_character.set(Property.CURRENT_ANIMATION, player_zoomin);
			}
		} else {
			if (player_accelerate.animationIsFinished()) {
				main_character.set(Property.CURRENT_ANIMATION, previous_animation);
				stopped = true;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onActivate(int previous_screen_id, AbstractGameSystems game_systems) {
		// load animations
		player_accelerate = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:player/player_ship.red_ship-accelerate");
		player_zoomin = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:player/player_ship.red_ship-zoomin");
		
		// control the player
		main_character = game_systems.party.get(0);
		Vector2 main_character_location = main_character.getLocation();
		target_mod = 30;
		previous_speed = main_character.getFloat(Property.SPEED);
		main_character.set(Property.SPEED, 200.0f);
		main_character.set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.WAYPOINT_FOLLOWING);
		main_character.set(Property.WAYPOINTS, new Vector2[] {main_character.getLocation(), new Vector2(75, main_character_location.y)});
		main_character.set(Property.CURRENT_WAYPOINT, 0);
		main_character.set(Property.LAST_WAYPOINT_SNAP_TO_FIRST, false);
		main_character.set(Property.AUTO_SPRITE_UPDATE, false);
		previous_animation = main_character.get(Property.CURRENT_ANIMATION);
		main_character.set(Property.CURRENT_ANIMATION, player_zoomin);
		
		// fix engine state
		game_systems.world_engine.enable(GlobalEngineFlags.TICK_WORLD);
	}
	
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		super.onDeactivate(game_systems);
		// fix player state
		main_character.set(Property.SPEED, previous_speed);
		main_character.set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.MANUAL);
		main_character.set(Property.AUTO_SPRITE_UPDATE, true);
		main_character.<AutoSideScrollingRegion>get(Property.CURRENT_REGION).setBackgroundSpeedMod(1);
		game_systems.party.get(0).bindInputEngine(game_systems.input_engine);
		
		// fix engine
		game_systems.world_engine.enable(EngineFlags.SPAWN_WAVES);
	}

}
