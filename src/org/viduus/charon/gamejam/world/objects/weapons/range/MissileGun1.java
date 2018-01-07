package org.viduus.charon.gamejam.world.objects.weapons.range;

import org.viduus.charon.gamejam.world.objects.weapons.bullets.MissileBullet1;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class MissileGun1 extends Gun {

	public MissileGun1(AbstractWorldEngine world_engine, String name, Character2D owner) {
		super(world_engine, name, owner, "vid:animation:objects/bullets", "player_missile_1");
	}

	@Override
	public Bullet2D createBullet() {
		return new MissileBullet1(world_engine, Uid.generateUid("vid:bullet", "MissileBullet1"), "MissileBullet1", this, getLocation().copy());
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

}
