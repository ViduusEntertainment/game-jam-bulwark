package org.viduus.charon.gamejam.world.objects.weapons.range;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.character.Character2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.RangeWeapon2D;

public abstract class Gun extends RangeWeapon2D{

	public Gun(AbstractWorldEngine world_engine, String name, Character2D owner,
			String animation_file, String sprite_id) {
		super(world_engine, name, owner, new Vector2(), animation_file, sprite_id);
	}
	
	@Override
	protected Joint getJoint(Object2D owner) {
		WeldJoint weld_joint = new WeldJoint(owner.getBody(), this.getBody(), new Vector2());
		weld_joint.setCollisionAllowed(false);
		
		return weld_joint;
	}
	
	@Override 
	protected void setPhysicsProperties() {
		Object2D owner = getOwner();
		Size owner_size = owner.<Size>get(Property.SIZE);
		Size size = this.<Size>get(Property.SIZE);
		set(Property.LOCATION, owner.getVector2(Property.LOCATION).copy().add(new Vector2(owner_size.width, -(owner_size.height - size.height))));
	}
}
