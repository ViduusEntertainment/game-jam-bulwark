package org.viduus.charon.gamejam.world.objects.character.nonplayable;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;

public class TestEnemy extends NonPlayableCharacter2D{

	private static final float SPEED = 5.0f;
	
	public TestEnemy(AbstractWorldEngine world_engine, String name, Vector2 location) {
		super(world_engine, name, location, SPEED, 100, 0, 100, 0, "vid:animation:eday_robot", "robot");
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

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

}
