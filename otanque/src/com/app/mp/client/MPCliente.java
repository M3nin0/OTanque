package com.app.mp.client;

/* 
 * MPCliente - V6.0
 * 
 * Classe do cliente para o jogo multiplayer
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.app.ammunition.Missel;
import com.app.characters.Tanque;
import com.app.chat.ChatClientGui;
import com.app.maps.Arena;
import com.app.window.GameWindow;
import com.google.gson.Gson;

public class MPCliente {

	/*
	 * Estáticos para que as informações dos atributos sejam compartilhados
	 */
	private static Socket socket;
	private static PrintWriter writer;
	private static BufferedReader reader;
	private static StringBuilder sb;
	public static String arrayGson = "";
	public static String chatmessage = "";

	private String[] infos;
	private String[] dados;
	private String message;

	private Gson gson = new Gson();

	public MPCliente() {

	}

	public MPCliente(String SERVER_ADDR, int PORT_SERVER) {

		/*
		 * StringBuilder para melhorar desempenho ao concatenar as strings no método
		 * sender
		 */
		sb = new StringBuilder();

		try {
			socket = new Socket(SERVER_ADDR, PORT_SERVER);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Servidor não encontrado!");

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro");
		}
	}

	/*
	 * Este método é resposável por enviar as informações para o servidor;
	 * 
	 * Ele verifica qual informação está disponivel para ele enviar e concatena,
	 * depois faz o envio.
	 */

	public void sender(String obj) {
		if (!obj.trim().equals("")) {
			sb.append(obj);
		} else
			sb.append("#");

		if (!arrayGson.trim().equals("")) {
			sb.append(";");
			sb.append(arrayGson);
		} else
			sb.append(";#");

		if (!chatmessage.trim().equals("")) {
			sb.append(";");
			sb.append(chatmessage);
		} else
			sb.append(";#");

		sb.append(";#");

		writer.println(sb.toString());

		arrayGson = "";
		chatmessage = "";
		sb.setLength(0);
	}

	/*
	 * Este método recebe as informações e aplica aos tanques
	 */
	public void receiver() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			infos = new String[4];
			dados = new String[11];

			while (true) {

				infos = reader.readLine().split(";");
				dados = infos[0].split(",");

				if (!infos[0].equals("#")) {
					Tanque temp = new Tanque(Double.parseDouble(dados[1]), Double.parseDouble(dados[2]),
							Double.parseDouble(dados[3]), Integer.parseInt(dados[6]));
					Arena.addTanque(temp);
					for (Tanque tanque : Arena.safeTanques) {
						if (tanque.getId() == Integer.parseInt(dados[6])) {
							if (tanque.getFerido() == 0)
								tanque.setLife(Double.parseDouble(dados[0]));				
							tanque.setX(Double.parseDouble(dados[1]));
							tanque.setY(Double.parseDouble(dados[2]));
							tanque.setAngulo(Double.parseDouble(dados[3]));
							tanque.setVelocidade(Double.parseDouble(dados[4]));
							tanque.setAlive(Boolean.parseBoolean(dados[5]));
							tanque.setCid(Integer.parseInt(dados[7]));
							tanque.setEmColisao(Boolean.parseBoolean(dados[8]));
							tanque.setTipo(dados[9]);
						}
					}
				}
				if (!infos[1].equals("#")) {
					Missel temp = gson.fromJson(infos[1], Missel.class);
					Arena.safeMissel.add(temp);
				}
				if (!infos[2].equals("#")) {
					if (infos[2].contains("!nck")) {
						message = infos[2].substring(4);
						message = message.replace("[", "");
						message = message.replace("]", "");
						String[] current_users = message.split(", ");
						ChatClientGui.jlOnline.setListData(current_users);
					}
					if (infos[2].contains("!msg")) {
						message = infos[2].substring(4);
						ChatClientGui.taConversation.append(message + "\n");
					}
				}
				// Recebe o ID do tanque que poderá ser usado
				if (!infos[3].equals("#")) {
					GameWindow.id = Integer.parseInt(infos[3]) + 2;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao receber informações do servidor");
		}
	}

	public void init() {
		// primeira escrita do cliente é passar o nickname,
		// só depois inicia-se as threads de comunicação.
		writer.println(ChatClientGui.username);

		// Thread para receber informações
		new Thread() {
			@Override
			public void run() {
				receiver();
			}
		}.start();
	}
}
