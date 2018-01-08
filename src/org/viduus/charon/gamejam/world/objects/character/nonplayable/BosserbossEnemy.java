package org.viduus.charon.gamejam.world.objects.character.nonplayable;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.world.AbstractWorldEngine;

public class BosserbossEnemy extends Enemy {

	private static final float SPEED = 5.0f;
	private static final float HEALTH = 10000;
	
	public BosserbossEnemy(AbstractWorldEngine world_engine, String name, Vector2 location) {
		super(world_engine, name, location, SPEED, HEALTH, 0, HEALTH, 0, "vid:animation:eday_robot", "robot", "walk_l", 0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setPhysicsProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWeaponUse(WeaponUseEvent use_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCollision(CollisionEvent collision_event) {
		// TODO Auto-generated method stub
		
	}
}
