/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 7, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import java.util.Random;

import org.viduus.charon.gamejam.graphics.ui.HorizontalUpgradeBox;
import org.viduus.charon.gamejam.graphics.ui.ShipSelectionBox;
import org.viduus.charon.gamejam.graphics.ui.UIElement;
import org.viduus.charon.gamejam.graphics.ui.VerticalUpgradeBox;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameConstants.EngineFlags;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.graphics.AbstractGraphicsEngine;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;
import org.viduus.charon.global.graphics.util.IntDimension;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.world.objects.twodimensional.character.playable.PlayableCharacter2D;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;

/**
 * 
 *
 * @author Ethan Toney
 */
public class UpgradeScreen extends AbstractJamScreen {

	private static final Random RN_JESUS = new Random();
	
	private Animation<?> upgrade_menu;
	private String[] demotivational_messages = {
		"You pilots aren’t worth the Currency we pay you.",
		"Every crashed drone comes out of your pay.",
		"Upgrades aren’t free.  We have to save them for the good pilots.",
		"We probably should’ve listened to those space scientists.",
		"You are the last that humanity has to offer.",
		"There is absolutely no more room for failure.",
		"The only good alien is a dead alien.",
		"We aren’t paying you to fail.",
		"Perhaps you should consider improving.",
		"The more time you waste the more people die."
	};
	private int demotivational_message_selection;
	private HorizontalUpgradeBox
		basic_gun_button,
		chain_gun_button,
		scatter_gun_button,
		arc_gun_button,
		laser_gun_button;
	private HorizontalUpgradeBox
		basic_missiles_button,
		scatter_missiles_button,
		bomb_button,
		emp_button,
		gravity_orb_button;
	private ShipSelectionBox
		mark_1_ship_button,
		heavy_variant_button,
		fast_variant_button,
		mark_2_ship_button;
	private VerticalUpgradeBox
		thruster_upgrade_option,
		shield_upgrade_option,
		armor_upgrade_option;

	/**
	 * @param graphics_frame
	 */
	public UpgradeScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame);
		
		setAlphaComponent(0);
		
		/*
		 * Ship stuff
		 */
		mark_1_ship_button = new ShipSelectionBox("MK 1", "The standard ship", 3, 5, 1);
		add(mark_1_ship_button);
		
		mark_2_ship_button = new ShipSelectionBox("MK 2", "New and improved", 4, 5, 1);
		add(mark_2_ship_button);
		
		heavy_variant_button = new ShipSelectionBox("MK 2 - heavy", "Take a hit or two!", 3, 5, 3);
		add(heavy_variant_button);
		
		fast_variant_button = new ShipSelectionBox("MK 2 - fast", "Move quick!", 3, 6, 0);
		add(fast_variant_button);

		/*
		 * Ship upgrade stuff
		 */
		thruster_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.thrusters", "Thruster");
		add(thruster_upgrade_option);

		shield_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.shield", "Shield");
		add(shield_upgrade_option);
		
		armor_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.armor", "Armor");
		add(armor_upgrade_option);
		
		/*
		 * Primary weapon stuff
		 */
		basic_gun_button = new HorizontalUpgradeBox("Basic Gun", 0, new int[] {1000, 2000, 3000, 4000, 5000, 6000});
		add(basic_gun_button);
		
		chain_gun_button = new HorizontalUpgradeBox("Chain Gun", 15000, new int[] {1500, 3000, 4500, 6000, 7500, 9000});
		add(chain_gun_button);
		
		scatter_gun_button = new HorizontalUpgradeBox("Scatter Gun", 10000, new int[] {1200, 2400, 3600, 4800, 6000, 7200});
		add(scatter_gun_button);
		
		arc_gun_button = new HorizontalUpgradeBox("Arc Gun", 25000, new int[] {1800, 3600, 5400, 7200, 9000, 10800});
		add(arc_gun_button);
		
		laser_gun_button = new HorizontalUpgradeBox("Charge Laser", 20000, new int[] {1600, 3200, 4800, 6400, 8000, 9600});
		add(laser_gun_button);
		
		/*
		 * Secondary weapon stuff
		 */
		basic_missiles_button = new HorizontalUpgradeBox("Basic Missiles", 500, new int[] {1600, 3200, 4800, 6400, 8000, 9600});
		add(basic_missiles_button);

		scatter_missiles_button = new HorizontalUpgradeBox("Scatter Missiles", 1000, new int[] {1600, 3200, 4800, 6400, 8000, 9600});
		add(scatter_missiles_button);

		bomb_button = new HorizontalUpgradeBox("Bomb", 800, new int[] {1600, 3200, 4800, 6400, 8000, 9600});
		add(bomb_button);

		emp_button = new HorizontalUpgradeBox("Emp", 5000, new int[] {1600, 3200, 4800, 6400, 8000, 9600});
		add(emp_button);

		gravity_orb_button = new HorizontalUpgradeBox("Gravity Orb", 6000, new int[] {1600, 3200, 4800, 6400, 8000, 9600});
		add(gravity_orb_button);
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec){
		IntDimension canvas_dimension = graphics.getCanvasDimension();
		int screen_width = canvas_dimension.width;
		int screen_height = canvas_dimension.height;

		int menu_width = (int) upgrade_menu.getCurrentFrame().getBoundingSize().width;
		int menu_height = (int) upgrade_menu.getCurrentFrame().getBoundingSize().height;
		int menu_x = (screen_width-menu_width)/2;
		int menu_y = (screen_height-menu_height)/2;
		
		int side_bar_width = (screen_width-menu_width)/2;
		
		UIElement.renderColoredSquare(graphics, menu_x, menu_y, menu_width, menu_height, 0.1882352941f, 0.3764705882f, 0.5098039216f, 0.2f);
		UIElement.renderColoredSquare(graphics, 0, 0, side_bar_width, menu_height, 0, 0, 0, 0.2f);
		UIElement.renderColoredSquare(graphics, menu_x+menu_width, 0, side_bar_width, menu_height, 0, 0, 0, 0.2f);
		
		super.render(graphics, d_sec);
		
		/*
		 * Stats section
		 */
		{
			int dr = OpenGLFont.getLineHeight() + 5;
			int top = menu_y + 145 + OpenGLFont.getLineHeight();
			int left = menu_x + 10;
			
			int c1_left = left;
			int c2_left = left + 100;
			
			PlayableCharacter2D character = game_systems.party.get(0);
			Weapon2D p_wep = character.<Weapon2D>getList(Property.EQUIPPED_WEAPONS).get(0);
			Weapon2D s_wep = character.<Weapon2D>getList(Property.EQUIPPED_WEAPONS).get(1);
			
			OpenGLFont.drawString2D(graphics, "Player Stats", (int) (c1_left + (180 - OpenGLFont.getStringWidth("Player Stats"))/2), top);
			OpenGLFont.drawString2D(graphics, "Hearts: " + character.getFloat(Property.MAX_HEALTH), c1_left, top + dr);
			OpenGLFont.drawString2D(graphics, "Currency: " + ((PlayerCharacter)character).getMoney(), c1_left, top + 2*dr);
			OpenGLFont.drawString2D(graphics, "P. DMG: " + p_wep.getFloat(Property.DAMAGE), c1_left, top + 3*dr);
			OpenGLFont.drawString2D(graphics, "S. DMG: " + s_wep.getFloat(Property.DAMAGE), c1_left, top + 4*dr);

			OpenGLFont.drawString2D(graphics, "Stock: " + s_wep.getInteger(Property.PROJECTILE_COUNT), c2_left, top + dr);
			OpenGLFont.drawString2D(graphics, "Speed: " + character.getFloat(Property.SPEED), c2_left, top + 2*dr);
		}
		
		/*
		 * Primary weapons
		 */
		{
			int top = menu_y + 244 + OpenGLFont.getLineHeight();
			int left = menu_x + 3;
			
			OpenGLFont.drawString2D(graphics, "Primary Weapons", (int) (left + (195 - OpenGLFont.getStringWidth("Primary Weapons"))/2), top);
		}
		
		/*
		 * Secondary weapons
		 */
		{
			int top = menu_y + 244 + OpenGLFont.getLineHeight();
			int left = menu_x + 201;
			
			OpenGLFont.drawString2D(graphics, "Secondary Weapons", (int) (left + (196 - OpenGLFont.getStringWidth("Secondary Weapons"))/2), top);
		}
		
		/*
		 * Demotivational message
		 */
		{
			int tlx = menu_x + 3;
			int tly = menu_y + 370;
			
			UIElement.renderColoredSquare(graphics, tlx, tly, menu_width-6, 27, 0.1333333333f, 0.1254901961f, 0.2039215686f, 0.8f);
			
			OpenGLFont.drawString2D(graphics, "Commander: " + demotivational_messages[demotivational_message_selection], tlx + 5, tly + 16);
		}
		
		// draw outline ui
		upgrade_menu.renderAnimation(graphics, d_sec, menu_x, menu_y, 1);
	}
	
	@Override
	public void onControllerState(ControllerState e) {
		super.onControllerState(e);
		
		if (e.getKeyState(ControllerState.SELECT_KEY) == ControllerState.PRESSED_STATE) {
			if (thruster_upgrade_option.hasFocus()) {
				OutputHandler.println("thruster upgrade");
			}
			else if (shield_upgrade_option.hasFocus()) {
				OutputHandler.println("shield upgrade");
			}
			else if (armor_upgrade_option.hasFocus()) {
				OutputHandler.println("armor upgrade");
			}
			else if (false) {
				// fix player state
				game_systems.party.get(0).bindInputEngine(game_systems.input_engine);
				
				// fix engine state
				game_systems.graphics_engine.enable(EngineFlags.RENDER_PLAYER_HUD);
				game_systems.world_engine.enable(EngineFlags.TICK_WORLD);
				
				// lfg
				game_systems.graphics_engine.showFrame(AbstractGraphicsEngine.GAME_SCREEN);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(AbstractGameSystems game_systems) {
		// pause engine state
		game_systems.graphics_engine.disable(EngineFlags.RENDER_PLAYER_HUD);
		game_systems.world_engine.disable(EngineFlags.TICK_WORLD);
		
		// pause player state
		game_systems.party.get(0).unbindInputEngine(game_systems.input_engine);
		
		// load stuff
		upgrade_menu = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/upgrade_menu.menu");
		super.onActivate(game_systems);
		
		/*
		 * Demotivational message stuff
		 */
		demotivational_message_selection = RN_JESUS.nextInt(demotivational_messages.length);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		super.onDeactivate(game_systems);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {
		int dx = (width - 400) / 2;
		
		/*
		 * Ship stuff
		 */
		{
			int top = 14 + 10;
			int left = dx + 3 + 2;

			int dc = 188/2;
			int dr = 126/2;
			
			mark_1_ship_button.setLocation(left, top);
			mark_2_ship_button.setLocation(left + dc, top);
			heavy_variant_button.setLocation(left, top + dr);
			fast_variant_button.setLocation(left + dc, top + dr);
		}
		
		/*
		 * Ship upgrade stuff
		 */
		{
			int left_col = 182 - 8;
			int right_col = 400 - left_col;
			int bottom_location = 110;
			
			int thruster_loc = dx + left_col + right_col/4;
			int armor_loc = dx + left_col + 2*right_col/4;
			int shield_loc = dx + left_col + 3*right_col/4;
			
			thruster_upgrade_option.setLocation(thruster_loc, bottom_location);
			shield_upgrade_option.setLocation(shield_loc, bottom_location);
			armor_upgrade_option.setLocation(armor_loc, bottom_location);
		}
		
		/*
		 * Primary weapon stuff
		 */
		{
			int top = 244 + 20;
			int left = dx + 10;
			int rh = 35;
			
			int c1_left = left;
			int c2_left = left+100;
			
			basic_gun_button.setLocation(c1_left, top);
			chain_gun_button.setLocation(c1_left, top + rh);
			scatter_gun_button.setLocation(c1_left, top + 2*rh);
			
			arc_gun_button.setLocation(c2_left, top);
			laser_gun_button.setLocation(c2_left, top + rh);
		}
		
		/*
		 * Secondary weapon stuff
		 */
		{
			int top = 244 + 20;
			int left = dx + 201 + 8;
			int rh = 35;
			
			int c1_left = left;
			int c2_left = left+100;
			
			basic_missiles_button.setLocation(c1_left, top);
			scatter_missiles_button.setLocation(c1_left, top + rh);
			bomb_button.setLocation(c1_left, top + 2*rh);
			
			emp_button.setLocation(c2_left, top);
			gravity_orb_button.setLocation(c2_left, top + rh);
		}
	}

}
