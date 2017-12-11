package com.app.mp.manager;

/*
 * MPManager - V2.0
 * Esta classe é a responsável em realizar o controle das conexões de cliente e servidor
 * e fazer com que todos os clientes recebam as informações do jogo
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class MPManager implements Runnable {
	
	private static int idCont;
	public static HashMap<Socket, String> clientes = new HashMap<>();

	private Socket socket; // Representa o cliente

	private static PrintWriter escreveTodos; // PrintWriter para enviar a todos os usuários

	public MPManager(Socket socket) {
		this.socket = socket;
	}

	public static void enviaParaTodos(String objJson) {
		for (Socket cliente : clientes.keySet()) {
			try {
				// Criando o fluxo de saída
				// O true indica atualiza��o do fluxo de forma automática
				escreveTodos = new PrintWriter(cliente.getOutputStream(), true);
				escreveTodos.println(objJson);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao se conectar com os demais clientes");
				System.exit(1);
			}
		}
	}

	public static void sendNicknames() {
		// Envia o nome dos conectados e o ID para o tanque do player
		enviaParaTodos("#;#;!nck" + clientes.values() + ";" + idCont++);
	}

	@Override
	public void run() {
		String msg = "";
			
		try {			
			// Abrindo fluxo que irá receber as informações
			// O bufferedReader é usado para facilitar a conversão das mensagens recebidas
			BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						
			// Primeiro input que o manager recebe é o nickname de quem se conectou.
			// só depois passa a escutar por mensagens/coordenadas.
			clientes.put(socket, leitor.readLine());
			sendNicknames();
			escreveTodos.flush();

			while ((msg = leitor.readLine()) != null) {
				enviaParaTodos(msg);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro na comunicação com os clientes\n" + e.getMessage());
		}
		System.out.println("Cliente desconectado: " + socket.getInetAddress());
		clientes.remove(socket);
		sendNicknames();

	}
}
