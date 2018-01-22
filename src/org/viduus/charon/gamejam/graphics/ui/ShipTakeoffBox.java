/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 9, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;

/**
 * 
 *
 * @author Ethan Toney
 */
public class ShipTakeoffBox extends UIElement {

	public ShipTakeoffBox() {
		
	}
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#render(org.viduus.charon.global.graphics.opengl.OpenGLGraphics, float)
	 */
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		if (hasFocus()) {
			renderSelect(graphics, d_sec, getX(), getY(), getWidth()-1, getHeight()-1);
			
			int top = getY();
//			int left = (int) (getX() + (getWidth() - OpenGLFont.getStringWidth("Ready!"))/2);
			int left = (int) (getX() + getWidth() - OpenGLFont.getStringWidth("Ready!") - 10);
			OpenGLFont.drawString2D(graphics, "Ready!", left, top+getHeight()-10);
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		updateSize();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onDeactivate(AbstractGameSystems game_systems) {
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.gamejam.graphics.ui.UIElement#updateSize()
	 */
	@Override
	protected void updateSize() {
		setSize(211, 97);
	}

}
