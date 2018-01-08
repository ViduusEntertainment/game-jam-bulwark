/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world;

import org.dyn4j.collision.AxisAlignedBounds;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.regions.Level1;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.physics.twodimensional.listeners.CollisionListener;
import org.viduus.charon.global.GameInfo;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;

/**
 * 
 *
 * @author Ethan Toney
 */
public class WorldEngine extends AbstractWorldEngine {

	/**
	 * @param fps
	 */
	public WorldEngine(int fps) {
		super(fps);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.AbstractWorldEngine#getNpcResolverClassPath()
	 */
	@Override
	protected String getNpcResolverClassPath() {
		return "org.viduus.charon.gamejam.world.objects.character.nonplayable";
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.WorldEngine#getRegionResolverClassPath()
	 */
	@Override
	protected String getRegionResolverClassPath() {
		return "org.viduus.charon.gamejam.world.regions";
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.WorldEngine#getCharacterResolverClassPath()
	 */
	@Override
	protected String getCharacterResolverClassPath() {
		return "org.viduus.charon.gamejam.world.objects.character.playable";
	}
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.WorldEngine#getWeaponResolverClassPath()
	 */
	@Override
	protected String getWeaponResolverClassPath() {
		return "org.viduus.charon.gamejam.world.objects.weapons";
	}
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.WorldEngine#getBulletResolverClassPath()
	 */
	@Override
	protected String getBulletResolverClassPath() {
		return "org.viduus.charon.gamejam.world.objects.weapons.bullets";
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.GameEngine#onLoadGame(org.viduus.charon.global.GameInfo)
	 */
	@Override
	protected void onLoadGame(GameInfo game_info) {
		super.onLoadGame(game_info);
		
		// Create the first level
		Level1 level_1 = new Level1(this, game_info.party);
		insert(level_1);
		ErrorHandler.tryRun(() -> {
			level_1.load();
		});
		
		// Load in demo Main Character information
		PlayableCharacter2D main_character = game_info.party.get(0);
		main_character.set(Property.CURRENT_REGION_ID, new Uid("vid:region:level_1"));
		
		// Load demo region and put player in region
		BaseRegion character_region = (BaseRegion) resolve((Uid) main_character.get(Property.CURRENT_REGION_ID));
		for (PlayableCharacter2D party_member : game_info.party) {
			character_region.queueEntityForAddition(party_member);
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.GameEngine#onSaveAndDisposeEngine()
	 */
	@Override
	protected void onSaveAndDisposeEngine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected World createWorld() {
		World world = new World();
		world.setGravity(new Vector2(0, 0));
		world.addListener(new CollisionListener(this));
		
		Settings settings = new Settings();
		settings.setAutoSleepingEnabled(false);
		settings.setMaximumTranslation(Double.MAX_VALUE);
		
		world.setSettings(settings);
		
		return world;
	}

}
