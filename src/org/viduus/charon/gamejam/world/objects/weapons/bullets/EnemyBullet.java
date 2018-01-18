package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.physics.twodimensional.filters.EnemyBulletFilter;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;

public class EnemyBullet extends Bullet {

	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public EnemyBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location) {
		super(world_engine, uid, name, owner, location, new Vector2(-200, 0), "vid:animation:objects/bullets", "enemy_bullet", 0f);
		set(Property.COLLISION_FILTER, new EnemyBulletFilter(this));
		set(Property.IS_BULLET, false);
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttached(IdentifiedResource owner) {}
}
