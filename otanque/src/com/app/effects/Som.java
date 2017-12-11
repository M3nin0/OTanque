package com.app.effects;

/*
 * Som - V0.9
 * 
 * Classe para o efeito de som no jogo
 * 
 * OBS: Utiliza o clip para evitar utilização de outras bibliotecas para ativar o MP3
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class Som {

	private static void playSound(File som) {
		
		/*
		 * A interface clip foi usado para que o som seja carregado antes 
		 * da execução. Isso deixou o jogo mais leve que as demais formas que foram testadas
		 */
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(som));
			clip.start();

			/*
			 * Faz o som ser reproduzido por completo
			 */
			Thread.sleep(clip.getMicrosecondLength());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao reproduzir o som do jogo");
		}
	}

	public static void InitSound(File som) {
		new Thread() {
			@Override
			public void run() {
				playSound(som);
			}
		}.start();
	}
}
