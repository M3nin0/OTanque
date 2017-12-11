package com.app.window;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.app.effects.Som;

@SuppressWarnings("serial")
public class GameMenu extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Som.InitSound(new File("res/menu_sound.wav"));
					GameMenu frame = new GameMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	public GameMenu() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 645, 428);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnSinglePlayer = new JButton("Um Jogador");
		btnSinglePlayer.setBackground(Color.decode("#ffeed8"));
		btnSinglePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				new GameWindow(1, "", 0, 0);
			}
		});
		btnSinglePlayer.setBounds(42, 215, 151, 39);
		contentPane.add(btnSinglePlayer);

		JButton btnMP = new JButton("Multijogadores");
		btnMP.setBackground(Color.decode("#ffeed8"));
		btnMP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new GameChose();
			}
		});
		btnMP.setBounds(42, 285, 151, 39);
		contentPane.add(btnMP);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("res/tanque_logo.png"));
		lblNewLabel.setBounds(272, 12, 327, 290);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("res/mini_tanque_menu.gif"));
		lblNewLabel_1.setBounds(54, 12, 164, 191);
		contentPane.add(lblNewLabel_1);
	}
}
