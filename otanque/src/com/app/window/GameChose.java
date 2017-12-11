package com.app.window;

/* 
 * Arena - V0.3
 * 
 * Classe da janela de escolha do tipo de jogo
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.app.chat.ChatClientGui;
import com.app.mp.server.MPServer;

@SuppressWarnings("serial")
public class GameChose extends JFrame {

	private JPanel contentPane;

	public GameChose() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnConnectServer = new JButton("Conectar em um servidor");
		btnConnectServer.setBackground(Color.decode("#ffeed8"));
		btnConnectServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] infos = new String[2];
				String infoServer = JOptionPane.showInputDialog("Insira o IP:Porta do servidor");
				String nick = JOptionPane.showInputDialog("Insira seu nickname");
				ChatClientGui.username = nick;
				infos = infoServer.split(":");
				setVisible(false);
				new GameWindow(2, infos[0], Integer.parseInt(infos[1]), 0);
			}
		});
		btnConnectServer.setBounds(110, 48, 217, 45);
		contentPane.add(btnConnectServer);

		JButton btnHostServer = new JButton("Ser um servidor");
		btnHostServer.setBackground(Color.decode("#ffeed8"));
		btnHostServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int porta = Integer.parseInt(JOptionPane.showInputDialog("Insira a porta do servidor"));
				String nick = JOptionPane.showInputDialog("Insira seu nickname");
				ChatClientGui.username = nick;

				new Thread(new MPServer(porta)).start();
				
				setVisible(false);
				new GameWindow(2, "127.0.0.1", porta, 1);

			}
		});
		btnHostServer.setBounds(150, 139, 143, 45);
		contentPane.add(btnHostServer);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new GameMenu().setVisible(true);;
			}
		});
		btnVoltar.setBackground(Color.decode("#ffeed8"));
		btnVoltar.setBounds(12, 238, 114, 25);
		contentPane.add(btnVoltar);

		setVisible(true);
	}
}
