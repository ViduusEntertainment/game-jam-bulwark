package org.viduus.charon.gamejam.world.objects.weapons.range;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.objects.weapons.bullets.BossLaserBeam;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class BossLaserGun extends Gun {

	public BossLaserGun(AbstractWorldEngine world_engine, String name, Character2D owner) {
		super(world_engine, name, owner, 1f, 0, Integer.MAX_VALUE);
	}

	@Override
	protected Bullet2D createBullet() {
		return null;
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override 
	protected void beforeBodyCreation() {
		Object2D owner = getOwner();
		Vector2 location = owner.getVector2(Property.LOCATION).copy().add(-100, -30);
		set(Property.LOCATION, location);
	}
	
	public Bullet2D createBullet(Vector2 location, Vector2 velocity, double rotation) {
		return new BossLaserBeam(world_engine, Uid.generateUid("vid:bullet", "BossLaserBeam"), "BossLaserBeam", this, location, velocity, get(Property.DAMAGE), rotation);
	}
}
