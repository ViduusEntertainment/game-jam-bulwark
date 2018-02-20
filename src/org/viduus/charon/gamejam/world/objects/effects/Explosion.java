package org.viduus.charon.gamejam.world.objects.effects;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.StaticObject2D;

public class Explosion extends StaticObject2D {

	public Explosion(AbstractWorldEngine world_engine, String name, Vector2 location) {
		super(world_engine, Uid.generateUid("vid:effect", name), name, location, "vid:animation:objects/effects", "explosion", "idle", false);
	}
	 
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		Animation<?> current_animation = get(Property.CURRENT_ANIMATION);
		if (current_animation != null && current_animation.animationIsFinished()) {
			set(Property.CURRENT_ANIMATION, null);
			world_engine.event_engine.queueEvent(this, new ObjectRemovalEvent(this), ObjectRemovalEvent.class);
		}
	}

	@Override
	protected void beforeBodyCreation() {}

	@Override
	protected void onCollision(CollisionEvent collision_event) {}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {}

	@Override
	protected void registerEvents() {}
	
	@Override
	public void onReleased() {}
	
	@Override
	public void onAttached(IdentifiedResource owner) {
		getMonoTrack("explosion").setVolume(0.3f).play();
	}
	
	@Override
	public void onDetached(IdentifiedResource owner) {}
}
