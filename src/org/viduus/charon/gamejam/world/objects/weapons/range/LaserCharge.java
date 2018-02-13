package org.viduus.charon.gamejam.world.objects.weapons.range;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.audio.AudioEngine;
import org.viduus.charon.gamejam.world.objects.weapons.bullets.LaserBeam;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class LaserCharge extends Gun {

	public LaserCharge(AbstractWorldEngine world_engine, String name, Character2D owner, float damage) {
		super(world_engine, name, owner, "vid:animation:objects/bullets", "laser_charge", 1f, damage, Integer.MAX_VALUE);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		Animation<?> current_animation = get(Property.CURRENT_ANIMATION);
		if (current_animation != null && current_animation.animationIsFinished()) {
			set(Property.CURRENT_ANIMATION, null);
			world_engine.event_engine.queueEvent(this, new ObjectRemovalEvent(this), ObjectRemovalEvent.class);
			
			List<Bullet2D> bullets = new ArrayList<>();
			
			for (int i = 0; i < 10; i++) {
				bullets.add(createBullet(new Vector2(getLocation().x + i * 32, getLocation().y)));
			}
			
			for (int i = 0; i < bullets.size(); i++) {
				world_engine.insert(bullets.get(i));
				this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullets.get(i));
			}
		}
	}
	
	protected Bullet2D createBullet(Vector2 location) {
		return new LaserBeam(world_engine, Uid.generateUid("vid:bullet", "LaserBeam"), "LaserBeam", this, location, get(Property.DAMAGE));
	}

	@Override
	protected Bullet2D createBullet() {
		return new LaserBeam(world_engine, Uid.generateUid("vid:bullet", "LaserBeam"), "LaserBeam", this, getLocation().copy(), get(Property.DAMAGE));
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override 
	protected void beforeBodyCreation() {
		Object2D owner = getOwner();
		Vector2 location = owner.getVector2(Property.LOCATION).copy().add(40, 8);
		set(Property.LOCATION, location);
	}
	
	@Override 
	public void onAttached(IdentifiedResource owner) {
		AudioEngine.LASER_CHARGE_SOUND.play();
	}
}
