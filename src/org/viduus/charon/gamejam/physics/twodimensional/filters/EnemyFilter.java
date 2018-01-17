package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.global.physics.twodimensional.filters.Character2DFilter;

public class EnemyFilter extends Character2DFilter<Enemy> implements Filter {

	public EnemyFilter(Enemy object) {
		super(object);
	}

	@Override
	protected boolean collisionAllowed(Filter filter) {
		if (filter instanceof WorldFilter || filter instanceof EnemyBulletFilter || filter instanceof GravityOrbFilter) 
			return false;
		else if (filter instanceof ArcBulletFilter) {
			if (((ArcBulletFilter)filter).non_collidable == getObject()) {
				return false;
			}
		}
		return super.collisionAllowed(filter);
	}
}
