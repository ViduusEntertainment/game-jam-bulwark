/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.regions;

import java.util.Random;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.gamejam.world.wave.EnemyWave;
import org.viduus.charon.gamejam.world.wave.LeftDiagonalWave;
import org.viduus.charon.gamejam.world.wave.RightDiagonalWave;
import org.viduus.charon.gamejam.world.wave.SpearheadWave;
import org.viduus.charon.gamejam.world.wave.TankWallWave;
import org.viduus.charon.gamejam.world.wave.Wavey1Wave;
import org.viduus.charon.gamejam.world.wave.Wavey2Wave;
import org.viduus.charon.global.event.events.DeathEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.player.PlayerParty;
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
	
	/**
	 * @param world_engine
	 * @param name
	 */
	public Level1(AbstractWorldEngine world_engine, PlayerParty party) {
		super(world_engine, "level_1", new Size(5000, 384), new Vector2(0, 0));
		this.party = party;
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
		if (active_enemy_wave != null)
			active_enemy_wave.onTick(tick_event);
		if (active_enemy_wave == null || active_enemy_wave.isFinished()) {
			active_enemy_wave = createRandomWave();
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
}
