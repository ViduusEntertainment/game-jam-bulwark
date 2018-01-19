package org.viduus.charon.gamejam.physics.twodimensional.filters;

import org.dyn4j.collision.Filter;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.global.world.objects.twodimensional.weapon.range.bullets.Bullet2D;

public class ArcBulletFilter extends FriendlyBulletFilter {

	public final Enemy non_collidable;
	
	public ArcBulletFilter(Bullet2D bullet, Enemy non_collidable) {
		super(bullet);
		
		this.non_collidable = non_collidable;
	}

	@Override
	protected boolean collisionAllowed(Filter filter) {
		if (filter instanceof EnemyFilter) {
			if (((EnemyFilter)filter).getObject() == non_collidable) {
				return false;
			}
		}
		return super.collisionAllowed(filter);
	}
}
