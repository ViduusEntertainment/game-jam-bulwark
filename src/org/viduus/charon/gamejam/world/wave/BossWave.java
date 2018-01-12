package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class BossWave extends EnemyWave {

	public BossWave(AbstractWorldEngine world_engine, BaseRegion region) {
		super(world_engine, region);
	}

	@Override
	protected void createEnemies(HashSet<NonPlayableCharacter2D> enemies) {
		
	}

}
