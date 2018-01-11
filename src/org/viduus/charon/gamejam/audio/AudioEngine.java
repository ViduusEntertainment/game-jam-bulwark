package org.viduus.charon.gamejam.audio;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.AbstractGameSystems.PauseType;
import org.viduus.charon.global.GameInfo;

import kuusisto.tinysound.TinySound;

public class AudioEngine extends org.viduus.charon.global.audio.AudioEngine{

	@Override
	protected void onLoadEngine(AbstractGameSystems game_systems) {
		TinySound.init();
	}
	@Override
	protected void onLoadGame(GameInfo game_info) {}
	@Override
	protected void onSaveAndDisposeGame() {}
	@Override
	protected void onSaveAndDisposeEngine() {
		TinySound.shutdown();
	}
	@Override
	public void onStartGame() {}
	@Override
	public void onPauseGame(PauseType pause_type) {}
	@Override
	public void onContinueGame() {}
	@Override
	public void onStopGame() {}
}
