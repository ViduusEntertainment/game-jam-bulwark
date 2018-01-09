package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;
import org.viduus.charon.global.physics.twodimensional.filters.Bullet2DFilter;

public class WorldFilter implements Filter {

	@Override
	public boolean isAllowed(Filter filter) {
		if (filter instanceof EnemyFilter || filter instanceof Bullet2DFilter)
			return false;
		return true;
	}

}
