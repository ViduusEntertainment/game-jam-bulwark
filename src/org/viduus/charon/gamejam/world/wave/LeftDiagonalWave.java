package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.WorldEngine;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.StandardEnemy;
import org.viduus.charon.gamejam.world.objects.weapons.range.EnemyGun;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class LeftDiagonalWave extends EnemyWave {

	public LeftDiagonalWave(AbstractWorldEngine world_engine, BaseRegion region) {
		super(world_engine, region);
	}

	@Override
	protected void createEnemies(HashSet<NonPlayableCharacter2D> enemies) {
		for (int i = 0; i < 13; i++) {
			enemies.add(createStandardEnemy(new Vector2(world_engine.getWorldSize().width + i * 30, 20 + i * 30)));
		}
	}

}
