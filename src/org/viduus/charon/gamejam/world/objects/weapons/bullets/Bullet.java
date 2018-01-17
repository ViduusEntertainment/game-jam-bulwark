package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public abstract class Bullet extends Bullet2D {
	
	private float MAX_DISTANCE = 1000;

	public Bullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location,
			Vector2 linear_velocity, String animation_file, String sprite_id, float damage) {
		super(world_engine, uid, name, owner, location, linear_velocity, animation_file, sprite_id, 0, damage);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		if (getVector2(Property.LOCATION).distanceSquared(getVector2(Property.INITIAL_LOCATION)) > MAX_DISTANCE * MAX_DISTANCE) {
			world_engine.queueEvent(this, new ObjectRemovalEvent(this), ObjectRemovalEvent.class);
		}
	}

	@Override
	protected Joint getJoint(Object2D owner) {
		return null;
	}

	@Override
	protected void beforeBodyCreation() {
		set(Property.LINEAR_DAMPING, 0.0);
	}
	
	@Override
	public void onReleased() {}
	
	@Override
	public void onAttached(IdentifiedResource owner) {}
	
	@Override
	public void onDetached(IdentifiedResource owner) {}
}
