/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.ui.HeadsUpDisplay;
import org.viduus.charon.global.player.PlayerParty;

/**
 * 
 *
 * @author Ethan Toney
 */
public class CharacterHUD extends HeadsUpDisplay {

	private final FillInHealthBar health_bar;
	
	/**
	 * @param game_systems
	 */
	public CharacterHUD(AbstractGameSystems game_systems) {
		super(game_systems);
		
		health_bar = new FillInHealthBar(game_systems);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.ui.HeadsUpDisplay#render(org.viduus.charon.global.graphics.opengl.OpenGLGraphics, float, org.viduus.charon.global.player.PlayerParty)
	 */
	@Override
	public void render(OpenGLGraphics graphics, float d_sec, PlayerParty players) {
		health_bar.render(graphics, d_sec, players);
	}

}
