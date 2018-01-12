package org.viduus.charon.gamejam.world.objects.weapons.bullets;

import java.util.List;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.physics.twodimensional.filters.FriendlyBulletFilter;
import org.viduus.charon.gamejam.world.objects.character.nonplayable.Enemy;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.CollisionEvent;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;
import org.viduus.charon.global.world.regions.BaseRegion;

public class ChainLightningBullet extends Bullet{
	
	private int level = 0;
	
	public ChainLightningBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, float damage, int level) {
		super(world_engine, uid, name, owner, location, new Vector2(800, 0), "vid:animation:objects/bullets", "chain_lightning", damage);
		setCollisionFilter(new FriendlyBulletFilter(this));
	}
	
	public ChainLightningBullet(AbstractWorldEngine world_engine, Uid uid, String name, Weapon2D owner, Vector2 location, Vector2 velocity, float damage, int level) {
		super(world_engine, uid, name, owner, location, velocity, "vid:animation:objects/bullets", "chain_lightning", damage);
		setCollisionFilter(new FriendlyBulletFilter(this));
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
		
		if (level < 2 && collision_event.object2D instanceof Enemy) {
			List<IdentifiedResource> enemies = world_engine.collect("vid:npc:*");
			enemies.sort((o1, o2) ->  { 
				if (o1.getVector2(Property.LOCATION).distanceSquared(getLocation()) < o2.getVector2(Property.LOCATION).distanceSquared(getLocation()))
					return -1;
				else
					return 1;
			});
			enemies.remove(collision_event.object2D);
			for (int i = 0; i < 3; i++) {
				if (enemies.size() > i) {
					Vector2 direction = enemies.get(i).getVector2(Property.LOCATION).subtract(collision_event.object2D.getLocation());
					direction.normalize();
					Vector2 velocity = direction.multiply(800);
					ChainLightningBullet bullet = new ChainLightningBullet(world_engine, Uid.generateUid("vid:bullet", "ChainLightningBullet"), "ChainLightningBullet", (Weapon2D)getOwner(), getLocation().copy(), velocity, get(Property.DAMAGE), level + 1);
					world_engine.insert(bullet);
					this.<BaseRegion>get(Property.CURRENT_REGION).queueEntityForAddition(bullet);
				}
			}
		}
	}
}
