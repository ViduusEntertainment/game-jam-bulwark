package org.viduus.charon.gamejam.world.wave;

import java.util.HashSet;

import org.apache.commons.lang3.time.StopWatch;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.WorldEngine;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.StandardEnemy;
import org.viduus.charon.gamejam.world.objects.weapons.range.EnemyGun;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class WallWave extends EnemyWave {

	private StopWatch timer;
	
	public WallWave(AbstractWorldEngine world_engine, BaseRegion region) {
		super(world_engine, region);
	}

	@Override
	protected void createEnemies(HashSet<NonPlayableCharacter2D> enemies) {
		for (int i = 0; i < 13; i++) {
			enemies.add(createTankEnemy(new Vector2(world_engine.getWorldSize().width, 20 + i * 30)));
		}
	}
	
	@Override 
	public void launchThemFuckers() {
		super.launchThemFuckers();
		timer = new StopWatch();
		timer.start();
	}

	@Override
	public boolean isFinished() {
		timer.split();
		OutputHandler.println("" + timer.getSplitTime());
		if (timer.getSplitTime() > 2000) {
			return true;
		} 
		
		timer.unsplit();
		return false;
	}
}
