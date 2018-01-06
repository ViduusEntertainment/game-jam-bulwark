/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

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
public class FillInHealthBar {

	private static final int WIDTH = 96;
	private static final int BORDER_SIZE = 3;
	
	private final Animation<?>
		outside_left,
		outside_middle,
		outside_right,
		inside_left,
		inside_middle_repeat_1,
		inside_middle_repeat_2,
		inside_right;
	
	public FillInHealthBar(AbstractGameSystems game_systems) {
		outside_left = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.outside_left");
		outside_middle = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.outside_middle");
		outside_right = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.outside_right");
		inside_left = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_left");
		inside_middle_repeat_1 = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_middle_repeat_1");
		inside_middle_repeat_2 = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_middle_repeat_2");
		inside_right = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_right");
	}
	
	public void render(OpenGLGraphics graphics, float d_sec, PlayerParty players) {
		PlayableCharacter2D character = players.get(0);
		
		/*
		 * Draw the inside of the health bar
		 */
		
		float percent_health = character.getFloat(Property.PERCENT_HEALTH);
		
		int pixels = (int) Math.floor(percent_health * (WIDTH - 2*BORDER_SIZE));
		int count = 0;
		
		// draw left side
		if (pixels >= 1) {
			inside_left.renderAnimation(graphics, d_sec, BORDER_SIZE+count, 0, 1);
			count++;
		}
		
		// draw middle
		while (pixels-count > 2) {
			if (count % 2 == 0) {
				inside_middle_repeat_1.renderAnimation(graphics, d_sec, BORDER_SIZE+count, 0, 1);
			} else {
				inside_middle_repeat_2.renderAnimation(graphics, d_sec, BORDER_SIZE+count, 0, 1);
			}
			count++;
		}
		
		// draw right
		if (pixels-count == 2) {
			inside_right.renderAnimation(graphics, d_sec, BORDER_SIZE+count, 0, 1);
		}
		
		/*
		 * Draw the outside of the health bar
		 */
		
		count = 0;
		
		// draw left side
		outside_left.renderAnimation(graphics, d_sec, 0+count, 0, 1);
		count += 32;
		
		// draw middle
		while (WIDTH - count > 32) {
			outside_middle.renderAnimation(graphics, d_sec, 0+count, 0, 1);
			count += 32;
		}
		
		// draw right
		outside_right.renderAnimation(graphics, d_sec, WIDTH-32, 0, 1);
	}
	
}
