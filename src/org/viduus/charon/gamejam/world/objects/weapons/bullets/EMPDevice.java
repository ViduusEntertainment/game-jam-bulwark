package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.audio.AudioEngine;
import org.viduus.charon.gamejam.physics.twodimensional.filters.FriendlyBulletFilter;
import org.viduus.charon.gamejam.world.regions.Level1;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;

public class EMPDevice extends Bullet{
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public EMPDevice(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, float damage) {
		super(world_engine, uid, name, owner, location, new Vector2(0, 0), "vid:animation:objects/effects", "emp", damage);
		set(Property.COLLISION_FILTER, new FriendlyBulletFilter(this));
	}
	
	@Override
	protected Joint getJoint(Object2D owner) {
		WeldJoint weld_joint = new WeldJoint(owner.getBody(), this.getBody(), new Vector2());
		weld_joint.setCollisionAllowed(false);
		
		return weld_joint;
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		if (this.<Animation<?>>get(Property.CURRENT_ANIMATION).animationIsFinished()) {
			set(Property.CURRENT_ANIMATION, null);
			world_engine.event_engine.queueEvent(this, new ObjectRemovalEvent(this), ObjectRemovalEvent.class);
		}
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCollision(CollisionEvent collision_event) {
		getWorldEngine().event_engine.queueEvent(collision_event.object2D, new HitByWeaponEvent(this), HitByWeaponEvent.class);
	}

	@Override
	public void onAttached(IdentifiedResource owner) {
		AudioEngine.EMP_SOUND.play(Level1.SFX);
	}
	
	@Override
	public void onDetached(IdentifiedResource owner) {}
}
