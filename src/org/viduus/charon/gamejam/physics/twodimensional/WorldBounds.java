package org.viduus.charon.gamejam.physics.twodimensional;

import org.dyn4j.collision.AbstractBounds;
import org.dyn4j.collision.Collidable;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.util.logging.OutputHandler;

public class WorldBounds extends AbstractBounds {

	private final AABB aabb;
	
	public WorldBounds(AABB aabb) {
		this.aabb = aabb;
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.collision.Bounds#isOutside(org.dyn4j.collision.Collidable)
	 */
	@Override
	public boolean isOutside(Collidable<?> collidable) {
		AABB aabbBounds = this.aabb;
		AABB aabbBody = collidable.createAABB(collidable.getTransform());
		
		if (aabbBounds.overlaps(aabbBody)) {
			return false;
		}
		
		return true;
	}

}
