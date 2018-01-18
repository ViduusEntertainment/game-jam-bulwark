/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.hud.components;

import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.gamejam.world.regions.Level1;
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

	private static final int WIDTH = 180;
	private static final int BORDER_SIZE = 3;
	
	private final Animation<?>
		outside_left,
		outside_middle,
		outside_right,
		inside_left,
		inside_middle_repeat_1,
		inside_middle_repeat_2,
		inside_right,
		inside_back;
	
	public FillInHealthBar(AbstractGameSystems game_systems) {
		outside_left = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.outside_left");
		outside_middle = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.outside_middle");
		outside_right = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.outside_right");
		inside_left = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_left");
		inside_middle_repeat_1 = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_middle_repeat_1");
		inside_middle_repeat_2 = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_middle_repeat_2");
		inside_right = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_right");
		inside_back = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/fill_in_health_bar.inside_back");
	}
	
	public void render(OpenGLGraphics graphics, int x, int y, float d_sec, PlayerParty players) {
		PlayerCharacter character = (PlayerCharacter) players.get(0);
		
		x -= WIDTH/2;
		
		/*
		 * Draw the inside of the health bar
		 */
		
		float percent_health = ((Level1)character.getCurrentRegion()).getPercentEnemyHealth();
		
		int max_pixels = WIDTH - 2*BORDER_SIZE;
		int pixels = (int) Math.floor(percent_health * max_pixels);
		int count = 0;
		
		// draw left side
		if (pixels >= 1) {
			inside_left.renderAnimation(graphics, d_sec, x + BORDER_SIZE+count, y, 1);
			count++;
		}
		
		// draw middle
		while (pixels-count > 1) {
			if (count % 2 == 0) {
				inside_middle_repeat_1.renderAnimation(graphics, d_sec, x + BORDER_SIZE+count, y, 1);
			} else {
				inside_middle_repeat_2.renderAnimation(graphics, d_sec, x + BORDER_SIZE+count, y, 1);
			}
			count++;
		}
		
		// draw right
		if (pixels-count == 1) {
			inside_right.renderAnimation(graphics, d_sec, x + BORDER_SIZE+count, y, 1);
		}
		
		// draw back
		while (pixels < max_pixels) {
			inside_back.renderAnimation(graphics, d_sec, x + BORDER_SIZE+pixels, y, 1);
			pixels++;
		}
		
		/*
		 * Draw the outside of the health bar
		 */
		
		count = 0;
		
		// draw left side
		outside_left.renderAnimation(graphics, d_sec, x + count, y, 1);
		count += 32;
		
		// draw middle
		while (WIDTH - count > 32) {
			outside_middle.renderAnimation(graphics, d_sec, x + count, y, 1);
			count += 32;
		}
		
		// draw right
		outside_right.renderAnimation(graphics, d_sec, x + WIDTH-32, y, 1);
	}
	
}
