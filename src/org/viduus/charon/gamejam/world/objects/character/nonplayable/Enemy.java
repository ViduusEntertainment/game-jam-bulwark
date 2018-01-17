package org.viduus.charon.gamejam.world.objects.character.nonplayable;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.physics.twodimensional.filters.EnemyFilter;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.gamejam.world.objects.effects.Explosion;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.event.events.WeaponUseEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.nonplayable.NonPlayableCharacter2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.RangeWeapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;
import org.viduus.charon.global.world.regions.BaseRegion;
import org.viduus.charon.global.world.util.CooldownTimer;

public abstract class Enemy extends NonPlayableCharacter2D{
	
	private final int reward;
	private CooldownTimer immunity_timer = new CooldownTimer(0.1f);
	
	public Enemy(AbstractWorldEngine world_engine, String name, Vector2 location, float speed, float health, 
			float mana, float max_health, float max_mana, String animation_file, String sprite_id, String animation_name, int reward) {
		super(world_engine, name, location, speed, health, mana, max_health, max_mana, animation_file, sprite_id, animation_name);
		this.reward = reward;
		setCollisionFilter(new EnemyFilter(this));
	}
	
	@Override
	protected void beforeBodyCreation() {
		set(Property.MASS, Double.MAX_VALUE);
		set(Property.LINEAR_DAMPING, 0.0);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		immunity_timer.update(tick_event.time_elapsed);
		super.onTick(tick_event);
	}
	
	public int getReward() {
		return reward;
	}
	
	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		if (!immunity_timer.isCooling()) {
			set(Property.HEALTH, getFloat(Property.HEALTH) - hit_by_weapon_event.weapon.getFloat(Property.DAMAGE));
			immunity_timer.reset();
			
			if (getFloat(Property.HEALTH) <= 0) {
				Explosion explosion = new Explosion(world_engine, "Explosion", getLocation().copy());
				world_engine.insert(explosion);
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(explosion);
			}
		}
	}
	
	@Override
	public void onWeaponUse(WeaponUseEvent weapon_use_event) {
		RangeWeapon2D weapon = (RangeWeapon2D)weapon_use_event.weapon;
		Bullet2D bullet = weapon.getBullet();
		world_engine.insert(bullet);
		this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullet);
	}
	
	@Override
	protected void onCollision(CollisionEvent collision_event) {
		if (collision_event.object2D instanceof PlayerCharacter) {
			world_engine.queueEvent(this, new ObjectRemovalEvent(this), ObjectRemovalEvent.class);
			Explosion explosion = new Explosion(world_engine, "Explosion", getLocation().copy());
			world_engine.insert(explosion);
			this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(explosion);
		}
	}
	
	@Override
	public void onReleased() {}
	
	@Override
	public void onAttached(IdentifiedResource owner) {}
	
	@Override
	public void onDetached(IdentifiedResource owner) {}
}
