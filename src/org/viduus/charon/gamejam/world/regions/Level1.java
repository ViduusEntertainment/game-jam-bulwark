/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.regions;

import java.util.Random;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.GameConstants.EngineFlags;
import org.viduus.charon.gamejam.audio.AudioEngine;
import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.BosserbossEnemy;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.gamejam.world.wave.BossWave;
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
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;

/**
 * 
 *
 * @author Ethan Toney
 */
public class Level1 extends AutoSideScrollingRegion {
	
	private PlayerParty party;
	private EnemyWave active_enemy_wave;
	private static Random RN_CODY = new Random();
	
	private final AbstractGraphicsEngine graphics_engine;
	
	private float total_enemy_health = 10000.0f;
	private float enemy_health = 10000.0f;
	private boolean is_battling_boss = false;
	private BosserbossEnemy boss;
	
	/**
	 * @param world_engine
	 * @param name
	 */
	public Level1(AbstractWorldEngine world_engine, AbstractGraphicsEngine graphics_engine, PlayerParty party) {
		super(world_engine, "level_1", new Size(5000, 384), new Vector2(0, 0));
		this.party = party;
		active_enemy_wave = createRandomWave();
		this.graphics_engine = graphics_engine;
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
		
		if (death_event.object_that_died instanceof BosserbossEnemy) {
			is_battling_boss = false;
			enemy_health = 10000f;
			AudioEngine.BOSS_BATTLE_TRACK.stop();
			AudioEngine.LEVEL1_TRACK.play();
		}
		else if (death_event.object_that_died instanceof Enemy) {
			Enemy enemy = (Enemy)death_event.object_that_died;
			PlayerCharacter player = (PlayerCharacter) party.get(0);
			player.giveMoney(enemy.getReward());
			setEnemyHealth(getEnemyHealth() - enemy.getFloat(Property.MAX_HEALTH));
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
			if (!player_dead) {
				if ((active_enemy_wave == null || active_enemy_wave.isFinished()) && getPercentEnemyHealth() > 0.0f) {
					active_enemy_wave = createRandomWave();
				} 
				else if (getPercentEnemyHealth() <= 0.0f && !is_battling_boss) {
					BossWave boss_wave = new BossWave(world_engine, this);
					active_enemy_wave = boss_wave;
					boss = boss_wave.getBoss();
					enemy_health = 10000f;
					is_battling_boss = true;
				}
			} 
			else {
				AudioEngine.BOSS_BATTLE_TRACK.stop();
				graphics_engine.showFrame(GraphicsEngine.UPGRADE_SCREEN);
			}
			
			if (is_battling_boss) {
				setEnemyHealth(boss.getFloat(Property.HEALTH));
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
	
	@Override
	public void onReleased() {
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
