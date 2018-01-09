package org.viduus.charon.gamejam.world.objects.character.nonplayable;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.util.CooldownTimer;

public class StandardEnemy extends Enemy{

	private static final float SPEED = 100.0f;
	private static final float HEALTH = 200;
	private final CooldownTimer weapon_timer = new CooldownTimer(3f);
	
	public StandardEnemy(AbstractWorldEngine world_engine, String name, Vector2 location) {
		super(world_engine, name, location, SPEED, HEALTH, 0, HEALTH, 0, "vid:animation:enemies/enemies", "normal", "walk_l", 100);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		weapon_timer.update(tick_event.time_elapsed);
		if (!weapon_timer.isCooling()) {
			world_engine.queueEvent(this, new WeaponUseEvent(getWeapons().get(0)), WeaponUseEvent.class);
			weapon_timer.reset();
		}
	}
}
