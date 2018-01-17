/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.regions;

import java.util.Random;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.GameConstants.EngineFlags;
import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.gamejam.world.wave.EnemyWave;
import org.viduus.charon.gamejam.world.wave.LeftDiagonalWave;
import org.viduus.charon.gamejam.world.wave.RightDiagonalWave;
import org.viduus.charon.gamejam.world.wave.SpearheadWave;
import org.viduus.charon.gamejam.world.wave.TankWallWave;
import org.viduus.charon.gamejam.world.wave.Wavey1Wave;
import org.viduus.charon.gamejam.world.wave.Wavey2Wave;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.DeathEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.graphics.AbstractGraphicsEngine;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.player.PlayerParty;
import org.viduus.charon.global.util.ResourceLoader;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 * 
 *
 * @author Ethan Toney
 */
public class Level1 extends AutoSideScrollingRegion {
	
	private PlayerParty party;
	private EnemyWave active_enemy_wave;
	private static Random RN_CODY = new Random();
	
	public static float SFX = 0.3f;
	
	/*
	 * Sounds
	 */
	public static Sound KAMIKAZE_SOUND;
	public static Sound EXPLOSION_SOUND;
	public static Sound BASIC_BULLET_SOUND;
	public static Sound MISSILE_SOUND;
	public static Sound SCATTERSHOT_SOUND;
	public static Sound LASER_CHARGE_SOUND; 
	public static Sound BOSS_SCREECH_SOUND; 
	public static Sound EMP_SOUND;
	
	private final AbstractGraphicsEngine graphics_engine;
	private boolean played_boss_screech = false;
	
	/**
	 * @param world_engine
	 * @param name
	 */
	public Level1(AbstractWorldEngine world_engine, AbstractGraphicsEngine graphics_engine, PlayerParty party) {
		super(world_engine, "level_1", new Size(5000, 384), new Vector2(0, 0));
		this.party = party;
		active_enemy_wave = createRandomWave();
		this.graphics_engine = graphics_engine;

		new Thread(new Runnable() {
			public void run() {
				ErrorHandler.tryRun(() -> {
					KAMIKAZE_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/kamikaze.ogg"));
					EXPLOSION_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/explosion.ogg"));
					BASIC_BULLET_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/basic_bullet.ogg"));
					MISSILE_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/missile.ogg"));
					SCATTERSHOT_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/scattershot.ogg"));
					LASER_CHARGE_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/charge_laser.ogg"));
					BOSS_SCREECH_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/boss_screech.ogg"));
					EMP_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/EMP.ogg"));
				});
			}
		}).start();
	}
	
	public PlayerParty getParty() {
		return party;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.regions.BaseRegion#addObjectCallbacks(org.viduus.charon.global.world.objects.twodimensional.Object2D)
	 */
	@Override
	protected void addObjectCallbacks(Object2D obj) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.regions.BaseRegion#removeObjectCallbacks(org.viduus.charon.global.world.objects.twodimensional.Object2D)
	 */
	@Override
	protected void removeObjectCallbacks(Object2D obj) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.regions.BaseRegion#load()
	 */
	@Override
	public void load() {
		addBackgroundSet(-8, 20, new Animation<?>[] {
			(Animation<?>) world_engine.resolve("vid:animation:backgrounds/city_landscape.back_1"),
			(Animation<?>) world_engine.resolve("vid:animation:backgrounds/city_landscape.back_2"),
			(Animation<?>) world_engine.resolve("vid:animation:backgrounds/city_landscape.back_3"),
		});
		addBackgroundSet(70, 30, new Animation<?>[] {
			(Animation<?>) world_engine.resolve("vid:animation:backgrounds/city_landscape.front_1"),
			(Animation<?>) world_engine.resolve("vid:animation:backgrounds/city_landscape.front_2"),
			(Animation<?>) world_engine.resolve("vid:animation:backgrounds/city_landscape.front_3"),
		});
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.regions.BaseRegion#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override 
	protected void onObjectDeath(DeathEvent death_event) {
		super.onObjectDeath(death_event);
		
		if (death_event.object_that_died instanceof Enemy) {
			Enemy enemy = (Enemy)death_event.object_that_died;
			PlayerCharacter player = (PlayerCharacter) party.get(0);
			player.giveMoney(enemy.getReward());
			player.setEnemyHealth(player.getEnemyHealth() - enemy.getFloat(Property.MAX_HEALTH));
		}
		
		if (active_enemy_wave != null) {
			active_enemy_wave.tryRemoveEnemy(death_event.object_that_died);
		}
	}
	
	@Override
	protected void onObjectRemoval(ObjectRemovalEvent object_removal_event) {
		super.onObjectRemoval(object_removal_event);
		if (active_enemy_wave != null) {
			active_enemy_wave.tryRemoveEnemy(object_removal_event.object_to_remove);
		}
	}
	
	@Override
	protected void onTick(TickEvent tick_event) {
		if (world_engine.enabled(EngineFlags.SPAWN_WAVES)) {
			PlayerCharacter player = (PlayerCharacter) party.get(0);
			
			boolean player_dead = player.getBoolean(Property.IS_DEAD);
			
			if (active_enemy_wave != null)
				active_enemy_wave.onTick(tick_event);
			if ((active_enemy_wave == null || active_enemy_wave.isFinished()) && !player_dead) {
				active_enemy_wave = createRandomWave();
				OutputHandler.println(active_enemy_wave.getClass().getSimpleName());
			}
			
			if (player_dead && active_enemy_wave.isFinished()) {
				graphics_engine.showFrame(GraphicsEngine.UPGRADE_SCREEN);
			}
			
			if (!played_boss_screech && player.getPercentEnemyHealth() < 0.5f) {
				BOSS_SCREECH_SOUND.play();
				played_boss_screech = true;
			}
		}
	}
	
	private EnemyWave createRandomWave() {
		int num = RN_CODY.nextInt(120);
		
		if (num < 20)
			return new LeftDiagonalWave(world_engine, this);
		else if (num < 40)
			return new RightDiagonalWave(world_engine, this);
		else if (num < 60)
			return new SpearheadWave(world_engine, this);
		else if (num < 80)
			return new TankWallWave(world_engine, this);
		else if (num < 100)
			return new Wavey1Wave(world_engine, this);
		else
			return new Wavey2Wave(world_engine, this);
	}
	
	@Override
	public void onReleased() {
		KAMIKAZE_SOUND.unload();
		EXPLOSION_SOUND.unload();
		BASIC_BULLET_SOUND.unload();
		MISSILE_SOUND.unload();
		SCATTERSHOT_SOUND.unload();
		LASER_CHARGE_SOUND.unload();
		BOSS_SCREECH_SOUND.unload();
		EMP_SOUND.unload();
	}

	@Override
	public void onAttached(IdentifiedResource owner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDetached(IdentifiedResource owner) {
		// TODO Auto-generated method stub
		
	}
}
