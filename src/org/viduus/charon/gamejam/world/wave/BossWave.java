package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;
import java.util.Random;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.BosserbossEnemy;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;
import org.viduus.charon.global.world.util.CooldownTimer;

public class BossWave extends EnemyWave {

	private BosserbossEnemy boss;
	
	private CooldownTimer kamikaze_timer = new CooldownTimer(3f);
	private static Random RN_CODY = new Random();
	
	public BossWave(AbstractWorldEngine world_engine, BaseRegion region) {
		super(world_engine, region);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		
		kamikaze_timer.update(tick_event.time_elapsed);
		
		if (!kamikaze_timer.isCooling()) {
			Enemy enemy = createKamikazeEnemy(new Vector2(world_engine.getWorldSize().width + 100, RN_CODY.nextInt((int)world_engine.getWorldSize().height)));
			region.queueEntityForAddition(enemy);
			kamikaze_timer.reset();
		}
	}
	
	@Override
	public boolean isFinished() {
		return enemies.size() == 0;
	}

	@Override
	protected void createEnemies(HashSet<NonPlayableCharacter2D> enemies) {
		Size world_size = world_engine.getWorldSize();
		boss = createBossEnemy(new Vector2(world_size.width + 250, world_size.height / 2));
		boss.getBody().setLinearVelocity(new Vector2(boss.getFloat(Property.SPEED) * -1, 0));
		enemies.add(boss);
	}

	public BosserbossEnemy getBoss() {
		return boss;
	}
}
