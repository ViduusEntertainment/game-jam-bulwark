/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 6, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameEngine;
import org.viduus.charon.global.player.Account;

/**
 * 
 *
 * @author Ethan Toney
 */
public class GameSystems extends AbstractGameSystems {

	public static final int GAME = 1;
	
	public GameSystems(GameEngine ... engines){
		super(engines);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.AbstractGameSystems#loadAccount()
	 */
	@Override
	protected Account loadAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.AbstractGameSystems#saveAccount(org.viduus.charon.global.player.Account)
	 */
	@Override
	protected void saveAccount(Account account) {
		// TODO Auto-generated method stub
		
	}
}
