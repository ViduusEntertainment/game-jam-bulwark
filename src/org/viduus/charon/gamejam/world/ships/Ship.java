package org.viduus.charon.gamejam.world.ships;

public abstract class Ship {
	private static final int SPEED_MULT = 60;
	
	private final int base_hearts;
	private final int base_speed;
	private final int armor_slots;
	private int thruster_upgrade = 0;
	private int armor_upgrade = 0;
	
	public Ship(int base_hearts, int base_speed, int armor_slots) {
		this.base_hearts = base_hearts;
		this.base_speed = base_speed;
		this.armor_slots = armor_slots;
	}
	
	public void setThrusterUpgrade(int thruster_upgrade) {
		this.thruster_upgrade = thruster_upgrade;
	}
	
	public void setArmorUpgrade(int armor_upgrade) {
		this.armor_upgrade = armor_upgrade;
	}
	
	public float getSpeed() {
		return SPEED_MULT * (base_speed + thruster_upgrade - (armor_upgrade > armor_slots ? armor_slots : armor_upgrade));
	}
	
	public float getHearts() {
		return base_hearts + (armor_upgrade > armor_slots ? armor_slots : armor_upgrade);
	}
}
