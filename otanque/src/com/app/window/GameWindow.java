package com.app.window;

/* 
 * Arena - V3.0
 * 
 * Classe que representa a janela do jogo
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import javax.swing.JFrame;

import com.app.characters.Tanque;
import com.app.chat.ChatClientGui;
import com.app.maps.Arena;
import com.app.mp.client.MPCliente;

public class GameWindow {

	public static int id; // ID que será atribuido ao tanque do player
	public static int copia; // Forma de persistência do ID do tanque

	public static void genWindow(int largura, int altura, int type) {
		Arena arena = new Arena(largura, altura, type);

		JFrame janela = new JFrame("OTank");
		janela.getContentPane().add(arena);
		janela.setResizable(false);
		janela.pack();
		janela.setVisible(true);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public GameWindow(int opt, String IP, int porta, int server) {

		if (opt == 1) {
			genWindow(640, 480, 0);
			Arena.addTanque(new Tanque(200, 150, 100, 9999, "curandeiro", 9999));
			Arena.addTanque(new Tanque(200, 50, 90, 4, "bot", 200));
			Arena.addTanque(new Tanque(100, 120, 270, 5, "bot", 200));
			// Arena.addTanque(new Tanque(180, 307, 34, 6, "bot"));
			Arena.addTanque(new Tanque(340, 205, 54, 7));
			Arena.addTanque(new Tanque(360, 40, 43, 8));
			Arena.addTanque(new Tanque(180, 319, 321, 9));
			// Arena.addTanque(new Tanque(45, 229, 123, 10));
			// Arena.addTanque(new Tanque(333, 111, 33, 11));
			// Arena.addTanque(new Tanque(122, 222, 22, 12));
		}

		// Criando Cliente
		if (opt == 2) {

			genWindow(640, 480, 1);

			new MPCliente(IP, porta).init();
			ChatClientGui chat = new ChatClientGui();
			chat.Menu();
			chat.initialize();

			if (server == 1) {
				Arena.addTanque(new Tanque(200, 150, 100, 99, "curandeiro", 9999));
				Arena.addTanque(new Tanque(230, 80, 70, 98, "curandeiro", 9999));
			}
			copia = id;
			Arena.addTanque(new Tanque(350, 50, 180, copia));
		}
	}
}
