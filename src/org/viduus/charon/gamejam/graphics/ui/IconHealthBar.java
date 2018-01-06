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
public class IconHealthBar {

	private static final int WIDTH = 96;
	private static final int BORDER_SIZE = 3;
	
	private final Animation<?>
		full,
		empty;
	
	public IconHealthBar(AbstractGameSystems game_systems) {
		full = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icon_health_bar.full");
		empty = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/icon_health_bar.empty");
	}
	
	public void render(OpenGLGraphics graphics, float d_sec, PlayerParty players) {
		
	}
	
}
