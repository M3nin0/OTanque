package com.app.teste;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class TesteSom {

	private static void playSound(File som) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(som));
			clip.start();
			
			Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
