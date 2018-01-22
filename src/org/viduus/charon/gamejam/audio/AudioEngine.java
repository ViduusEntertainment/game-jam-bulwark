package org.viduus.charon.gamejam.audio;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.AbstractGameSystems.PauseType;
import org.viduus.charon.global.GameInfo;
import org.viduus.charon.global.util.ResourceLoader;
import org.viduus.charon.global.util.logging.ErrorHandler;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class AudioEngine extends org.viduus.charon.global.audio.AudioEngine{

	public static Sound INTRO_SOUND;
	public static Music MENU_MUSIC;
	public static Music LEVEL1_MUSIC;
	public static Sound KAMIKAZE_SOUND;
	public static Sound EXPLOSION_SOUND;
	public static Sound BASIC_BULLET_SOUND;
	public static Sound MISSILE_SOUND;
	public static Sound SCATTERSHOT_SOUND;
	public static Sound LASER_CHARGE_SOUND; 
	public static Sound BOSS_SCREECH_SOUND; 
	public static Sound EMP_SOUND;
	public static Sound BOSS_ORB; 
	public static Sound BOSS_LASER;
	public static Music BOSS_BATTLE_MUSIC;
	
    public AudioEngine() {
	}
	
	@Override
	protected void onLoadEngine(AbstractGameSystems game_systems) {
		TinySound.init();
		
		INTRO_SOUND = ErrorHandler.tryRunThrow(() -> TinySound.loadSound(ResourceLoader.loadResource("resources/audio/sfx/intro/viduus.ogg")));
		MENU_MUSIC = TinySound.loadMusic(ErrorHandler.tryRunThrow(() -> ResourceLoader.loadResourceWithError("resources/audio/music/menu/main_menu.ogg")));
		
		new Thread(new Runnable() {
			public void run() {
				ErrorHandler.tryRunThrow(() -> {
					LEVEL1_MUSIC = TinySound.loadMusic(ErrorHandler.tryRunThrow(() -> ResourceLoader.loadResourceWithError("resources/audio/music/game/gamejam_level1_theme.ogg")));
					KAMIKAZE_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/kamikaze.ogg"));
					EXPLOSION_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/explosion.ogg"));
					BASIC_BULLET_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/basic_bullet.ogg"));
					MISSILE_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/missile.ogg"));
					SCATTERSHOT_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/scattershot.ogg"));
					LASER_CHARGE_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/charge_laser.ogg"));
					BOSS_SCREECH_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/boss_screech.ogg"));
					EMP_SOUND = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/EMP.ogg"));
					BOSS_ORB = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/boss_orb.ogg"));
					BOSS_LASER = TinySound.loadSound(ResourceLoader.loadResourceWithError("resources/audio/sfx/game/boss_laser.ogg"));
					BOSS_BATTLE_MUSIC = TinySound.loadMusic(ErrorHandler.tryRunThrow(() -> ResourceLoader.loadResourceWithError("resources/audio/music/game/gamejam_boss_battle.ogg")));
				});
			}
		}).start();
	}
	
	@Override
	protected void onLoadGame(GameInfo game_info) {
		
	}
	
	@Override
	protected void onSaveAndDisposeGame() {
		
	}
	
	@Override
	protected void onSaveAndDisposeEngine() {
		INTRO_SOUND.unload();
		MENU_MUSIC.unload();
		LEVEL1_MUSIC.unload();
		KAMIKAZE_SOUND.unload();
		EXPLOSION_SOUND.unload();
		BASIC_BULLET_SOUND.unload();
		MISSILE_SOUND.unload();
		SCATTERSHOT_SOUND.unload();
		LASER_CHARGE_SOUND.unload();
		BOSS_SCREECH_SOUND.unload();
		EMP_SOUND.unload();
		BOSS_BATTLE_MUSIC.unload();
		TinySound.shutdown();
	}
	
	@Override
	public void onStartGame() {
		
	}
	
	@Override
	public void onPauseGame(PauseType pause_type) {
		
	}
	
	@Override
	public void onContinueGame() {
		
	}
	
	@Override
	public void onStopGame() {
		
	}
	
}
