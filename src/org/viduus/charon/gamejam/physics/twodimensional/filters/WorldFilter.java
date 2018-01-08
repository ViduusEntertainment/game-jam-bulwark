package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;

public class WorldFilter implements Filter {

	@Override
	public boolean isAllowed(Filter filter) {
		if (filter instanceof EnemyFilter || filter instanceof BulletFilter)
			return false;
		return true;
	}

}
