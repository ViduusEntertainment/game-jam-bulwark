package org.viduus.charon.gamejam.world.objects.effects;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.event.events.HitByWeaponEvent;
import org.viduus.charon.global.event.events.ObjectRemovalEvent;
import org.viduus.charon.global.event.events.TickEvent;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.util.identification.IdentifiedResource;
import org.viduus.charon.global.util.identification.Uid;
import org.viduus.charon.global.world.AbstractWorldEngine;
import org.viduus.charon.global.world.objects.twodimensional.Object2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;

public class Shield extends Weapon2D {

	public Shield(AbstractWorldEngine world_engine, Uid uid, String name, Object2D owner, float cooldown) {
		super(world_engine, uid, name, owner, owner.getLocation(), "vid:animation:objects/effects", "shield", false, cooldown, Float.MAX_VALUE);
	}
	
	@Override
	public void onTick(TickEvent tick_event) {
		super.onTick(tick_event);
		if (this.<Animation<?>>get(Property.CURRENT_ANIMATION).animationIsFinished()) {
			set(Property.CURRENT_ANIMATION, null);
			world_engine.queueEvent(this, new ObjectRemovalEvent(this), ObjectRemovalEvent.class);
		}
	}

	@Override
	protected Joint getJoint(Object2D owner) {
		WeldJoint weld_joint = new WeldJoint(owner.getBody(), this.getBody(), new Vector2());
		weld_joint.setCollisionAllowed(false);
		
		return weld_joint;
	}

	@Override
	protected void beforeBodyCreation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onHitByWeapon(HitByWeaponEvent hit_by_weapon_event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReleased() {}
	
	@Override
	public void onAttached(IdentifiedResource owner) {}
	
	@Override
	public void onDetached(IdentifiedResource owner) {}
}
