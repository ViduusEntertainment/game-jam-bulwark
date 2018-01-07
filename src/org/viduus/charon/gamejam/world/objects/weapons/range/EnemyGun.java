package org.viduus.charon.gamejam.world.objects.weapons.range;

import org.viduus.charon.gamejam.world.objects.weapons.bullets.EnemyBullet;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class EnemyGun extends Gun {

	public EnemyGun(AbstractWorldEngine world_engine, String name, Character2D owner) {
		super(world_engine, name, owner, 1f);
	}

	@Override
	public Bullet2D createBullet() {
		return new EnemyBullet(world_engine, Uid.generateUid("vid:bullet", "EnemyBullet"), "EnemyBullet", this, getLocation().copy());
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

}
