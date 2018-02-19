package org.viduus.charon.gamejam.audio;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.audio.StereoTrack;

public class AudioEngine extends org.viduus.charon.global.audio.AudioEngine{
	
	public static StereoTrack INTRO_TRACK;
	public static StereoTrack MENU_TRACK;
	public static StereoTrack LEVEL1_TRACK;
	public static StereoTrack BOSS_BATTLE_TRACK;
	public static StereoTrack BOSS_SCREECH_TRACK;
	
    public AudioEngine() {
	}
	
	@Override
	protected void onLoadEngine(AbstractGameSystems game_systems) {
		super.onLoadEngine(game_systems);
		
		INTRO_TRACK = getStereoTrack("viduus");
		MENU_TRACK = getStereoTrack("main_menu"); MENU_TRACK.setLooping(true);
		LEVEL1_TRACK = getStereoTrack("gamejam_level1_theme"); LEVEL1_TRACK.setLooping(true);
		BOSS_BATTLE_TRACK = getStereoTrack("gamejam_boss_battle"); BOSS_BATTLE_TRACK.setLooping(true);
		BOSS_SCREECH_TRACK = getStereoTrack("boss_screech");
	}
}
