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
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.input.InputEngine;
import org.viduus.charon.global.input.controller.Controller;
import org.viduus.charon.global.input.player.PlayerControlsState;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.RangeWeapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;
import org.viduus.charon.global.world.regions.BaseRegion;
import org.viduus.charon.global.world.util.CooldownTimer;

/**
 * 
 *
 * @author Ethan Toney
 */
public class PlayerCharacter extends PlayableCharacter2D {

	private static final float
		DEFAULT_HEALTH = 5,
		DEFAULT_MANA = 100f,
		DEFAULT_SPEED = 10.0f,
		SPRINT_CONSTANT = 1.5f,
		STAMINA_SPRINT_CONSTANT = 20, // stam/sec
		ROLL_CONSTANT = 3.0f,
		ROLL_DURATION = 0.5f,
		STAMINA_ROLL_CONSTANT = 30, // stam/sec
		STAMINA_REGEN_CONSTANT = 20,
		STAMINA_ATTACK_CONSTANT = 8; // stam/attack
	private static final String
		DEFAULT_SPRITE_FILE = "vid:animation:player/player_ship",
		DEFAULT_SPRITE_ID = "red_ship";
	
	private final GameSystems game_systems;
	private Vector2 last_trans = new Vector2();
	private boolean controller_binded = false;
	private Controller default_controller;
	
	/*
	 * Animation
	 */
	private Animation<?>[] vertical_animations = null;
	private int last_animation_index = -1;
	private int animaiton_index = 3;
	
	/*
	 * Cool downs
	 */
	private CooldownTimer primary_weapon_timer;
	private CooldownTimer secondary_weapon_timer;
	private CooldownTimer immunity_timer = new CooldownTimer(1f);
	
	/*
	 * Weapons
	 */
	private RangeWeapon2D primary_weapon;
	private RangeWeapon2D secondary_weapon;
	
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
	
	@Override
	protected void setPhysicsProperties() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void determineActiveAnimation() {
		// load animations
		if (vertical_animations == null) {
			vertical_animations = new Animation<?>[] {
				(Animation<?>) world_engine.resolve("vid:animation:player/player_ship.red_ship-walk_d_start_4"),
				(Animation<?>) world_engine.resolve("vid:animation:player/player_ship.red_ship-walk_d_start_3"),
				(Animation<?>) world_engine.resolve("vid:animation:player/player_ship.red_ship-walk_d_start_2"),
				(Animation<?>) world_engine.resolve("vid:animation:player/player_ship.red_ship-walk_d_start_1"),
				(Animation<?>) world_engine.resolve("vid:animation:player/player_ship.red_ship-walk_u_start_2"),
				(Animation<?>) world_engine.resolve("vid:animation:player/player_ship.red_ship-walk_u_start_3"),
				(Animation<?>) world_engine.resolve("vid:animation:player/player_ship.red_ship-walk_u_start_4")
			};
		}
		
		Animation<?> curr_animation = get(Property.CURRENT_ANIMATION);
		if (curr_animation != null) {
			// determine what the next animation is
			if (curr_animation.animationIsFinished() || animaiton_index == 3) {
				// if not moving
				if (getLinearVelocity().isZero()) {
					if (animaiton_index > 3) {
						animaiton_index--;
					} else if (animaiton_index < 3) {
						animaiton_index++;
					}
					
				// if moving
				} else {
					switch (getInteger(Property.DIRECTION_FACING)) {
					case (LEFT):
					case (RIGHT):
						animaiton_index = 3;
						break;
					case (UP):
						animaiton_index++;
						break;
					case (DOWN):
						animaiton_index--;
						break;
					}
				}
			}
			if (animaiton_index >= vertical_animations.length)
				animaiton_index = vertical_animations.length-1;
			if (animaiton_index < 0)
				animaiton_index = 0;

			// skip setting the animation if already set
			if (animaiton_index == last_animation_index)
				return;
			last_animation_index = animaiton_index;
		}
		
		setAnimation(vertical_animations[animaiton_index]);
		set(Property.CURRENT_ANIMATION, vertical_animations[animaiton_index]);
		
		last_animation_index = animaiton_index;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D#performTick(org.viduus.charon.global.input.player.PlayerControlsState, org.viduus.charon.global.event.events.TickEvent)
	 */
	@Override
	protected void performTick(PlayerControlsState controls_state, TickEvent tick_event) {
		float time_elapsed = tick_event.time_elapsed;
		
		if (primary_weapon_timer != null) primary_weapon_timer.update(time_elapsed);
		if (secondary_weapon_timer != null) secondary_weapon_timer.update(time_elapsed);
		immunity_timer.update(time_elapsed);
		
		// Player hit Interaction key/button. Set this object to be in an interacting state
		this.<Boolean>set(Property.IS_INTERACTING, controls_state.isSelect());
		
		// Set whether or not the player is attacking
		this.set(Property.IS_ATTACKING, controls_state.getPrimaryAttack() || controls_state.getSecondaryAttack());
		
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
		
		// Check if the player is attacking with primary
		if(controls_state.getPrimaryAttack()){
			if (!primary_weapon_timer.isCooling())
			{
				world_engine.queueEvent(this, new WeaponUseEvent(primary_weapon), WeaponUseEvent.class);
				primary_weapon_timer.reset();
			}
		}
		
		// Check if the player is attacking with secondary
		if(controls_state.getSecondaryAttack()){
			if (!secondary_weapon_timer.isCooling())
			{
				world_engine.queueEvent(this, new WeaponUseEvent(secondary_weapon), WeaponUseEvent.class);
				secondary_weapon_timer.reset();
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

	public void equipPrimaryWeapon(RangeWeapon2D weapon) {
		if (primary_weapon != null) unequipWeapon(primary_weapon);
		primary_weapon = weapon;
		primary_weapon_timer = new CooldownTimer(weapon.getCooldown());
		equipWeapon(weapon);
	}
	
	public void equipSecondaryWeapon(RangeWeapon2D weapon) {
		if (secondary_weapon != null) unequipWeapon(secondary_weapon);
		secondary_weapon = weapon;
		secondary_weapon_timer = new CooldownTimer(weapon.getCooldown());
		equipWeapon(weapon);
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

	@Override
	public void onWeaponUse(WeaponUseEvent weapon_use_event) {
		RangeWeapon2D weapon = (RangeWeapon2D)weapon_use_event.weapon;
		Bullet2D bullet = weapon.createBullet();
		world_engine.insert(bullet);
		this.<BaseRegion>get(Property.CURRENT_REGION).addEntity(bullet);
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Weapon strength detection?
	}

	@Override
	protected void onCollision(CollisionEvent collision_event) {
		if (!immunity_timer.isCooling()) {		
			set(Property.HEALTH, getFloat(Property.HEALTH) - 1);
			immunity_timer.reset();
		}
	}
}
