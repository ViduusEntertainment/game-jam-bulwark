/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.world.regions;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.TestEnemy;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;

/**
 * 
 *
 * @author Ethan Toney
 */
public class Level1 extends SideScrollingRegion {
	
	/**
	 * @param world_engine
	 * @param name
	 */
	public Level1(AbstractWorldEngine world_engine) {
		super(world_engine, "level_1", new Size(5000, 384));
		// TODO Auto-generated constructor stub
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
		
		TestEnemy test_enemy_1 = new TestEnemy(world_engine, "TestEnemy1", new Vector2(200, 200));
		world_engine.insert(test_enemy_1);
		addEntity(test_enemy_1);
//		
//		TestEnemy test_enemy_2 = new TestEnemy(world_engine, "TestEnemy2", new Vector2(250, 150));
//		world_engine.insert(test_enemy_2);
//		addEntity(test_enemy_2);
//		
//		TestEnemy test_enemy_3 = new TestEnemy(world_engine, "TestEnemy2", new Vector2(250, 250));
//		world_engine.insert(test_enemy_3);
//		addEntity(test_enemy_3);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.world.regions.BaseRegion#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
