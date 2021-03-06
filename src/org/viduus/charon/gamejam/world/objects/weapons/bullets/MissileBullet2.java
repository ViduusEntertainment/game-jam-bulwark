package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.physics.twodimensional.filters.Bullet2DFilter;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class MissileBullet2 extends Bullet {

	private float MAX_TRAVEL = 200.0f;
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public MissileBullet2(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, float damage) {
		super(world_engine, uid, name, owner, location, new Vector2(600, 0), "vid:animation:objects/bullets", "player_missile_2", damage);
		set(Property.COLLISION_FILTER, new Bullet2DFilter(this));
	}
	
	@Override 
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		if (getLocation().distanceSquared(getVector2(Property.INITIAL_LOCATION)) > MAX_TRAVEL * MAX_TRAVEL) {
			world_engine.event_engine.queueEvent(this, new ObjectRemovalEvent(this), ObjectRemovalEvent.class);
			
			BaseRegion region = get(Property.CURRENT_REGION);
			
			MissileBullet3 bullet1 = new MissileBullet3(world_engine, Uid.generateUid("vid:bullet", "MissileBullet3"), "MissileBullet3", (Weapon2D)getOwner(), getLocation().copy(), 600f);
			bullet1.getBody().setLinearVelocity(new Vector2(600, 200));
			world_engine.insert(bullet1);
			region.queueEntityForAddition(bullet1);
			
			MissileBullet3 bullet2 = new MissileBullet3(world_engine, Uid.generateUid("vid:bullet", "MissileBullet3"), "MissileBullet3", (Weapon2D)getOwner(), getLocation().copy(), 600f);
			bullet2.getBody().setLinearVelocity(new Vector2(600, 0));
			world_engine.insert(bullet2);
			region.queueEntityForAddition(bullet2);
			
			MissileBullet3 bullet3 = new MissileBullet3(world_engine, Uid.generateUid("vid:bullet", "MissileBullet3"), "MissileBullet3", (Weapon2D)getOwner(), getLocation().copy(), 600f);
			bullet3.getBody().setLinearVelocity(new Vector2(600, -200));
			world_engine.insert(bullet3);
			region.queueEntityForAddition(bullet3);
		}
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttached(IdentifiedResource owner) {
		getStereoTrack("missile").setVolume(0.3f).play();
	}
}
