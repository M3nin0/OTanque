package com.app.chat;

/* 
 * Arena - V2.7
 * 
 * Janela do chat
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.awt.Color;

/*
 * ChatClientGui - V1.5
 * 
 * Classe que da janela do chat
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.app.mp.client.MPCliente;

public class ChatClientGui {

	public static String username;

	public static JFrame mainWindow = new JFrame();
	private static JButton btnSend = new JButton();
	public static JTextField tfMessage = new JTextField(20);
	public static JTextArea taConversation = new JTextArea();
	private static JScrollPane conversation = new JScrollPane();
	private static JLabel lblOnline = new JLabel();
	public static JList jlOnline = new JList();
	private static JScrollPane playersOnline = new JScrollPane();
	private static JLabel lLoggedInAs = new JLabel();
	private static JLabel lLoggedInAsBox = new JLabel();

	public static void initialize() {

		btnSend.setEnabled(true);
		lLoggedInAsBox.setText("Usuário: " + username);
	}

	public static void mainWindowAction() {

		btnSend.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendMsg();
			}
		});

	}

	public static void sendMsg() {
		if (!tfMessage.getText().trim().equals("")) {
			MPCliente.chatmessage = "!msg" + username + " diz: " + tfMessage.getText();
			tfMessage.setText("");
			tfMessage.requestFocus();
		}
	}

	public static void Menu() {
		mainWindow.setTitle("Chat");
		mainWindow.setSize(450, 500);
		mainWindow.setLocation(220, 180);
		mainWindow.setResizable(false);
		configureMainWindow();
		mainWindowAction();
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void configureMainWindow() {

		mainWindow.setBackground(new java.awt.Color(255, 255, 255));
		mainWindow.setSize(615, 387);
		mainWindow.getContentPane().setLayout(null);

		btnSend.setBackground(Color.decode("#ffeed8"));
		btnSend.setForeground(Color.decode("#2c3e50"));
		btnSend.setText("Enviar");
		mainWindow.getContentPane().add(btnSend);
		btnSend.setBounds(436, 309, 130, 38);

		tfMessage.setForeground(Color.decode("#2c3e50"));
		tfMessage.requestFocus();
		mainWindow.getContentPane().add(tfMessage);
		tfMessage.setBounds(10, 320, 382, 30);

		taConversation.setColumns(20);
		taConversation.setFont(new java.awt.Font("Tahoma", 0, 12));
		taConversation.setForeground(Color.decode("#2c3e50"));
		taConversation.setLineWrap(true);
		taConversation.setRows(5);
		taConversation.setEditable(false);

		conversation.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		conversation.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		conversation.setViewportView(taConversation);
		mainWindow.getContentPane().add(conversation);
		conversation.setBounds(10, 22, 382, 271);

		lblOnline.setHorizontalAlignment(SwingConstants.CENTER);
		lblOnline.setText("Jogadores online");
		lblOnline.setToolTipText("");
		mainWindow.getContentPane().add(lblOnline);
		lblOnline.setBounds(436, 55, 130, 16);

		lblOnline.setForeground(Color.decode("#2c3e50"));

		playersOnline.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		playersOnline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		playersOnline.setViewportView(jlOnline);
		mainWindow.getContentPane().add(playersOnline);
		playersOnline.setBounds(429, 83, 150, 180);

		lLoggedInAsBox.setHorizontalAlignment(SwingConstants.CENTER);
		lLoggedInAsBox.setFont(new java.awt.Font("Tahoma", 0, 12));
		lLoggedInAsBox.setForeground(Color.decode("#7f8c8d"));
		lLoggedInAsBox.setBorder(BorderFactory.createLineBorder(Color.decode("#ffeed8")));
		mainWindow.getContentPane().add(lLoggedInAsBox);
		lLoggedInAsBox.setBounds(429, 10, 150, 20);
	}
}
