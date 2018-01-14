package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.physics.twodimensional.filters.FriendlyBulletFilter;
import org.viduus.charon.gamejam.world.objects.effects.Explosion;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class BombBullet extends Bullet{
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public BombBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, float damage) {
		super(world_engine, uid, name, owner, location, new Vector2(0, 0), "vid:animation:objects/bullets", "bomb", damage);
		setCollisionFilter(new FriendlyBulletFilter(this));
	}
	
	@Override
	protected void beforeBodyCreation() {
		super.beforeBodyCreation();
		set(Property.GRAVITY_SCALE, 1.0);
	}
	
	@Override
	public void onCollision(CollisionEvent collision_event) {
		super.onCollision(collision_event);
		Explosion explosion = new Explosion(world_engine, "Explosion", getLocation().copy());
		world_engine.insert(explosion);
		getCurrentRegion().queueEntityForAddition(explosion);
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}
}
