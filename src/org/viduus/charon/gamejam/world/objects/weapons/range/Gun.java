package org.viduus.charon.gamejam.world.objects.weapons.range;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.RangeWeapon2D;

public abstract class Gun extends RangeWeapon2D{
	
	public Gun(AbstractWorldEngine world_engine, String name, Character2D owner, float cooldown, int projectile_count) {
		super(world_engine, name, owner, new Vector2(), "vid:animation:objects/blank", "blank", cooldown, projectile_count);
	}
	
	@Override
	protected Joint getJoint(Object2D owner) {
		WeldJoint weld_joint = new WeldJoint(owner.getBody(), this.getBody(), new Vector2());
		weld_joint.setCollisionAllowed(false);
		
		return weld_joint;
	}
}