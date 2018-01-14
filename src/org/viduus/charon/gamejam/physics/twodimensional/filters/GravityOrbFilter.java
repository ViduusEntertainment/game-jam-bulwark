package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;
import org.viduus.charon.global.physics.twodimensional.filters.Bullet2DFilter;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class GravityOrbFilter extends Bullet2DFilter {

	public GravityOrbFilter(Bullet2D bullet) {
		super(bullet);
	}

	@Override
	protected boolean collisionAllowed(Filter filter) {
		if (filter instanceof EnemyFilter || filter instanceof GravityOrbFilter) {
			return false;
		} 
		return super.collisionAllowed(filter);
	}
}
