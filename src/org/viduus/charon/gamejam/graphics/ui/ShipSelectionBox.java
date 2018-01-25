/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 8, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;

/**
 * 
 *
 * @author Ethan Toney
 */
public class ShipSelectionBox extends UIElement {

	private boolean
		is_purchased = false,
		is_equipped = false;
	private String
		description,
		name;
	private int
		hearts,
		speed,
		armor_slots,
		price;
	private Animation<?>
		heart_animation,
		speed_animation,
		armor_animation,
		money,
		check_on,
		check_off;

	public ShipSelectionBox(String name, String description, int hearts, int speed, int armor_slots, int price) {
		this.name = name;
		this.description = description;
		this.hearts = hearts;
		this.speed = speed;
		this.armor_slots = armor_slots;
		this.price = price;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#render(org.viduus.charon.global.graphics.opengl.OpenGLGraphics, float)
	 */
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		int ds = OpenGLFont.getLineHeight() - 3;
		int dr = OpenGLFont.getLineHeight() - 2;
		int iw = 18;
		int left = getX() + 4;
		int top = getY() + 5;
		
		Animation<?> check_anim = (is_equipped) ? check_on : check_off;
		check_anim.renderAnimation(graphics, d_sec, left + 7, top + 5, 1);
		OpenGLFont.drawString2D(graphics, name, left + 17, top - 1 + ds);
		OpenGLFont.drawString2D(graphics, description, left, top + dr + ds);
		
		heart_animation.renderAnimation(graphics, d_sec, left, top + 2*dr, 1);
		OpenGLFont.drawString2D(graphics, ""+hearts, left+iw, top + 2*dr + ds + 2);
		
		left += 28;
		speed_animation.renderAnimation(graphics, d_sec, left + 8, top + 2*dr + 8, 1);
		OpenGLFont.drawString2D(graphics, ""+speed, left+iw, top + 2*dr + ds + 2);
		
		left += 28;
		armor_animation.renderAnimation(graphics, d_sec, left + 8, top + 2*dr + 8, 1);
		OpenGLFont.drawString2D(graphics, ""+armor_slots, left+iw, top + 2*dr + ds + 2);
		updateSize();
		
		if (hasFocus()) {
			renderColoredSquare(graphics, getX()+1, getY()+1, getWidth()-1, getHeight()-1, 0.1333333333f, 0.1254901961f, 0.2039215686f, 0.7f);
			renderSelect(graphics, d_sec, getX(), getY(), getWidth(), getHeight());

			if (!is_purchased) {
				renderTextAndAnimationScreen(graphics, d_sec, x, y, width, height, "Unlock", "x", price, money);
			}
		}
	}
	
	public void equip() {
		is_equipped = true;
	}
	
	public void unequip() {
		is_equipped = false;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		heart_animation = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.heart_full");
		speed_animation = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.thrusters_on");
		armor_animation = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icons.armor_on");
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
		game_systems.graphics_engine.release(heart_animation);
		game_systems.graphics_engine.release(speed_animation);
		game_systems.graphics_engine.release(armor_animation);
		game_systems.graphics_engine.release(check_on);
		game_systems.graphics_engine.release(check_off);
		game_systems.graphics_engine.release(money);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#updateSize()
	 */
	@Override
	protected void updateSize() {
		setSize(87, 46);
	}
	
	public void setPurchased() {
		is_purchased = true;
	}

	/**
	 * @return
	 */
	public String getShipName() {
		return name.replaceAll(" ", "");
	}
}
