package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;

public class EnemyFilter implements Filter {

	@Override
	public boolean isAllowed(Filter filter) {
		if (filter instanceof WorldFilter || filter instanceof BulletFilter) 
			return false;
		return true;
	}
}
