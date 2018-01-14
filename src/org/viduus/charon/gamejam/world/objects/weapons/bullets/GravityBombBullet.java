package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import java.util.List;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.physics.twodimensional.filters.GravityOrbFilter;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.util.CooldownTimer;

public class GravityBombBullet extends Bullet {

	private CooldownTimer gravity_timer;
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public GravityBombBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location) {
		super(world_engine, uid, name, owner, location, new Vector2(800, 0), "vid:animation:objects/bullets", "gravity_bomb", 0f);
		setCollisionFilter(new GravityOrbFilter(this));
		gravity_timer = new CooldownTimer(3f);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		gravity_timer.update(tick_event.time_elapsed);
		if (!gravity_timer.isCooling()) {
			getCurrentRegion().queueEntityForRemoval(this);
		}
		else {
			List<IdentifiedResource> enemies = world_engine.collect("vid:npc:*");
			
			for (IdentifiedResource resource : enemies) {
				Enemy enemy = (Enemy)resource;
				enemy.set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.OBJECT_FOLLOWING);
				enemy.setFollowee(this, 0.0f);
			}
		}
	}
	
	@Override
	protected void beforeBodyCreation() {
		super.beforeBodyCreation();
		set(Property.LINEAR_DAMPING, 3.0);
	}
	
	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttached(IdentifiedResource owner) {
		gravity_timer.reset();
	}
	
	@Override
	public void onDetached(IdentifiedResource owner) {
		List<IdentifiedResource> enemies = world_engine.collect("vid:npc:*");
		
		for (IdentifiedResource resource : enemies) {
			Enemy enemy = (Enemy)resource;
			enemy.set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.MANUAL);
			enemy.setLinearVelocity(enemy.get(Property.INITIAL_LINEAR_VELOCITY));
		}
	}
	
	@Override
	public void onCollision(CollisionEvent collision_event) {}
}
