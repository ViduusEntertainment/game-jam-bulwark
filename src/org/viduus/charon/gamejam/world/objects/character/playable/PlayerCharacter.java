/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.objects.character.playable;

import java.util.HashMap;
import java.util.HashSet;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.GameSystems;
import org.viduus.charon.gamejam.input.PlayerControls;
import org.viduus.charon.gamejam.world.objects.effects.Shield;
import org.viduus.charon.gamejam.world.objects.weapons.range.ChainGun;
import org.viduus.charon.gamejam.world.objects.weapons.range.ChainLightningGun;
import org.viduus.charon.gamejam.world.objects.weapons.range.DefaultGun;
import org.viduus.charon.gamejam.world.objects.weapons.range.LaserCharge;
import org.viduus.charon.gamejam.world.objects.weapons.range.LaserGun;
import org.viduus.charon.gamejam.world.objects.weapons.range.MissileGun1;
import org.viduus.charon.gamejam.world.objects.weapons.range.MissileGun2;
import org.viduus.charon.gamejam.world.objects.weapons.range.ScatterGun;
import org.viduus.charon.gamejam.world.ships.Fast;
import org.viduus.charon.gamejam.world.ships.Heavy;
import org.viduus.charon.gamejam.world.ships.Mk1;
import org.viduus.charon.gamejam.world.ships.Mk2;
import org.viduus.charon.gamejam.world.ships.Ship;
import org.viduus.charon.global.AbstractGameSystems.PauseType;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.input.InputEngine;
import org.viduus.charon.global.input.controller.Controller;
import org.viduus.charon.global.input.player.PlayerControlsState;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
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
		DEFAULT_HEALTH = 3,
		DEFAULT_MANA = 100f,
		DEFAULT_SPEED = 500.0f,
		SPRINT_CONSTANT = 1.5f,
		STAMINA_SPRINT_CONSTANT = 20, // stam/sec
		STAMINA_REGEN_CONSTANT = 20; // stam/attack
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
	private int animaiton_index;
	
	/*
	 * Cool downs
	 */
	private CooldownTimer primary_weapon_timer;
	private CooldownTimer secondary_weapon_timer;
	private CooldownTimer shield_timer = new CooldownTimer(15f);
	private CooldownTimer immunity_timer = new CooldownTimer(0.5f);
	
	/*
	 * Weapons
	 */
	private RangeWeapon2D primary_weapon;
	private RangeWeapon2D secondary_weapon;
	private HashSet<String> purchased_ships = new HashSet<>();
	private Ship ship;
	
	private int money = 1000000;
	
	private float total_enemy_health = 10000.0f;
	private float enemy_health = 10000.0f;
	
	private int thruster_upgrades = 0;
	private int armor_upgrades = 0;
	private int shield_upgrades = 0;
	
	private HashMap<String, Integer> upgrades = new HashMap<>();
	
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
		setShip(new Mk1());
		purchased_ships.add("Mk1");
		upgrades.put("Basic", 0);
		upgrades.put("Missile1", 0);
	}
	
	@Override
	protected void setPhysicsProperties() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void determineActiveAnimation() {
		// load animations
		if (vertical_animations == null) {
			animaiton_index = 3;
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
		shield_timer.update(time_elapsed);
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
			if (!secondary_weapon_timer.isCooling() && secondary_weapon.getInteger(Property.PROJECTILE_COUNT) > 0)
			{
				world_engine.queueEvent(this, new WeaponUseEvent(secondary_weapon), WeaponUseEvent.class);
				secondary_weapon_timer.reset();
			}
		}
		
		if (controls_state.getShield()) {
			if (!shield_timer.isCooling()) {
				world_engine.queueEvent(this, new WeaponUseEvent(new Shield(world_engine, Uid.generateUid("vid:effect", "Shield"), "Shield", this, 15f - 15f * 0.05f * shield_upgrades)), WeaponUseEvent.class);
				shield_timer.reset();
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
		
		setLinearVelocity(new Vector2(dx, dy));
	}
	
	public int getMoney() {
		return money;
	}
	
	public void giveMoney(int amount) {
		money += amount;
	}
	
	public void takeMoney(int amount) {
		money -= amount;
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
	
	public CooldownTimer getPrimaryWeaponCooldownTimer() {
		return primary_weapon_timer;
	}
	
	public CooldownTimer getSecondaryWeaponCooldownTimer() {
		return secondary_weapon_timer;
	}
	
	public CooldownTimer getShieldCooldownTimer() {
		return shield_timer;
	}
	
	public RangeWeapon2D getPrimaryWeapon() {
		return primary_weapon;
	}
	
	public RangeWeapon2D getSecondaryWeapon() {
		return secondary_weapon;
	}
	
	public void setTotalEnemyHealth(float total_enemy_health) {
		this.total_enemy_health = total_enemy_health;
	}
	
	public float getPercentEnemyHealth() {
		return enemy_health / total_enemy_health;
	}
	
	
	public float getEnemyHealth() {
		return enemy_health;
	}
	
	public void setEnemyHealth(float enemy_health) {
		if (enemy_health < 0) enemy_health = 0;
		this.enemy_health = enemy_health;
	}
	
	private void setShip(Ship ship) {
		this.ship = ship;
		this.ship.setArmorUpgrade(armor_upgrades);
		this.ship.setThrusterUpgrade(thruster_upgrades);
		set(Property.HEALTH, ship.getHearts());
		set(Property.SPEED, ship.getSpeed());
	}

	public boolean tryPurchaseShip(String name, int price) {
		Ship ship = null;
		switch (name) {
		case "Mk1":
			ship = new Mk1();
			break;
		case "Mk2":
			ship = new Mk2();
			break;
		case "Heavy":
			ship = new Heavy();
			break;
		case "Fast":
			ship = new Fast();
			break;
		}
		
		if (purchased_ships.contains(name)) {
			setShip(ship);
			return true;
		} else if (money >= price) {
			takeMoney(price);
			setShip(ship);
			purchased_ships.add(name);
			return true;
		}
		return false;
	}
	
	public boolean tryPurchasePrimaryWeapon(String name, int price) {
		RangeWeapon2D weapon = null;
		switch (name) {
		case "Basic":
			weapon = new DefaultGun(world_engine, "Primary Weapon", this, calculateWeaponDamage(name, 100));
			break;
		case "Chain":
			weapon = new ChainGun(world_engine, "Primary Weapon", this, calculateWeaponDamage(name, 60));
			break;
		case "Scatter":
			weapon = new ScatterGun(world_engine, "Primary Weapon", this, calculateWeaponDamage(name, 100));
			break;
		case "Arc":
			weapon = new ChainLightningGun(world_engine, "Primary Weapon", this, calculateWeaponDamage(name, 60));
			break;
		case "ChargeLaser":
			weapon = new LaserGun(world_engine, "Primary Weapon", this, calculateWeaponDamage(name, 250));
			break;
		}
		
		if (upgrades.containsKey(name)) {
			world_engine.insert(weapon);
			equipPrimaryWeapon(weapon);
			return true;
		} else if (money >= price) {
			world_engine.insert(weapon);
			takeMoney(price);
			equipPrimaryWeapon(weapon);
			upgrades.put(name, 0);
			return true;
		}
		return false;
	}
	
	public boolean tryPurchaseSecondaryWeapon(String name, int price) {
		RangeWeapon2D weapon = null;
		switch (name) {
		case "BasicMissile":
			weapon = new MissileGun1(world_engine, "Secondary Weapon", this, calculateWeaponDamage(name, 800));
			break;
		case "ScatterMissile":
			weapon = new MissileGun2(world_engine, "Secondary Weapon", this, calculateWeaponDamage(name, 1000));
			break;
		case "Bomb":
			break;
		case "Emp":
			break;
		case "GravityOrb":
			break;
		}
		
		if (upgrades.containsKey(name)) {
			world_engine.insert(weapon);
			equipSecondaryWeapon(weapon);
			return true;
		} else if (money >= price) {
			world_engine.insert(weapon);
			takeMoney(price);
			equipSecondaryWeapon(weapon);
			upgrades.put(name, 0);
			return true;
		}
		return false;
	}
	
	public boolean tryUpgradeWeapon(String name, int price) {
		int upgrade = upgrades.get(name);
		if (money >= price && upgrade < 5) {
			takeMoney(price);
			upgrades.put(name, ++upgrade);
			return true;
		}
		return false;
	}
	
	public int getUpgradeLevel(String name) {
		return upgrades.get(name);
	}
	
	private float calculateWeaponDamage(String name, float base_damage) { 
		float new_damage = base_damage;
		
		if (upgrades.containsKey(name)) {
			for (int i = 0; i < upgrades.get(name); i++) {
				new_damage += new_damage * 0.3f;
			}
		}
		
		return new_damage;
	}
	
	public boolean upgradeArmor(int price) {
		if (money >= price && armor_upgrades < 3) {
			takeMoney(price);
			armor_upgrades++;
			setShip(ship);
			return true;
		}
		return false;
	}
	
	public boolean upgradeShield(int price) {
		if (money >= price && shield_upgrades < 5) {
			takeMoney(price);
			shield_upgrades++;
			shield_timer.setCooldown(15f - 15f * 0.05f * shield_upgrades);
			return true;
		}
		return false;
	}
	
	public boolean upgradeThruster(int price) {
		if (money >= price && thruster_upgrades < 5) {
			takeMoney(price);
			thruster_upgrades++;
			setShip(ship);
			return true;
		}
		return false;
	}
	
	public int getArmorUpgrades() {
		return armor_upgrades;
	}
	
	public int getThrusterUpgrades() {
		return thruster_upgrades;
	}
	
	public int getShieldUpgrades() {
		return shield_upgrades;
	}
	
	public Ship getShip() {
		return ship;
	}
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D#bindInputEngine(org.viduus.charon.global.input.InputEngine)
	 */
	@Override
	public void bindInputEngine(InputEngine input_engine) {
		if( !controller_binded ){
			controller_binded = true;
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
			game_systems.input_engine.removeListener(0, "main-player-default-controls");
		}else{
			ErrorHandler.println("Tried to remove MainCharacter listener after it was already removed!");
		}
	}

	@Override
	public void onWeaponUse(WeaponUseEvent weapon_use_event) {
		if (weapon_use_event.weapon instanceof Shield) {
			world_engine.insert(weapon_use_event.weapon);
			this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(weapon_use_event.weapon);
		}
		else if (weapon_use_event.weapon instanceof RangeWeapon2D) {
			RangeWeapon2D weapon = (RangeWeapon2D)weapon_use_event.weapon;
			if (weapon instanceof ScatterGun) {
				Bullet2D bullet1 = weapon.getBullet();
				bullet1.setLinearVelocity(new Vector2(800, -100));
				world_engine.insert(bullet1);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullet1);
				
				Bullet2D bullet2 = weapon.getBullet();
				bullet2.setLinearVelocity(new Vector2(800, 0));
				world_engine.insert(bullet2);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullet2);
				
				Bullet2D bullet3 = weapon.getBullet();
				bullet3.setLinearVelocity(new Vector2(800, 100));
				world_engine.insert(bullet3);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullet3);
			}
			else if (weapon instanceof LaserGun) {
				Weapon2D laser_charge = new LaserCharge(world_engine, "Laser Charge", this, weapon.getFloat(Property.DAMAGE));
				world_engine.insert(laser_charge);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(laser_charge);
			}
			else {
				Bullet2D bullet = weapon.getBullet();
				world_engine.insert(bullet);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullet);
			}
		}
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
	
	
	@Override
	public void onReleased() {}
	
	@Override
	public void onAttached(IdentifiedResource owner) {}
	
	@Override
	public void onDetached(IdentifiedResource owner) {}
}
