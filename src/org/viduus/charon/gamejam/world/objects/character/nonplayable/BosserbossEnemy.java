package org.viduus.charon.gamejam.world.objects.character.nonplayable;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.audio.AudioEngine;
import org.viduus.charon.gamejam.world.WorldEngine;
import org.viduus.charon.gamejam.world.objects.effects.Explosion;
import org.viduus.charon.gamejam.world.objects.weapons.range.BossLaserGun;
import org.viduus.charon.gamejam.world.objects.weapons.range.BossScatterGun;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;
import org.viduus.charon.global.world.regions.BaseRegion;
import org.viduus.charon.global.world.util.CooldownTimer;

public class BosserbossEnemy extends Enemy {

	private static final float SPEED = 100.0f;
	private static final float HEALTH = 10000;
	private CooldownTimer immunity_timer = new CooldownTimer(Float.MAX_VALUE);
	private CooldownTimer laser_timer = new CooldownTimer(Float.MAX_VALUE);
	private CooldownTimer primary_timer = new CooldownTimer(0.5f);
	private CooldownTimer wait_timer = new CooldownTimer(3f);
	private Animation<?> laser_animation;
	private Animation<?> shooting_animation;
	
	public BosserbossEnemy(AbstractWorldEngine world_engine, String name, Vector2 location) {
		super(world_engine, name, location, SPEED, HEALTH, 0, HEALTH, 0, "vid:animation:enemies/boss", "boss", "idle", 0);
		set(Property.AUTO_SPRITE_UPDATE, true);
		wait_timer.reset();
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		laser_timer.update(tick_event.time_elapsed);	
		immunity_timer.update(tick_event.time_elapsed);
		wait_timer.update(tick_event.time_elapsed);
		primary_timer.update(tick_event.time_elapsed);
		
		if (!wait_timer.isCooling()) {
			getBody().setLinearVelocity(new Vector2(getFloat(Property.SPEED) * -1, 0));
			wait_timer.setCooldown(Float.MAX_VALUE);
			wait_timer.reset();
			AudioEngine.LEVEL1_TRACK.stop();
			AudioEngine.BOSS_BATTLE_TRACK.play();
			laser_timer.setCooldown(10f);
			laser_timer.reset();
			immunity_timer.setCooldown(0.1f);
			immunity_timer.reset();
			set(Property.CURRENT_ANIMATION, shooting_animation);
		}
		
		if (getVector2(Property.LOCATION).distanceSquared(getVector2(Property.INITIAL_LOCATION)) > 62500) {
			Size world_size = ((WorldEngine)world_engine).getWorldSize();
			set(Property.MOVEMENT_TYPE, NPC_MOVEMENT.FREE_ROAM);
			set(Property.FREE_ROAM_BOUNDS, new AABB(world_size.width / 2, 125, world_size.width, world_size.height - 125));
		}
	}
	
	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		if (!immunity_timer.isCooling()) {
			set(Property.HEALTH, getFloat(Property.HEALTH) - hit_by_weapon_event.weapon.getFloat(Property.DAMAGE));
			immunity_timer.reset();
			
			if (getFloat(Property.HEALTH) <= 0) {
				Explosion explosion = new Explosion(world_engine, "Explosion", getLocation().copy().add(-40, -20));
				world_engine.insert(explosion);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(explosion);
				
				Explosion explosion2 = new Explosion(world_engine, "Explosion", getLocation().copy().add(20, -40));
				world_engine.insert(explosion2);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(explosion2);
				
				Explosion explosion3 = new Explosion(world_engine, "Explosion", getLocation().copy().add(20, 20));
				world_engine.insert(explosion3);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(explosion3);
				
				Explosion explosion4 = new Explosion(world_engine, "Explosion", getLocation().copy().add(-30, 50));
				world_engine.insert(explosion4);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(explosion4);
			}
		}
	}
	
	@Override
	public void determineActiveAnimation() {
		if (shooting_animation == null) {
			laser_animation = ((Animation<?>) world_engine.resolve("vid:animation:enemies/boss.boss-laser")).copy();
			shooting_animation = ((Animation<?>) world_engine.resolve("vid:animation:enemies/boss.boss-shooting")).copy();
			laser_animation.start();
			shooting_animation.start();
		}
		
		Animation<?> curr_animation = get(Property.CURRENT_ANIMATION);
		if (curr_animation != null) {
			if (curr_animation.animationIsFinished()) {
				if (curr_animation == laser_animation) {
					world_engine.event_engine.queueEvent(this, new WeaponUseEvent(getWeapons().get(1)), WeaponUseEvent.class);
					shooting_animation.start();
					set(Property.CURRENT_ANIMATION, shooting_animation);
				} else if (curr_animation == shooting_animation) {
					if (!laser_timer.isCooling()) {
						laser_animation.start();
						set(Property.CURRENT_ANIMATION, laser_animation);
						laser_timer.reset();
					}
					else {
						shooting_animation.start();
					}
				}
			} else if (curr_animation == shooting_animation && shooting_animation.getCurrentFrameID() == 4 && !primary_timer.isCooling()) {
				world_engine.event_engine.queueEvent(this, new WeaponUseEvent(getWeapons().get(0)), WeaponUseEvent.class);
				primary_timer.reset();
			}
		}
	}
	
	@Override
	public void onWeaponUse(WeaponUseEvent weapon_use_event) {
		List<Bullet2D> bullets = new ArrayList<>();
		
		if (weapon_use_event.weapon instanceof BossLaserGun) {
			List<IdentifiedResource> characters = world_engine.collect("vid:character:*");
			if (characters.size() > 0) {
				IdentifiedResource player = characters.get(0);
				Vector2 direction = player.getVector2(Property.LOCATION).subtract(weapon_use_event.weapon.getLocation());
				direction.normalize();
				Vector2 velocity = direction.multiply(500);
				double rotation = weapon_use_event.weapon.getLocation().getAngleBetween(player.getVector2(Property.LOCATION));
				for (int i = 0; i < 10; i++) {
					Bullet2D bullet = ((BossLaserGun)weapon_use_event.weapon).createBullet(new Vector2(weapon_use_event.weapon.getLocation().x - i * 32, weapon_use_event.weapon.getLocation().y), velocity, rotation);
					bullets.add(bullet);
				}
			}
		} else {
			BossScatterGun weapon = (BossScatterGun) weapon_use_event.weapon;
			bullets.add(weapon.createBullet(new Vector2(-600, -200)));
			bullets.add(weapon.createBullet(new Vector2(-600, 0)));
			bullets.add(weapon.createBullet(new Vector2(-600, 200)));
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			world_engine.insert(bullets.get(i));
			this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullets.get(i));
		}
	}
	
	@Override
	protected void onCollision(CollisionEvent collision_event) {}
	
	@Override
	public void onAttached(IdentifiedResource owner) {
		AudioEngine.BOSS_SCREECH_TRACK.setVolume(0.3f).play();
	}
}
