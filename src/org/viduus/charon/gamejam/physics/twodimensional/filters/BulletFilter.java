package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;
import org.viduus.charon.global.physics.twodimensional.filters.Bullet2DFilter;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class BulletFilter extends Bullet2DFilter {

	public BulletFilter(Bullet2D bullet) {
		super(bullet);
	}

	@Override
	protected boolean collisionAllowed(Filter filter) {
		if (filter instanceof WorldFilter || filter instanceof EnemyFilter)
			return false;
		else
			return super.collisionAllowed(filter);
	}
}
