package org.viduus.charon.gamejam.audio;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.audio.Track;

public class AudioEngine extends org.viduus.charon.global.audio.AudioEngine{
	
	public static Track INTRO_TRACK;
	public static Track MENU_TRACK;
	public static Track LEVEL1_TRACK;
	public static Track BOSS_BATTLE_TRACK;
	public static Track BOSS_SCREECH_TRACK;
	
    public AudioEngine() {
	}
	
	@Override
	protected void onLoadEngine(AbstractGameSystems game_systems) {
		super.onLoadEngine(game_systems);
		
		INTRO_TRACK = getStereoTrack("viduus");
		MENU_TRACK = getStereoTrack("main_menu").setLooping(true);
		LEVEL1_TRACK = getStereoTrack("gamejam_level1_theme").setLooping(true);
		BOSS_BATTLE_TRACK = getStereoTrack("gamejam_boss_battle").setLooping(true);
		BOSS_SCREECH_TRACK = getStereoTrack("boss_screech");
	}
}
