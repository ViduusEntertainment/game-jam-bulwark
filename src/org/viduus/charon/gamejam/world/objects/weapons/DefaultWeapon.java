package org.viduus.charon.gamejam.world.objects.weapons;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.world.objects.weapons.bullets.DefaultBullet;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.RangeWeapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class DefaultWeapon extends RangeWeapon2D {

	public DefaultWeapon(AbstractWorldEngine world_engine, String name, Character2D owner) {
		super(world_engine, name, owner, owner.getLocation().copy().add(new Vector2(20, 0)), "vid:animation:eday_robot", "robot");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Bullet2D createBullet() {
		OutputHandler.println(this.getOwner().getLocation().copy().toString());
		Vector2 yep = this.getOwner().getLocation().copy().add(50, 0);
		OutputHandler.println(yep.toString());
		return new DefaultBullet(world_engine, Uid.generateUid("vid:bullet", "DefaultBullet"), "DefaultBullet", this, yep, new Vector2(100, 0));
	}

	@Override
	protected Joint getJoint(Object2D owner) {
		AABB owner_aabb =  owner.getBody().createAABB();
		Vector2 anchor = new Vector2(owner_aabb.getMaxX(), (owner_aabb.getMaxY() - owner_aabb.getMinY()) / 2);
		WeldJoint weld_joint = new WeldJoint(owner.getBody(), this.getBody(), anchor);
		weld_joint.setCollisionAllowed(false);
		
		return weld_joint;
	}

}
