package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;

import org.apache.commons.lang3.time.StopWatch;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class TankWallWave extends EnemyWave {

	private StopWatch timer;
	
	public TankWallWave(AbstractWorldEngine world_engine, BaseRegion region) {
		super(world_engine, region);
	}

	@Override
	protected void createEnemies(HashSet<NonPlayableCharacter2D> enemies) {
		for (int i = 0; i < 13; i++) {
			enemies.add(createTankEnemy(new Vector2(world_engine.getWorldSize().width, 20 + i * 30)));
		}
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		if (!launched) {
			timer = new StopWatch();
			timer.start();
		}
		
		super.onTick(tick_event);
	}

	@Override
	public boolean isFinished() {
		timer.split();
		if (timer.getSplitTime() > 2000) {
			return true;
		} 
		
		timer.unsplit();
		return false;
	}
}
