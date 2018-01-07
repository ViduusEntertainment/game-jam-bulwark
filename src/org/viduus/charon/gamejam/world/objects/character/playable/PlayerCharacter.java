/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.objects.character.playable;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.GameSystems;
import org.viduus.charon.gamejam.input.PlayerControls;
import org.viduus.charon.global.AbstractGameSystems.PauseType;
import org.viduus.charon.global.GameConstants;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.input.InputEngine;
import org.viduus.charon.global.input.controller.Controller;
import org.viduus.charon.global.input.player.PlayerControlsState;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.RangeWeapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;
import org.viduus.charon.global.world.regions.BaseRegion;

/**
 * 
 *
 * @author Ethan Toney
 */
public class PlayerCharacter extends PlayableCharacter2D {

	private static final float
		DEFAULT_HEALTH = 100f,
		DEFAULT_MANA = 100f,
		DEFAULT_SPEED = 3.0f,
		SPRINT_CONSTANT = 1.5f,
		STAMINA_SPRINT_CONSTANT = 20, // stam/sec
		ROLL_CONSTANT = 3.0f,
		ROLL_DURATION = 0.5f,
		STAMINA_ROLL_CONSTANT = 30, // stam/sec
		STAMINA_REGEN_CONSTANT = 20,
		STAMINA_ATTACK_CONSTANT = 8; // stam/attack
	private static final String
		DEFAULT_SPRITE_FILE = "vid:animation:eday_robot",
		DEFAULT_SPRITE_ID = "robot";
	
	private final GameSystems game_systems;
	private Vector2 last_trans = new Vector2();
	private boolean controller_binded = false;
	private Controller default_controller;
	
	private float WEAPON_COOL_DOWN = .3f; // seconds
	private float usage_countdown = 0;
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param speed
	 * @param health
	 * @param mana
	 * @param max_health
	 * @param max_mana
	 * @param animation_file
	 * @param sprite_id
	 */
	public PlayerCharacter(GameSystems game_systems, String name, Vector2 location) {
		super(game_systems.world_engine, name, location, DEFAULT_SPEED, DEFAULT_HEALTH, DEFAULT_MANA, DEFAULT_HEALTH, DEFAULT_MANA, DEFAULT_SPRITE_FILE, DEFAULT_SPRITE_ID);
		
		this.game_systems = game_systems;
		this.<Boolean>set(Property.IS_MOVABLE, true);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D#performTick(org.viduus.charon.global.input.player.PlayerControlsState, org.viduus.charon.global.event.events.TickEvent)
	 */
	@Override
	protected void performTick(PlayerControlsState controls_state, TickEvent tick_event) {
		float time_elapsed = tick_event.time_elapsed;
		
		usage_countdown -= time_elapsed;
		
		// Player hit Interaction key/button. Set this object to be in an interacting state
		this.<Boolean>set(Property.IS_INTERACTING, controls_state.isSelect());
		
		// Set whether or not the player is attacking
		this.set(Property.IS_ATTACKING, controls_state.getAttack());
		
		// Player selected Start key/button. Quick pause the game
		if (controls_state.getStart()) {
			if (!game_systems.isPaused()) {
				game_systems.pauseGame(PauseType.QUICK);
			}
		}

		// Check for things that use energy
		boolean using_energy = false;
		float curr_mana = getFloat(Property.MANA);
		float max_mana = getFloat(Property.MAX_MANA);
		float velocity_mod = 1;
		
		// Check if the player is attacking
		if(controls_state.getAttack()){
			if (!usingWeapon())
			{
				world_engine.queueEvent(this, new WeaponUseEvent(getWeapons().get(0)), WeaponUseEvent.class);
				usage_countdown = WEAPON_COOL_DOWN;
			}
		}
		
		// Check if sprinting
		if( controls_state.getSprint() > 0 ){
			float stamina_to_sprint = STAMINA_SPRINT_CONSTANT * time_elapsed;
			if( curr_mana > stamina_to_sprint ){
				using_energy = true;
				curr_mana -= stamina_to_sprint;
				velocity_mod = SPRINT_CONSTANT;
			}
		}
		
		// Regen mana
		if( !using_energy ){
			float stamina_this_tick = STAMINA_REGEN_CONSTANT * time_elapsed;
			curr_mana += stamina_this_tick;
			if( curr_mana > max_mana ) curr_mana = max_mana;
		}
		set(Property.MANA, curr_mana);
		
		// TODO Check if rolling
		boolean is_rolling = false; //action_timer.getElapstedTimeSec() < ROLL_DURATION;
//		if( is_rolling ){
//			velocity_mod = ROLL_CONSTANT;
//			
//		}else if( controls_state.getRoll() == 1 ){
//			float stamina_to_roll = STAMINA_ROLL_CONSTANT;
//			if( curr_mana > stamina_to_roll ){
//				using_energy = true;
//				curr_mana -= stamina_to_roll;
//				action_timer.startClock();
//			}
//		}
		
		double dx=0, dy=0;
		
		float speed = this.<Float>get(Property.SPEED);
		// Move player
		if( is_rolling ){			
			dx = velocity_mod * speed * last_trans.x;
			dy = velocity_mod * speed * last_trans.y;
		}else{
			float x_trans = controls_state.getLeft() + controls_state.getRight();
			float y_trans = controls_state.getUp() + controls_state.getDown();
			last_trans = new Vector2(x_trans, y_trans).getNormalized();
			
			dx = velocity_mod * speed * x_trans; // 20 = movement speed
			dy = velocity_mod * speed * y_trans;
		}
		
		if( dx != 0 && dy != 0 ){
			dx *= org.viduus.charon.global.util.math.Constants.SQRT2OVER2;
			dy *= org.viduus.charon.global.util.math.Constants.SQRT2OVER2;
		}
		
		setLinearVelocity(new Vector2(dx * GameConstants.PIXELS_PER_METER, dy * GameConstants.PIXELS_PER_METER));
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D#bindInputEngine(org.viduus.charon.global.input.InputEngine)
	 */
	@Override
	public void bindInputEngine(InputEngine input_engine) {
		if( !controller_binded ){
			controller_binded = true;
			OutputHandler.println("Binding listener");
			default_controller = this.game_systems.input_engine.getDefaultController();
			setController(new PlayerControls());
			game_systems.input_engine.registerListener(0, "main-player-default-controls", getController());
		}else{
			ErrorHandler.println("Tried to add MainCharacter listener after it was already added!");
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D#unbindInputEngine(org.viduus.charon.global.input.InputEngine)
	 */
	@Override
	public void unbindInputEngine(InputEngine input_engine) {
		if( controller_binded ){
			controller_binded = false;
			OutputHandler.println("Unbinding listener");
			game_systems.input_engine.removeListener(0, "main-player-default-controls");
		}else{
			ErrorHandler.println("Tried to remove MainCharacter listener after it was already removed!");
		}
	}
	
	/**
	 * <b>Do not call during tick event callback.</b>
	 * 
	 * @return
	 */
	protected float getTimeSinceWeaponUse() {
		return Math.max(-usage_countdown, 0);
	}
	
	/**
	 * <b>Do not call during tick event callback.</b>
	 * 
	 * @return
	 */
	protected boolean usingWeapon() {
		return usage_countdown > 0;
	}

	@Override
	public void onWeaponUse(WeaponUseEvent weapon_use_event) {
		RangeWeapon2D weapon = (RangeWeapon2D)weapon_use_event.weapon;
		Bullet2D bullet = weapon.createBullet();
		world_engine.insert(bullet);
		this.<BaseRegion>get(Property.CURRENT_REGION).addEntity(bullet);
	}

}