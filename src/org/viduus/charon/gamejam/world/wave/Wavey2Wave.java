package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class Wavey2Wave extends EnemyWave {

	public Wavey2Wave(AbstractWorldEngine world_engine, BaseRegion region) {
		super(world_engine, region);
	}

	@Override
	protected void createEnemies(HashSet<NonPlayableCharacter2D> enemies) { 
		float world_width = world_engine.getWorldSize().width;
		Vector2[] waypoints = new Vector2[12];
		float step = world_width / (waypoints.length - 1);
		
		waypoints[0] = new Vector2(world_width - step * 0, 400);
		waypoints[1] = new Vector2(world_width - step * 1, 390);
		waypoints[2] = new Vector2(world_width - step * 2, 370);
		waypoints[3] = new Vector2(world_width - step * 3, 350);
		waypoints[4] = new Vector2(world_width - step * 4, 300);
		waypoints[5] = new Vector2(world_width - step * 5, 230);
		waypoints[6] = new Vector2(world_width - step * 6, 200);
		waypoints[7] = new Vector2(world_width - step * 7, 170);
		waypoints[8] = new Vector2(world_width - step * 8, 100);
		waypoints[9] = new Vector2(world_width - step * 9, 50);
		waypoints[10] = new Vector2(world_width - step * 10, 30);
		waypoints[11] = new Vector2(-1000, 20);
		
		for (int i = 0; i < 13; i++) {
			enemies.add(createRandomEnemy((new Vector2(world_engine.getWorldSize().width + (11 - i) * 50, 400)), waypoints));
		}
	}

}
