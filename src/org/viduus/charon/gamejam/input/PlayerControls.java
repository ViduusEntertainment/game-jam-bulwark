/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.input;

import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.input.controller.device.JoystickController;
import org.viduus.charon.global.input.player.PlayerController;

/**
 * 
 *
 * @author Ethan Toney
 */
public class PlayerControls extends PlayerController {
	
	private boolean joystick_active = false;
	private int joystick_active_count = 0;
	
	@Override
	public synchronized void onControllerState(ControllerState e) {
		
		//Check if a joystick controller has been active in the past 10 onControllerState calls
		//If a joystick controller is active then we don't want the keyboard controller to reset
		//the joystick controller state.
		if(!(e.getCaller() instanceof JoystickController) && joystick_active) {
			joystick_active_count++;
			if(joystick_active_count == 10) {
				joystick_active = false;
				joystick_active_count = 0;
			}
			return;
		}
		
		boolean state_changed = false;
		
		curr_state.setLeft(0);
		curr_state.setRight(0);
		curr_state.setUp(0);
		curr_state.setDown(0);
		curr_state.setStrafe(0);
		curr_state.setJump(0);
		curr_state.setZoom(0);
		curr_state.setSprint(0);
		curr_state.setRoll(0);
		
		/*
		 * Action keys
		 */
		// Interact
		if( e.getKeyState(ControllerState.ACTION1) <= ControllerState.HELD_STATE ){
			curr_state.setPrimaryAttack(true);
			state_changed = true;
		}
		// Attack
		if( e.getKeyState(ControllerState.ACTION2) <= ControllerState.HELD_STATE ){
			curr_state.setSecondaryAttack(true);
			state_changed = true;
		}
		// Sprint
		if( e.getKeyState(ControllerState.ACTION3) <= ControllerState.HELD_STATE ){
			curr_state.setShield(true);
			state_changed = true;
		}
		// Rolling
		if( e.getKeyState(ControllerState.ACTION4) <= ControllerState.HELD_STATE ){
			curr_state.setRoll(1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.ACTION5) <= ControllerState.HELD_STATE ){
			curr_state.setRoll(1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.ACTION6) <= ControllerState.HELD_STATE ){
			curr_state.setRoll(1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.ACTION7) <= ControllerState.HELD_STATE ){
			curr_state.setRoll(1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.ACTION8) <= ControllerState.HELD_STATE ){
			curr_state.setRoll(1);
			state_changed = true;
		}
		
		/*
		 * Movement keys
		 */
		if( e.getKeyState(ControllerState.MOVE_UP) <= ControllerState.HELD_STATE ){
			curr_state.setUp(-1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.MOVE_DOWN) <= ControllerState.HELD_STATE  ){
			curr_state.setDown(1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.MOVE_LEFT) <= ControllerState.HELD_STATE ){
			curr_state.setLeft(-1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.MOVE_RIGHT) <= ControllerState.HELD_STATE ){
			curr_state.setRight(1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.STRAFE_LEFT_GAME_KEY) <= ControllerState.HELD_STATE){
			curr_state.setStrafe(curr_state.getStrafe()-1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.STRAFE_RIGHT_GAME_KEY) <= ControllerState.HELD_STATE){
			curr_state.setStrafe(curr_state.getStrafe()+1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.CAM_ZOOM_IN_GAME_KEY) <= ControllerState.HELD_STATE ){
			curr_state.setZoom(curr_state.getZoom()-1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.CAM_ZOOM_OUT_GAME_KEY) <= ControllerState.HELD_STATE ){
			curr_state.setZoom(curr_state.getZoom()+1);
			state_changed = true;
		}
		if( e.getKeyState(ControllerState.JUMP_GAME_KEY) <= ControllerState.HELD_STATE ){
			curr_state.setJump(1);
			state_changed = true;
		}
		if(e.getKeyState(ControllerState.SELECT_KEY) <= ControllerState.PRESSED_STATE) {
			curr_state.setSelect(true);
			state_changed = true;
		}
		if(e.getKeyState(ControllerState.INTERACT_KEY) <= ControllerState.PRESSED_STATE) {
			curr_state.setInteract(true);
			state_changed = true;
		}
		if(e.getKeyState(ControllerState.START_BUTTON) <= ControllerState.PRESSED_STATE) {
			curr_state.setStart(true);
			state_changed = true;
		}
		
		if(state_changed && e.getCaller() instanceof JoystickController) {
			joystick_active = true;
		}
	}

}
