package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;
import java.util.Random;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.WorldEngine;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.BosserbossEnemy;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.KamikazeEnemy;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.StandardEnemy;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.TankEnemy;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.gamejam.world.objects.weapons.range.BossLaserGun;
import org.viduus.charon.gamejam.world.objects.weapons.range.EnemyGun;
import org.viduus.charon.gamejam.world.regions.Level1;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.DynamicObject2D.NPC_MOVEMENT;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;
import org.viduus.charon.global.world.util.CooldownTimer;

public abstract class EnemyWave {
	
	protected HashSet<NonPlayableCharacter2D> enemies = new HashSet<>();
	protected final Level1 region;
	protected final WorldEngine world_engine;
	protected boolean launched = false;
	private static Random RN_CODY = new Random();
	private PlayerCharacter character;
	private CooldownTimer backup_finished = new CooldownTimer(15f);
	
	public EnemyWave(AbstractWorldEngine world_engine, BaseRegion region) {
		this.world_engine = (WorldEngine)world_engine;
		this.region = (Level1)region;
		character = (PlayerCharacter)this.region.getParty().get(0);
		createEnemies(enemies);
	}

	protected abstract void createEnemies(HashSet<NonPlayableCharacter2D> enemies);
	
	public void tryRemoveEnemy(Object2D entity) {
		if (enemies.contains(entity))
			enemies.remove(entity);
	}
	
	public boolean isFinished() {
		return enemies.size() == 0 || !backup_finished.isCooling();
	}
	
	public void onTick(TickEvent tick_event) {
		if (!launched) {
			for (NonPlayableCharacter2D enemy : enemies) {
				region.queueEntityForAddition(enemy);
			}
			launched = true;
			backup_finished.reset();
		}
		
		if (launched) {
			backup_finished.update(tick_event.time_elapsed);
		}
	}
	
	protected StandardEnemy createStandardEnemy(Vector2 location) {
		StandardEnemy enemy = new StandardEnemy(world_engine, "StandardEnemy", location);
		EnemyGun weapon = new EnemyGun(world_engine, "Enemy Weapon", enemy);
		enemy.getBody().setLinearVelocity(new Vector2(-100, 0));
		world_engine.insert(enemy);
		world_engine.insert(weapon);
		enemy.equipWeapon(weapon);
		return enemy;
	}
	
	protected StandardEnemy createWaypointStandardEnemy(Vector2 location, Vector2[] waypoints) {
		StandardEnemy enemy = new StandardEnemy(world_engine, "StandardEnemy", location);
		EnemyGun weapon = new EnemyGun(world_engine, "Enemy Weapon", enemy);
		enemy.set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.WAYPOINT_FOLLOWING);
		enemy.set(Property.WAYPOINTS, waypoints);
		world_engine.insert(enemy);
		world_engine.insert(weapon);
		enemy.equipWeapon(weapon);
		return enemy;
	}
	
	protected TankEnemy createTankEnemy(Vector2 location) {
		TankEnemy enemy = new TankEnemy(world_engine, "TankEnemy", location);
		world_engine.insert(enemy);
		return enemy;
	}

	protected KamikazeEnemy createKamikazeEnemy(Vector2 location) {
		KamikazeEnemy enemy = new KamikazeEnemy(world_engine, "KamikazeEnemy", location, character);
		world_engine.insert(enemy);
		return enemy;
	}
	
	protected BosserbossEnemy createBossEnemy(Vector2 location) {
		BosserbossEnemy enemy = new BosserbossEnemy(world_engine, "BosserbossEnemy", location);
		BossLaserGun weapon = new BossLaserGun(world_engine, "Boss Weapon", enemy);
		world_engine.insert(enemy);
		world_engine.insert(weapon);
		enemy.equipWeapon(weapon);
		return enemy;
	}
	
	protected Enemy createRandomEnemy(Vector2 location, Vector2[] waypoints) {
		int num = RN_CODY.nextInt(100);
		
		if (num < 30)
			return createKamikazeEnemy(location);
		else if (waypoints != null)
			return createWaypointStandardEnemy(location, waypoints);
		else 
			return createStandardEnemy(location);
	}
	
	protected Enemy createRandomEnemy(Vector2 location) {
		return createRandomEnemy(location, null);
	}
}
