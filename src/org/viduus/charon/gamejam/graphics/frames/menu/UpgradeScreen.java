/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 7, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.viduus.charon.gamejam.audio.AudioEngine;
import org.viduus.charon.gamejam.graphics.GraphicsEngine;
import org.viduus.charon.gamejam.graphics.ui.PrimaryWeaponBox;
import org.viduus.charon.gamejam.graphics.ui.SecondaryWeaponBox;
import org.viduus.charon.gamejam.graphics.ui.ShipSelectionBox;
import org.viduus.charon.gamejam.graphics.ui.ShipTakeoffBox;
import org.viduus.charon.gamejam.graphics.ui.UIElement;
import org.viduus.charon.gamejam.graphics.ui.UIString;
import org.viduus.charon.gamejam.graphics.ui.VerticalUpgradeBox;
import org.viduus.charon.gamejam.world.objects.character.playable.PlayerCharacter;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameConstants.GlobalEngineFlags;
import org.viduus.charon.global.GameConstants.Property;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.components.OpenGLComponent;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;
import org.viduus.charon.global.graphics.util.IntDimension;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.input.events.ControllerTypeChangeEvent;
import org.viduus.charon.global.world.objects.twodimensional.weapon.Weapon2D;

/**
 * 
 *
 * @author Ethan Toney
 */
public class UpgradeScreen extends AbstractJamScreen {

	private static final Random RN_JESUS = new Random();
	
	private Animation<?>
		upgrade_menu,
		controller_a,
		controller_x,
		keyboard_a,
		keyboard_space;
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
	private PrimaryWeaponBox
		basic_gun_button,
		chain_gun_button,
		scatter_gun_button,
		arc_gun_button,
		laser_gun_button,
		last_primary_button;
	private SecondaryWeaponBox
		basic_missiles_button,
		scatter_missiles_button,
		bomb_button,
		emp_button,
		gravity_orb_button,
		last_secondary_button;
	private ShipSelectionBox
		mark_1_ship_button,
		heavy_variant_button,
		fast_variant_button,
		mark_2_ship_button,
		last_ship_button;
	private VerticalUpgradeBox
		thruster_upgrade_option,
		shield_upgrade_option,
		armor_upgrade_option;
	private ShipTakeoffBox deploy_ship_button;
	private PlayerCharacter main_character;

	/**
	 * @param graphics_frame
	 */
	public UpgradeScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame);
		
		setAlphaComponent(0);
		
		/* --------------------------------------------------------------------
		 * Ship stuff
		 */
		mark_1_ship_button = new ShipSelectionBox("MK 1", "The standard ship", 3, 5, 1, 0);
		mark_1_ship_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (main_character.tryPurchaseShip("Mk1", 0))
					mark_1_ship_button.setPurchased();
				armor_upgrade_option.setMaxLevel(1);
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipShip(mark_1_ship_button);
			}
		});
		add(mark_1_ship_button);
		
		mark_2_ship_button = new ShipSelectionBox("MK 2", "New and improved", 4, 5, 1, 40000);
		mark_2_ship_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (main_character.tryPurchaseShip("Mk2", 40000))
					mark_2_ship_button.setPurchased();
				armor_upgrade_option.setMaxLevel(1);
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipShip(mark_2_ship_button);
			}
		});
		add(mark_2_ship_button);
		
		heavy_variant_button = new ShipSelectionBox("MK 2 - heavy", "Take a hit or two!", 3, 5, 3, 40000);
		heavy_variant_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (main_character.tryPurchaseShip("Heavy", 40000))
					heavy_variant_button.setPurchased();
				armor_upgrade_option.setMaxLevel(3);
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipShip(heavy_variant_button);
			}
		});
		add(heavy_variant_button);
		
		fast_variant_button = new ShipSelectionBox("MK 2 - fast", "Move quick!", 3, 6, 0, 40000);
		fast_variant_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (main_character.tryPurchaseShip("Fast", 40000))
					fast_variant_button.setPurchased();
				armor_upgrade_option.setMaxLevel(0);
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipShip(fast_variant_button);
			}
		});
		add(fast_variant_button);

		/* --------------------------------------------------------------------
		 * Ship upgrade stuff
		 */
		thruster_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.thrusters", "Thruster", new int[] {2000, 4000, 6000, 8000, 10000});
		thruster_upgrade_option.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (thruster_upgrade_option.getMaxLevel() > main_character.getThrusterUpgrades()) {
					if(main_character.upgradeThruster(thruster_upgrade_option.getNextPrice()))
						thruster_upgrade_option.setLevel(main_character.getThrusterUpgrades());
				}
			}
		});
		add(thruster_upgrade_option);

		shield_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.shield", "Shield", new int[] {1000, 2000, 3000, 4000, 5000});
		shield_upgrade_option.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (shield_upgrade_option.getMaxLevel() > main_character.getShieldUpgrades()) {
					if(main_character.upgradeShield(shield_upgrade_option.getNextPrice()))
						shield_upgrade_option.setLevel(main_character.getShieldUpgrades());
				}
			}
		});
		add(shield_upgrade_option);
		
		armor_upgrade_option = new VerticalUpgradeBox("vid:animation:hud/icons.armor", "Armor", new int[] {1000, 2000, 3000});
		armor_upgrade_option.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (armor_upgrade_option.getMaxLevel() > main_character.getArmorUpgrades()) {
					if(main_character.upgradeArmor(armor_upgrade_option.getNextPrice()))
						armor_upgrade_option.setLevel(main_character.getArmorUpgrades());
				}
			}
		});
		add(armor_upgrade_option);
		armor_upgrade_option.setMaxLevel(1);
		
		/* --------------------------------------------------------------------
		 * Start fighting stuff
		 */
		deploy_ship_button = new ShipTakeoffBox();
		deploy_ship_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				game_systems.graphics_engine.showFrame(GraphicsEngine.START_GAME_SCREEN);
			}
		});
		add(deploy_ship_button);
		
		/* --------------------------------------------------------------------
		 * Primary weapon stuff
		 */
		basic_gun_button = new PrimaryWeaponBox("Basic Gun", 0, new int[] {1000, 2000, 3000, 4000, 5000});
		basic_gun_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (!basic_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("BasicGun", 0))
					basic_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("BasicGun")) {
					if(main_character.tryUpgradeWeapon("BasicGun", basic_gun_button.getNextPrice()))
						basic_gun_button.setLevel(main_character.getUpgradeLevel("BasicGun"));
				}
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipPrimaryWeapon(basic_gun_button);
			}
		});
		add(basic_gun_button);
		basic_gun_button.setPurchased();
		
		chain_gun_button = new PrimaryWeaponBox("Chain Gun", 15000, new int[] {1500, 3000, 4500, 6000, 7500});
		chain_gun_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (!chain_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("ChainGun", 15000))
					chain_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("ChainGun")) {
					if(main_character.tryUpgradeWeapon("ChainGun", chain_gun_button.getNextPrice()))
						chain_gun_button.setLevel(main_character.getUpgradeLevel("ChainGun"));
				}
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipPrimaryWeapon(chain_gun_button);
			}
		});
		add(chain_gun_button);
		
		scatter_gun_button = new PrimaryWeaponBox("Scatter Gun", 10000, new int[] {1200, 2400, 3600, 4800, 6000});
		scatter_gun_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (!scatter_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("ScatterGun", 10000))
					scatter_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("ScatterGun")) {
					if(main_character.tryUpgradeWeapon("ScatterGun", scatter_gun_button.getNextPrice()))
						scatter_gun_button.setLevel(main_character.getUpgradeLevel("ScatterGun"));
				}
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipPrimaryWeapon(scatter_gun_button);
			}
		});
		add(scatter_gun_button);
		
		arc_gun_button = new PrimaryWeaponBox("Arc Gun", 25000, new int[] {1800, 3600, 5400, 7200, 9000});
		arc_gun_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (!arc_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("ArcGun", 25000))
					arc_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("ArcGun")) {
					if(main_character.tryUpgradeWeapon("ArcGun", arc_gun_button.getNextPrice()))
						arc_gun_button.setLevel(main_character.getUpgradeLevel("ArcGun"));
				}
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipPrimaryWeapon(arc_gun_button);
			}
		});
		add(arc_gun_button);
		
		laser_gun_button = new PrimaryWeaponBox("Charge Laser", 20000, new int[] {1600, 3200, 4800, 6400, 8000});
		laser_gun_button.addControllerListener(e -> {
			// purchase/upgrade
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (!laser_gun_button.getPurchased() && main_character.tryPurchasePrimaryWeapon("ChargeLaser", 20000))
					laser_gun_button.setPurchased();
				else if (5 > main_character.getUpgradeLevel("ChargeLaser")) {
					if(main_character.tryUpgradeWeapon("ChargeLaser", laser_gun_button.getNextPrice()))
						laser_gun_button.setLevel(main_character.getUpgradeLevel("ChargeLaser"));
				}
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipPrimaryWeapon(laser_gun_button);
			}
		});
		add(laser_gun_button);
		
		/* --------------------------------------------------------------------
		 * Secondary weapon stuff
		 */
		basic_missiles_button = new SecondaryWeaponBox("Basic Missiles", 15, 500);
		basic_missiles_button.addControllerListener(e -> {
			// purchase
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (basic_missiles_button.canPurchase() && main_character.tryPurchaseSecondaryWeapon("BasicMissile", 500))
					basic_missiles_button.increaseStock();
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipSecondaryWeapon(basic_missiles_button);
			}
		});
		add(basic_missiles_button);

		scatter_missiles_button = new SecondaryWeaponBox("Scatter Missiles", 15, 1000);
		scatter_missiles_button.addControllerListener(e -> {
			// purchase
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (scatter_missiles_button.canPurchase() && main_character.tryPurchaseSecondaryWeapon("ScatterMissiles", 1000))
					scatter_missiles_button.increaseStock();
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipSecondaryWeapon(scatter_missiles_button);
			}
		});
		add(scatter_missiles_button);

		bomb_button = new SecondaryWeaponBox("Bomb", 15, 800);
		bomb_button.addControllerListener(e -> {
			// purchase
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (bomb_button.canPurchase() && main_character.tryPurchaseSecondaryWeapon("Bomb", 800))
					bomb_button.increaseStock();
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipSecondaryWeapon(bomb_button);
			}
		});
		add(bomb_button);

		emp_button = new SecondaryWeaponBox("Emp", 5, 5000);
		emp_button.addControllerListener(e -> {
			// purchase
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (emp_button.canPurchase() && main_character.tryPurchaseSecondaryWeapon("Emp", 5000))
					emp_button.increaseStock();
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipSecondaryWeapon(emp_button);
			}
		});
		add(emp_button);

		gravity_orb_button = new SecondaryWeaponBox("Gravity Orb", 4, 6000);
		gravity_orb_button.addControllerListener(e -> {
			// purchase
			if (e.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (gravity_orb_button.canPurchase() && main_character.tryPurchaseSecondaryWeapon("GravityOrb", 6000))
					gravity_orb_button.increaseStock();
			}
			// equip
			else if (e.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				tryEquipSecondaryWeapon(gravity_orb_button);
			}
		});
		add(gravity_orb_button);
	}
	
	private void tryEquipShip(ShipSelectionBox button) {
		if (main_character.tryEquipShip(button.getShipName())) {
			button.equip();
			if (last_ship_button != null) {
				last_ship_button.unequip();
			}
			last_ship_button = button;
		}
	}
	
	private void tryEquipPrimaryWeapon(PrimaryWeaponBox button) {
		if (main_character.tryEquipPrimaryWeapon(button.getName())) {
			button.equip();
			if (last_primary_button != null) {
				last_primary_button.unequip();
			}
			last_primary_button = button;
		}
	}
	
	private void tryEquipSecondaryWeapon(SecondaryWeaponBox button) {
		if (main_character.tryEquipSecondaryWeapon(button.getName())) {
			button.equip();
			if (last_secondary_button != null) {
				last_secondary_button.unequip();
			}
			last_secondary_button = button;
		}
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
		
		int row1_y = menu_y + 143;
		int row2_y = menu_y + 240;
		int row3_y = menu_y + 364;
		
		// render ui background shadow
		UIElement.renderColoredSquare(graphics, menu_x + 186, menu_y, menu_width - 186, row1_y, 0.1882352941f, 0.3764705882f, 0.5098039216f, 0.2f);
		UIElement.renderColoredSquare(graphics, menu_x, menu_y, 186, row3_y, 0.1882352941f, 0.3764705882f, 0.5098039216f, 0.2f);
		UIElement.renderColoredSquare(graphics, menu_x + 186, menu_y + row2_y, menu_width - 186, row3_y - row2_y, 0.1882352941f, 0.3764705882f, 0.5098039216f, 0.2f);
		
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
			OpenGLFont.drawString2D(graphics, "Hearts: " + Math.round(main_character.getShip().getHearts()), c1_left, top + dr);
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
			
			UIElement.renderColoredSquare(graphics, tlx, row3_y, menu_width-6, menu_height - row3_y, 0.1333333333f, 0.1254901961f, 0.2039215686f, 0.8f);
			
			OpenGLFont.drawString2D(graphics, "Commander: " + demotivational_messages[demotivational_message_selection], tlx + 3, row3_y + 13);
		}
		
		/*
		 * Controls
		 */
		{
			int top = row3_y + 33;
			int left = menu_x + 3;
			
			if (focused_components.size() > 0) {
				OpenGLComponent focused_component = focused_components.get(0);
				
				if (focused_component instanceof UIElement) {
					UIString tool_tip = ((UIElement)focused_component).getToolTip();
					if (tool_tip != null) {
						tool_tip.render(graphics, d_sec, left + 3, top);
					}
				}
			}
		}
		
		// draw outline ui
		upgrade_menu.renderAnimation(graphics, d_sec, menu_x, menu_y, 1);
	}
	
	private void onControllerTypeChange(ControllerTypeChangeEvent event) {
		Animation<?> action_1, action_2;
		
		switch (event.type) {
		case GAMEPAD:
			action_1 = controller_a;
			action_2 = controller_x;
			break;
		default:
			action_1 = keyboard_space;
			action_2 = keyboard_a;
			break;
		}
		
		basic_gun_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		chain_gun_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		scatter_gun_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		arc_gun_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		laser_gun_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		
		basic_missiles_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		scatter_missiles_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		bomb_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		emp_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		gravity_orb_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Equip")));
		
		mark_1_ship_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Use")));
		heavy_variant_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Use")));
		fast_variant_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Use")));
		mark_2_ship_button.setToolTip(new UIString(Pair.of(action_1, "Unlock/Upgrade   "), Pair.of(action_2, "Use")));
		
		thruster_upgrade_option.setToolTip(new UIString(action_1, "Upgrade"));
		shield_upgrade_option.setToolTip(new UIString(action_1, "Upgrade"));
		armor_upgrade_option.setToolTip(new UIString(action_1, "Upgrade"));
		
		deploy_ship_button.setToolTip(new UIString(action_1, "Start!"));
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(int previous_screen_id, AbstractGameSystems game_systems) {
		// pause engine state
		game_systems.graphics_engine.disable(GlobalEngineFlags.RENDER_PLAYER_HUD);
		game_systems.world_engine.disable(GlobalEngineFlags.TICK_WORLD);
		
		// pause player state
		main_character = (PlayerCharacter) game_systems.party.get(0);
		main_character.unbindInputEngine(game_systems.input_engine);
		
		// load stuff
		upgrade_menu = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:hud/upgrade_menu.menu");
		controller_a = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:controller/xbox.a");
		controller_x = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:controller/xbox.x");
		keyboard_a = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:controller/keyboard.A");
		keyboard_space = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:controller/keyboard.space");
		
		// set tooltips
		onControllerTypeChange(new ControllerTypeChangeEvent(game_systems.input_engine.getLastUsedControllerType()));
	
		super.onActivate(previous_screen_id, game_systems);
		
		game_systems.input_engine.registerGlobalEventCallback(ControllerTypeChangeEvent.class, this::onControllerTypeChange);
		
		/*
		 * Demotivational message stuff
		 */
		demotivational_message_selection = RN_JESUS.nextInt(demotivational_messages.length);
		
		AudioEngine.LEVEL1_TRACK.stop();
		if (previous_screen_id != GraphicsEngine.MENU_SCREEN)
			AudioEngine.MENU_TRACK.play();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		super.onDeactivate(game_systems);
		
		// fix engine state
		game_systems.graphics_engine.enable(GlobalEngineFlags.RENDER_PLAYER_HUD);
		
		AudioEngine.MENU_TRACK.stop(); 
		AudioEngine.LEVEL1_TRACK.play();
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
			int rh = 32;
			
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
			int rh = 32;
			
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
