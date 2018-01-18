/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.hud;

import org.viduus.charon.gamejam.graphics.hud.components.FillInHealthBar;
import org.viduus.charon.gamejam.graphics.hud.components.IconCountdown;
import org.viduus.charon.gamejam.graphics.hud.components.IconCounter;
import org.viduus.charon.gamejam.graphics.hud.components.IconHealthBar;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.ui.HeadsUpDisplay;
import org.viduus.charon.global.player.PlayerParty;
import org.viduus.charon.global.util.identification.Uid;

/**
 * 
 *
 * @author Ethan Toney
 */
public class CharacterHUD extends HeadsUpDisplay {

	private final FillInHealthBar enemy_health_bar;
	private final IconHealthBar player_health_bar;
	private final IconCounter
		missile_stock_counter,
		currency_counter;
	private final IconCountdown
		missile_cooldown_counter,
		shield_cooldown_counter;
	
	/**
	 * @param game_systems
	 */
	public CharacterHUD(AbstractGameSystems game_systems) {
		super(game_systems);
		
		enemy_health_bar = new FillInHealthBar(game_systems);
		player_health_bar = new IconHealthBar(3, game_systems);
		missile_stock_counter = new IconCounter(game_systems, new Uid("vid:animation:hud/icons.missile"), 10);
		currency_counter = new IconCounter(game_systems, new Uid("vid:animation:hud/icons.money"), 0);
		missile_cooldown_counter = new IconCountdown(game_systems, new Uid("vid:animation:hud/icons.missile_cooldown_active"), new Uid("vid:animation:hud/icons.missile_cooldown_inactive"), 0);
		shield_cooldown_counter = new IconCountdown(game_systems, new Uid("vid:animation:hud/icons.shield_cooldown_active"), new Uid("vid:animation:hud/icons.shield_cooldown_inactive"), 0);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.ui.HeadsUpDisplay#render(org.viduus.charon.global.graphics.opengl.OpenGLGraphics, float, org.viduus.charon.global.player.PlayerParty)
	 */
	@Override
	public void render(OpenGLGraphics graphics, float d_sec, PlayerParty players) {
		int screen_width = graphics.getCanvasDimension().width;
		int screen_height = graphics.getCanvasDimension().height;
		
		PlayerCharacter main_player = (PlayerCharacter)players.get(0);
		missile_stock_counter.setCount(main_player.getSecondaryWeapon().getInteger(Property.PROJECTILE_COUNT));
		currency_counter.setCount(main_player.getMoney());
		missile_cooldown_counter.setCount(main_player.getSecondaryWeaponCooldownTimer().coolTimeLeft());
		shield_cooldown_counter.setCount(main_player.getShieldCooldownTimer().coolTimeLeft());
		player_health_bar.setNumIcons(Math.round(main_player.getShip().getHearts()));
		
		enemy_health_bar.render(graphics, screen_width/2, 2, d_sec, players);
		player_health_bar.render(graphics, 0, 0, d_sec, players);
		missile_stock_counter.render(graphics, 2, 18, d_sec);
		currency_counter.render(graphics, screen_width-70, 2, d_sec);
		missile_cooldown_counter.render(graphics, screen_width/2-33, screen_height-34, d_sec);
		shield_cooldown_counter.render(graphics, screen_width/2+1, screen_height-34, d_sec);
	}

}
