/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 7, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import java.util.Random;

import org.dyn4j.geometry.Vector2;
import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.gamejam.graphics.ui.CheckBox;
import org.viduus.charon.gamejam.graphics.ui.HorizontalUpgradeBox;
import org.viduus.charon.gamejam.graphics.ui.ShipSelectionBox;
import org.viduus.charon.gamejam.graphics.ui.ShipTakeoffBox;
import org.viduus.charon.gamejam.graphics.ui.UIElement;
import org.viduus.charon.gamejam.graphics.ui.VerticalUpgradeBox;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameConstants.GlobalEngineFlags;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;
import org.viduus.charon.global.graphics.util.IntDimension;
import org.viduus.charon.global.graphics.util.Size;
import org.viduus.charon.global.input.controller.ControllerState;
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
		"You pilots aren't worth the Currency we pay you.",
		"Every crashed drone comes out of your pay.",
		"Upgrades aren't free.  We have to save them for the good pilots.",
		"We probably should've listened to those space scientists.",
		"You are the last that humanity has to offer.",
		"There is absolutely no more room for failure.",
		"The only good alien is a dead alien.",
		"We aren't paying you to fail.",
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
	

	private CheckBox
	equip_mk1_button,
	equip_mk2_button,
	equip_heavy_button,
	equip_fast_button,
	equip_basic_gun_button,
	equip_chain_gun_button,
	equip_scatter_gun_button,
	equip_arc_gun_button,
	equip_laser_gun_button,
	equip_basic_missiles_button,
	equip_scatter_missiles_button,
	equip_bomb_button,
	equip_emp_button,
	equip_gravity_orb_button;
	
	private ShipTakeoffBox deploy_ship_button;

	private PlayerCharacter main_character;

	/**
	 * @param graphics_frame
	 */
	public UpgradeScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame);
		
		setAlphaComponent(0);
		
		/*
		 * Ship stuff
		 */
		mark_1_ship_button = new ShipSelectionBox("MK 1", "The standard ship", 3, 5, 1, 0);
		add(mark_1_ship_button);
		
		mark_2_ship_button = new ShipSelectionBox("MK 2", "New and improved", 4, 5, 1, 40000);
		add(mark_2_ship_button);
		
		heavy_variant_button = new ShipSelectionBox("MK 2 - heavy", "Take a hit or two!", 3, 5, 3, 40000);
		add(heavy_variant_button);
		
		fast_variant_button = new ShipSelectionBox("MK 2 - fast", "Move quick!", 3, 6, 0, 40000);
		add(fast_variant_button);

		/*
		 * Ship upgrade stuff
		 */
		thruster_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.thrusters", "Thruster", new int[] {2000, 4000, 6000, 8000, 10000});
		add(thruster_upgrade_option);

		shield_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.shield", "Shield", new int[] {1000, 2000, 3000, 4000, 5000});
		add(shield_upgrade_option);
		
		armor_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.armor", "Armor", new int[] {1000, 2000, 3000});
		add(armor_upgrade_option);
		armor_upgrade_option.setMaxLevel(1);
		
		/*
		 * Start fighting stuff
		 */
		deploy_ship_button = new ShipTakeoffBox();
		add(deploy_ship_button);
		
		/*
		 * Primary weapon stuff
		 */
		basic_gun_button = new HorizontalUpgradeBox("Basic Gun", 0, new int[] {1000, 2000, 3000, 4000, 5000});
		add(basic_gun_button);
		basic_gun_button.setPurchased();
		
		chain_gun_button = new HorizontalUpgradeBox("Chain Gun", 15000, new int[] {1500, 3000, 4500, 6000, 7500});
		add(chain_gun_button);
		
		scatter_gun_button = new HorizontalUpgradeBox("Scatter Gun", 10000, new int[] {1200, 2400, 3600, 4800, 6000});
		add(scatter_gun_button);
		
		arc_gun_button = new HorizontalUpgradeBox("Arc Gun", 25000, new int[] {1800, 3600, 5400, 7200, 9000});
		add(arc_gun_button);
		
		laser_gun_button = new HorizontalUpgradeBox("Charge Laser", 20000, new int[] {1600, 3200, 4800, 6400, 8000});
		add(laser_gun_button);
		
		/*
		 * Secondary weapon stuff
		 */
		basic_missiles_button = new HorizontalUpgradeBox("Basic Missiles", 500, new int[] {1600, 3200, 4800, 6400, 8000});
		add(basic_missiles_button);

		scatter_missiles_button = new HorizontalUpgradeBox("Scatter Missiles", 1000, new int[] {1600, 3200, 4800, 6400, 8000});
		add(scatter_missiles_button);

		bomb_button = new HorizontalUpgradeBox("Bomb", 800, new int[] {1600, 3200, 4800, 6400, 8000});
		add(bomb_button);

		emp_button = new HorizontalUpgradeBox("Emp", 5000, new int[] {1600, 3200, 4800, 6400, 8000});
		add(emp_button);

		gravity_orb_button = new HorizontalUpgradeBox("Gravity Orb", 6000, new int[] {1600, 3200, 4800, 6400, 8000});
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
		
		// render ui background shadow
		UIElement.renderColoredSquare(graphics, menu_x + 186, menu_y, menu_width - 186, 143, 0.1882352941f, 0.3764705882f, 0.5098039216f, 0.2f);
		UIElement.renderColoredSquare(graphics, menu_x, menu_y, 186, menu_height, 0.1882352941f, 0.3764705882f, 0.5098039216f, 0.2f);
		UIElement.renderColoredSquare(graphics, menu_x + 186, menu_y + 240, menu_width - 186, 160, 0.1882352941f, 0.3764705882f, 0.5098039216f, 0.2f);
		
		// render left and right borders
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
			
			Weapon2D p_wep = main_character.getPrimaryWeapon();
			Weapon2D s_wep = main_character.getSecondaryWeapon();
			
			OpenGLFont.drawString2D(graphics, "Player Stats", (int) (c1_left + (180 - OpenGLFont.getStringWidth("Player Stats"))/2), top);
			OpenGLFont.drawString2D(graphics, "Hearts: " + main_character.getShip().getHearts(), c1_left, top + dr);
			OpenGLFont.drawString2D(graphics, "Currency: " + main_character.getMoney(), c1_left, top + 2*dr);
			OpenGLFont.drawString2D(graphics, "P. DMG: " + p_wep.getFloat(Property.DAMAGE), c1_left, top + 3*dr);
			OpenGLFont.drawString2D(graphics, "S. DMG: " + s_wep.getFloat(Property.DAMAGE), c1_left, top + 4*dr);

			OpenGLFont.drawString2D(graphics, "Stock: " + s_wep.getInteger(Property.PROJECTILE_COUNT), c2_left, top + dr);
			OpenGLFont.drawString2D(graphics, "Speed: " + main_character.getShip().getSpeed(), c2_left, top + 2*dr);
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
				if (thruster_upgrade_option.getMaxLevel() > main_character.getThrusterUpgrades()) {
					if(main_character.upgradeThruster(thruster_upgrade_option.getNextPrice()))
						thruster_upgrade_option.setLevel(main_character.getThrusterUpgrades());
				}
			}
			else if (shield_upgrade_option.hasFocus()) {
				if (shield_upgrade_option.getMaxLevel() > main_character.getShieldUpgrades()) {
					if(main_character.upgradeShield(shield_upgrade_option.getNextPrice()))
						shield_upgrade_option.setLevel(main_character.getShieldUpgrades());
				}
			}
			else if (armor_upgrade_option.hasFocus()) {
				if (armor_upgrade_option.getMaxLevel() > main_character.getArmorUpgrades()) {
					if(main_character.upgradeArmor(armor_upgrade_option.getNextPrice()))
						armor_upgrade_option.setLevel(main_character.getArmorUpgrades());
				}
			}
			else if (deploy_ship_button.hasFocus()) {
				game_systems.graphics_engine.showFrame(GraphicsEngine.START_GAME_SCREEN);
			}
			else if (mark_1_ship_button.hasFocus()) {
				if (main_character.tryPurchaseShip("Mk1", 0))
					mark_1_ship_button.setPurchased();
				armor_upgrade_option.setMaxLevel(1);
			}
			else if (mark_2_ship_button.hasFocus()) {
				if (main_character.tryPurchaseShip("Mk2", 40000))
					mark_2_ship_button.setPurchased();
				armor_upgrade_option.setMaxLevel(1);
			}
			else if (heavy_variant_button.hasFocus()) {
				if (main_character.tryPurchaseShip("Heavy", 40000))
					heavy_variant_button.setPurchased();
				armor_upgrade_option.setMaxLevel(3);
			}
			else if (fast_variant_button.hasFocus()) {
				if (main_character.tryPurchaseShip("Fast", 40000))
					fast_variant_button.setPurchased();
				armor_upgrade_option.setMaxLevel(0);
			}
			else if (basic_gun_button.hasFocus()) {
				if (!basic_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("Basic", 0))
					basic_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("Basic")) {
					if(main_character.tryUpgradeWeapon("Basic", basic_gun_button.getNextPrice()))
						basic_gun_button.setLevel(main_character.getUpgradeLevel("Basic"));
				}
			}
			else if (chain_gun_button.hasFocus()) {
				if (!chain_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("Chain", 15000))
					chain_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("Chain")) {
					if(main_character.tryUpgradeWeapon("Chain", chain_gun_button.getNextPrice()))
						chain_gun_button.setLevel(main_character.getUpgradeLevel("Chain"));
				}
			}
			else if (scatter_gun_button.hasFocus()) {
				if (!scatter_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("Scatter", 10000))
					scatter_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("Scatter")) {
					if(main_character.tryUpgradeWeapon("Scatter", scatter_gun_button.getNextPrice()))
						scatter_gun_button.setLevel(main_character.getUpgradeLevel("Scatter"));
				}
			}
			else if (arc_gun_button.hasFocus()) {
				if (!arc_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("Arc", 25000))
					arc_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("Arc")) {
					if(main_character.tryUpgradeWeapon("Arc", arc_gun_button.getNextPrice()))
						arc_gun_button.setLevel(main_character.getUpgradeLevel("Arc"));
				}
			}
			else if (laser_gun_button.hasFocus()) {
				if (!laser_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("ChargeLaser", 20000))
					laser_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("ChargeLaser")) {
					if(main_character.tryUpgradeWeapon("ChargeLaser", laser_gun_button.getNextPrice()))
						laser_gun_button.setLevel(main_character.getUpgradeLevel("ChargeLaser"));
				}
			}
			else if (basic_missiles_button.hasFocus()) {
				if (!basic_missiles_button.getPurchased() && main_character.tryPurchaseSecondaryWeapon("BasicMissile", 0))
					basic_missiles_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("BasicMissile")) {
					if(main_character.tryUpgradeWeapon("BasicMissile", basic_missiles_button.getNextPrice()))
						basic_missiles_button.setLevel(main_character.getUpgradeLevel("BasicMissile"));
				}
			}
			else if (scatter_missiles_button.hasFocus()) {
				if (!scatter_missiles_button.getPurchased() && main_character.tryPurchaseSecondaryWeapon("ScatterMissile", 15000))
					scatter_missiles_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("ScatterMissile")) {
					if(main_character.tryUpgradeWeapon("ScatterMissile", scatter_missiles_button.getNextPrice()))
						scatter_missiles_button.setLevel(main_character.getUpgradeLevel("ScatterMissile"));
				}
			}
			else if (bomb_button.hasFocus()) {
				if (!bomb_button.getPurchased() && main_character.tryPurchaseSecondaryWeapon("Bomb", 10000))
					bomb_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("Bomb")) {
					if(main_character.tryUpgradeWeapon("Bomb", bomb_button.getNextPrice()))
						bomb_button.setLevel(main_character.getUpgradeLevel("Bomb"));
				}
			}
			else if (emp_button.hasFocus()) {
				if (!emp_button.getPurchased() && main_character.tryPurchaseSecondaryWeapon("Emp", 25000))
					emp_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("Emp")) {
					if(main_character.tryUpgradeWeapon("Emp", emp_button.getNextPrice()))
						emp_button.setLevel(main_character.getUpgradeLevel("Emp"));
				}
			}
			else if (gravity_orb_button.hasFocus()) {
				if (!gravity_orb_button.getPurchased() && main_character.tryPurchaseSecondaryWeapon("GravityOrb", 20000))
					gravity_orb_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("GravityOrb")) {
					if(main_character.tryUpgradeWeapon("GravityOrb", gravity_orb_button.getNextPrice()))
						gravity_orb_button.setLevel(main_character.getUpgradeLevel("GravityOrb"));
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(AbstractGameSystems game_systems) {
		// pause engine state
		game_systems.graphics_engine.disable(GlobalEngineFlags.RENDER_PLAYER_HUD);
		game_systems.world_engine.disable(GlobalEngineFlags.TICK_WORLD);
		
		// pause player state
		main_character = (PlayerCharacter) game_systems.party.get(0);
		main_character.unbindInputEngine(game_systems.input_engine);
		
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
		
		// fix engine state
		game_systems.graphics_engine.enable(GlobalEngineFlags.RENDER_PLAYER_HUD);
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
		 * Ship deployment stuff
		 */
		{
			deploy_ship_button.setLocation(dx + 186, 143);
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
		
		/*
		 * Equip button stuff
		 */
		{
			
		}
	}

}
