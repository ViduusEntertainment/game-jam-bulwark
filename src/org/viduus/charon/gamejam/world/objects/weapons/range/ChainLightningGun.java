package org.viduus.charon.gamejam.world.objects.weapons.range;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.objects.weapons.bullets.ChainLightningBullet;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class ChainLightningGun extends Gun {

	public ChainLightningGun(AbstractWorldEngine world_engine, String name, Character2D owner, float damage) {
		super(world_engine, name, owner, .5f, damage, Integer.MAX_VALUE);
	}

	@Override
	protected Bullet2D createBullet() {
		return new ChainLightningBullet(world_engine, Uid.generateUid("vid:bullet", "ChainLightningBullet"), "ChainLightningBullet", this, getLocation().copy(), get(Property.DAMAGE), 0);
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
}
