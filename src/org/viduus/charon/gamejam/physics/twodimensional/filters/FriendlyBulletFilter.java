package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;
import org.viduus.charon.global.physics.twodimensional.filters.Bullet2DFilter;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class FriendlyBulletFilter extends Bullet2DFilter {

	public FriendlyBulletFilter(Bullet2D bullet) {
		super(bullet);
	}

	@Override
	protected boolean collisionAllowed(Filter filter) {
		if (filter instanceof WorldFilter) 
			return false;
		return super.collisionAllowed(filter);
	}
}
