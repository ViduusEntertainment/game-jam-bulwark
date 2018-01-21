package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.audio.AudioEngine;
import org.viduus.charon.gamejam.physics.twodimensional.filters.EnemyBulletFilter;
import org.viduus.charon.gamejam.world.regions.Level1;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;

public class BossLaserBeam extends Bullet{
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public BossLaserBeam(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, Vector2 velocity, float damage, double rotation) {
		super(world_engine, uid, name, owner, location, velocity, "vid:animation:objects/bullets", "boss_laser_beam", damage);
		set(Property.COLLISION_FILTER, new EnemyBulletFilter(this));
		set(Property.ROTATION, rotation);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttached(IdentifiedResource owner) {
		AudioEngine.BOSS_LASER.play(0.05);
	}
}
