package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class DefaultBullet extends Bullet2D{
	
	/**
	 * @param world_engine
	 * @param name
	 * @param location
	 * @param sprite_map
	 */
	public DefaultBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, Vector2 velocity) {
		super(world_engine, uid, name, owner, location, velocity, "vid:animation:eday_robot", "robot");
	}

	@Override
	protected Joint getJoint(Object2D owner) {
		return null;
	}
}
