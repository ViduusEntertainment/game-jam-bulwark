package org.viduus.charon.gamejam.physics.twodimensional.listeners;

import org.dyn4j.collision.Collidable;
import org.dyn4j.collision.Fixture;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;

public class BoundsListener implements org.dyn4j.collision.BoundsListener {

	private final AbstractWorldEngine world_engine;
	
	public BoundsListener(AbstractWorldEngine world_engine) {
		this.world_engine = world_engine;
	}
	
	@Override
	public <E extends Collidable<T>, T extends Fixture> void outside(E collidable) {
		Object2D object = (Object2D) collidable.getUserData();
		world_engine.event_engine.queueEvent(object, new ObjectRemovalEvent(object), ObjectRemovalEvent.class);
	}

}
