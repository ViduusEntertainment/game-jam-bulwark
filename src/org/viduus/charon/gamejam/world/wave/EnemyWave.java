package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.WorldEngine;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.StandardEnemy;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.TankEnemy;
import org.viduus.charon.gamejam.world.objects.weapons.range.EnemyGun;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public abstract class EnemyWave {
	
	private HashSet<NonPlayableCharacter2D> enemies = new HashSet<>();
	protected final BaseRegion region;
	protected final WorldEngine world_engine;
	
	public EnemyWave(AbstractWorldEngine world_engine, BaseRegion region) {
		this.world_engine = (WorldEngine)world_engine;
		this.region = region;
		createEnemies(enemies);
	}

	protected abstract void createEnemies(HashSet<NonPlayableCharacter2D> enemies);
	
	public void tryRemoveEnemy(Object2D entity) {
		if (enemies.contains(entity))
			enemies.remove(entity);
	}
	
	public boolean isFinished() {
		return enemies.size() == 0;
	}
	
	public void launchThemFuckers() {
		for (NonPlayableCharacter2D enemy : enemies) {
			region.queueEntityForAddition(enemy);
		}
	}
	
	protected StandardEnemy createStandardEnemy(Vector2 location) {
		StandardEnemy enemy = new StandardEnemy(world_engine, "StandardEnemy", location);
		EnemyGun weapon = new EnemyGun(world_engine, "Enemy Weapon", enemy);
		enemy.setLinearVelocity(new Vector2(-100, 0));
		world_engine.insert(enemy);
		world_engine.insert(weapon);
		enemy.equipWeapon(weapon);
		return enemy;
	}
	
	protected TankEnemy createTankEnemy(Vector2 location) {
		TankEnemy enemy = new TankEnemy(world_engine, "TankEnemy", location);
		enemy.setLinearVelocity(new Vector2(-100, 0));
		world_engine.insert(enemy);
		return enemy;
	}
}
