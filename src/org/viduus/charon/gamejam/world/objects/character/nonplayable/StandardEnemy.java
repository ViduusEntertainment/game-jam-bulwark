package org.viduus.charon.gamejam.world.objects.character.nonplayable;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.util.CooldownTimer;

public class StandardEnemy extends Enemy {

	private static final float SPEED = 100.0f;
	private static final float HEALTH = 200;
	private final CooldownTimer weapon_timer = new CooldownTimer(3f);
	private Animation<?> normal_animation;
	private Animation<?> shooting_animation;
	
	public StandardEnemy(AbstractWorldEngine world_engine, String name, Vector2 location) {
		super(world_engine, name, location, SPEED, HEALTH, 0, HEALTH, 0, "vid:animation:enemies/enemies", "normal", "walk_l", 100);
		set(Property.AUTO_SPRITE_UPDATE, true);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		weapon_timer.update(tick_event.time_elapsed);	
	}
	
	@Override
	public void determineActiveAnimation() {
		if (shooting_animation == null && normal_animation == null) {
			shooting_animation = ((Animation<?>) world_engine.resolve("vid:animation:enemies/enemies.normal-shooting")).copy();
			normal_animation = ((Animation<?>) world_engine.resolve("vid:animation:enemies/enemies.normal-walk_l")).copy();
			set(Property.CURRENT_ANIMATION, normal_animation);
			normal_animation.start();
			shooting_animation.start();
		}

		if (!weapon_timer.isCooling() && normal_animation.animationIsFinished()) {
			set(Property.CURRENT_ANIMATION, shooting_animation);
			shooting_animation.start();
			normal_animation.start();
		} else if (shooting_animation.animationIsFinished()) {
			set(Property.CURRENT_ANIMATION, normal_animation);
			shooting_animation.start();
			normal_animation.start();
			world_engine.queueEvent(this, new WeaponUseEvent(getWeapons().get(0)), WeaponUseEvent.class);
			weapon_timer.reset();
		}	
	}
}
