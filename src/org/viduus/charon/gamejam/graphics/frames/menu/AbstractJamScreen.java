/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 8, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.frames.menu;

import java.util.ArrayList;
import java.util.List;

import org.viduus.charon.gamejam.graphics.ui.UIElement;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.components.OpenGLComponent;
import org.viduus.charon.global.graphics.screens.AbstractGameScreen;

/**
 * 
 *
 * @author Ethan Toney
 */
public abstract class AbstractJamScreen extends AbstractGameScreen {
	
	List<UIElement> ui_elements = new ArrayList<>();
	
	/**
	 * @param graphics_frame
	 */
	public AbstractJamScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame);
	}
	
	@Override
	public void render(OpenGLGraphics graphics, float d_sec) {
		super.render(graphics, d_sec);
		ui_elements.stream().forEach(element -> element.render(graphics, d_sec));
	}
	
	@Override
	protected void onActivate(AbstractGameSystems game_systems) {
		ui_elements.stream().forEach(element -> element.activate(game_systems));
	}
	
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
//		ui_elements.stream().forEach(element -> element.deactivate(game_systems));
	}
	
	public void addUi(UIElement element) {
		ui_elements.add(element);
	}
	
	@Override
	public void add(OpenGLComponent component) {
		if (component instanceof UIElement)
			addUi((UIElement) component);
		super.add(component);
	}

}
