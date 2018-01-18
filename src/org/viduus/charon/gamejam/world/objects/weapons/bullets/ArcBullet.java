package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import java.util.HashSet;
import java.util.List;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.physics.twodimensional.filters.ArcBulletFilter;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class ArcBullet extends Bullet{
	
	private int level = 0;
	
	public ArcBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, float damage, double rotation, int level, Enemy non_collidable) {
		this(world_engine, uid, name, owner, location, new Vector2(800, 0), damage, rotation, level, non_collidable);
	}
	
	public ArcBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, Vector2 velocity, float damage, double rotation, int level, Enemy non_collidable) {
		super(world_engine, uid, name, owner, location, velocity, "vid:animation:objects/bullets", "chain_lightning", damage);
		this.level = level;
		set(Property.ROTATION, rotation);
		set(Property.COLLISION_FILTER, new ArcBulletFilter(this, non_collidable));
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttached(IdentifiedResource owner) {
		
	}
	
	@Override
	public void onCollision(CollisionEvent collision_event) {
		super.onCollision(collision_event);
		
		if (level < 1 && collision_event.object2D instanceof Enemy) {
			List<IdentifiedResource> enemies = world_engine.collect("vid:npc:*");
			HashSet<IdentifiedResource> enemies_set = new HashSet<>();
			enemies_set.addAll(enemies);
			enemies_set.remove(collision_event.object2D);
			enemies.clear();
			enemies.addAll(enemies_set);
			enemies.sort((o1, o2) ->  { 
				if (o1.getVector2(Property.LOCATION).distanceSquared(collision_event.object2D.getLocation()) < o2.getVector2(Property.LOCATION).distanceSquared(collision_event.object2D.getLocation()))
					return -1; 
				else
					return 1;
			});
			for (int i = 0; i < 3; i++) {
				if (enemies.size() > i) {
					Vector2 direction = enemies.get(i).getVector2(Property.LOCATION).subtract(collision_event.object2D.getLocation());
					direction.normalize();
					Vector2 velocity = direction.multiply(600);
					double rotation = collision_event.object2D.getLocation().getAngleBetween(enemies.get(i).getVector2(Property.LOCATION));
					ArcBullet bullet = new ArcBullet(world_engine, Uid.generateUid("vid:bullet", "ChainLightningBullet"), "ChainLightningBullet", (Weapon2D)getOwner(), collision_event.object2D.getLocation().copy(), velocity, get(Property.DAMAGE), rotation, level + 1, (Enemy)collision_event.object2D);
					world_engine.insert(bullet);
					this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullet);
				}
			}
		}
	}
}
