package org.viduus.charon.gamejam.world.objects.character.nonplayable;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.regions.Level1;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;

public class KamikazeEnemy extends Enemy{

	private static final float SPEED = 200.0f;
	private static final float HEALTH = 100.0f;
	private static final float STOP_DISTANCE = 9000.0f; // this is squared
	private boolean sound_played = false;
	
	public KamikazeEnemy(AbstractWorldEngine world_engine, String name, Vector2 location, Object2D followee) {
		super(world_engine, name, location, SPEED, HEALTH, 0, HEALTH, 0, "vid:animation:enemies/enemies", "kamikaze", "walk_l", 50);
		set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.OBJECT_FOLLOWING);
		setFollowee(followee, 0.0f);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		Object2D followee = this.<Object2D>get(Property.FOLLOWEE);
		if (!sound_played && followee.getLocation().distanceSquared(getLocation()) < STOP_DISTANCE) {
			set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.MANUAL);
			Level1.KAMIKAZE_SOUND.play(0.1);
			sound_played = true;
		}
		super.onTick(tick_event);
	}
}
