/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.hud.components;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.player.PlayerParty;
import org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D;

/**
 * 
 *
 * @author Ethan Toney
 */
public class IconHealthBar {
	
	private final Animation<?>
		full,
		empty;
	private final int num_icons;
	
	public IconHealthBar(int num_icons, AbstractGameSystems game_systems) {
		this.num_icons = num_icons;
		full = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icon_health_bar.full");
		empty = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icon_health_bar.empty");
	}
	
	public void render(OpenGLGraphics graphics, int x, int y, float d_sec, PlayerParty players) {
		PlayableCharacter2D character = players.get(0);
		
		float percent_health = character.getFloat(Property.PERCENT_HEALTH);
		int full_icons = (int) Math.ceil(percent_health * num_icons);
		
		int count = 0;
		
		while (count < full_icons) {
			full.renderAnimation(graphics, d_sec, x + 2+17*count, y, 1);
			count++;
		}
		while (count < num_icons) {
			empty.renderAnimation(graphics, d_sec, x + 2+17*count, y, 1);
			count++;
		}
	}
	
}
