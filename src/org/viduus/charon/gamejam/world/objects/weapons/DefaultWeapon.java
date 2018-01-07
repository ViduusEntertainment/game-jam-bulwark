package org.viduus.charon.gamejam.world.objects.weapons;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.objects.weapons.bullets.DefaultBullet;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.RangeWeapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class DefaultWeapon extends RangeWeapon2D {

	public DefaultWeapon(AbstractWorldEngine world_engine, String name, Character2D owner) {
		super(world_engine, name, owner, owner.getLocation().copy().add(new Vector2(20, 0)), "vid:animation:eday_robot", "robot");
	}

	@Override
	public Bullet2D createBullet() {
		return new DefaultBullet(world_engine, Uid.generateUid("vid:bullet", "DefaultBullet"), "DefaultBullet", this, getLocation().copy().add(30, 0), new Vector2(3, 0));
	}

	@Override
	protected Joint getJoint(Object2D owner) {
		WeldJoint weld_joint = new WeldJoint(owner.getBody(), this.getBody(), owner.getBody().getWorldCenter().copy().add(30, 0));
		weld_joint.setCollisionAllowed(false);
		
		return weld_joint;
	}

}
