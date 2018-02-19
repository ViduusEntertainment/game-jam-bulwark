package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.physics.twodimensional.filters.FriendlyBulletFilter;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;

public class ScatterBullet extends Bullet{
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public ScatterBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, float damage) {
		this(world_engine, uid, name, owner, location, new Vector2(800, 0), damage);
	}
	
	public ScatterBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, Vector2 velocity, float damage) {
		super(world_engine, uid, name, owner, location, velocity, "vid:animation:objects/bullets", "scatter", damage);
		set(Property.COLLISION_FILTER, new FriendlyBulletFilter(this));
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttached(IdentifiedResource owner) {
		getStereoTrack("scattershot").play();
	}
}
