/**
 * Copyright 2017, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 9, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam;

import org.viduus.charon.global.util.properties.templates.PropertyTemplate;

/**
 * 
 *
 * @author Ethan Toney
 */
public class GameConstants {
	
	public static interface EngineFlags {
		
		public static final PropertyTemplate
				// WorldEngine
				SPAWN_WAVES = new PropertyTemplate("spawn_waves", Boolean.class);
	}

}
