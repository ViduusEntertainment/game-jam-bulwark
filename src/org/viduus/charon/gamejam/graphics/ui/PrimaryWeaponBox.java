/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 8, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;

/**
 * 
 *
 * @author Ethan Toney
 */
public class PrimaryWeaponBox extends UIElement {

	private static final int MAX_LEVEL = 5;
	
	private final String upgrade_text;
	private boolean
		is_purchased = false,
		is_equipped = false;
	private Animation<?>
		animation_on,
		animation_off,
		check_on,
		check_off,
		money;
	private int
		level = 0,
		purchase_cost;
	private int[] upgrade_cost;

	public PrimaryWeaponBox(String upgrade_text, int purchase_cost, int[] upgrade_cost) {
		this.upgrade_text = upgrade_text;
		this.upgrade_cost = upgrade_cost;
		this.purchase_cost = purchase_cost;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setPurchased() {
		is_purchased = true;
	}
	
	public boolean getPurchased() {
		return is_purchased;
	}
	
	public int getNextPrice() {
		return upgrade_cost[level];
	}
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#render(org.viduus.charon.global.graphics.opengl.OpenGLGraphics, float)
	 */
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		updateSize();
		int i=0;
		for (; i<level ; i++) {
			animation_on.renderAnimation(graphics, d_sec, getX()+9 + 16*i, getY()+8+17, 1);
		}
		for (; i<MAX_LEVEL ; i++) {
			animation_off.renderAnimation(graphics, d_sec, getX()+9 + 16*i, getY()+8+17, 1);
		}
		updateSize();
		Animation<?> check_anim = (is_equipped) ? check_on : check_off;
		check_anim.renderAnimation(graphics, d_sec, getX() + 9, getY() + 9, 1);
		OpenGLFont.drawString2D(graphics, upgrade_text, getX()+19, getY()+OpenGLFont.getLineHeight());
		
		if (hasFocus()) {
			renderColoredSquare(graphics, getX()+1, getY()+1, getWidth()-1, getHeight()-1, 0.1333333333f, 0.1254901961f, 0.2039215686f, 0.7f);
			renderSelect(graphics, d_sec, getX(), getY(), getWidth(), getHeight());
			
			if (level < 5) {
				if (!is_purchased) {
					renderTextAndAnimationScreen(graphics, d_sec, x, y, width, height, "Unlock", "x", purchase_cost, money);
				} else {
					renderTextAndAnimationScreen(graphics, d_sec, x, y, width, height, "Upgrade", "x", upgrade_cost[level], money);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		animation_on = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.select_on");
		animation_off = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.select_off");
		check_on = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.check_on");
		check_off = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.check_off");
		money = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.money");
		updateSize();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onDeactivate(AbstractGameSystems game_systems) {
		game_systems.graphics_engine.release(animation_on);
		game_systems.graphics_engine.release(animation_off);
		game_systems.graphics_engine.release(check_on);
		game_systems.graphics_engine.release(check_off);
		game_systems.graphics_engine.release(money);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#updateSize()
	 */
	@Override
	protected void updateSize() {
		setSize((int) Math.max(22+OpenGLFont.getStringWidth(upgrade_text), 16*5), 32);
	}

	/**
	 * @return
	 */
	public String getName() {
		return upgrade_text.replaceAll(" ", "");
	}

	public void equip() {
		is_equipped  = true;
	}
	
	public void unequip() {
		is_equipped = false;
	}
	
}
