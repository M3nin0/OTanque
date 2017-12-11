package com.app.maps;

/* 
 * Arena - V6.2
 * 
 * Classe da arena onde o jogo ocorre
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.awt.BasicStroke;

/* 
 * Arena - V3.1
 * 
 * Classe que representa a arena que será utilizada pelo jogo
 *
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;

import com.app.ammunition.Missel;
import com.app.characters.Tanque;
import com.app.effects.Colisao;
import com.app.effects.Som;
import com.app.mp.client.MPCliente;
import com.app.window.GameWindow;

@SuppressWarnings("serial")
public class Arena extends JComponent implements MouseListener, ActionListener, KeyListener, Colisao {

	private MPCliente mpCliente;

	// Collections.synchronizedSet foi criado para evitar problemas com thread
	public static Set<Tanque> safeTanques;
	public static Set<Missel> safeMissel;

	// String builder para alocar informações do tanque
	private StringBuilder stringTanque;

	private Tanque apontado;
	private int largura, altura;
	private Timer contador;
	private long agora;
	private Image bg;
	private String txtVida;
	private double intVida;
	private int type; // Type(0) == Single player; Type(1) == Multiplayer

	public Arena(int largura, int altura, int type) {
		this.altura = altura;
		this.largura = largura;
		this.type = type;

		mpCliente = new MPCliente();

		// Aplicação de sincronização (Thread-safe)
		// Evita problemas nas operações simultâneas
		safeTanques = Collections.synchronizedSet(new HashSet<Tanque>());
		safeMissel = Collections.synchronizedSet(new HashSet<Missel>());

		stringTanque = new StringBuilder();

		// Adiciona os ouvintes de eventos
		addMouseListener(this);
		addKeyListener(this);

		bg = new ImageIcon("res/bg_r.jpg").getImage();

		setFocusable(true);

		// Iniciando contador do som
		Som.InitSound(new File("res/stage_sound.wav"));

		// Utilizado para realizar as atualizações
		contador = new Timer(40, this);
		contador.start();
	}

	public static void addMisseis(Missel e) {
		safeMissel.add(e);
	}

	public static void addTanque(Tanque tanque) {
		safeTanques.add(tanque);
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getPreferredSize() {
		return new Dimension(largura, altura);
	}

	// Trata das colisões entre os tanques
	public void autoColisao(Tanque tanque) {
		for (Tanque t : safeTanques) {
			if (tanque.getId() != t.getId()) {
				double distancia = Math
						.sqrt(Math.pow(tanque.getX() - t.getX(), 2) + Math.pow(tanque.getY() - t.getY(), 2));

				// Tanque inimigo tenta fugir
				if (distancia < 80 && tanque.isAlive()) {
					t.setTime(System.currentTimeMillis());
					if (agora % 2 == 0)
						t.girarAntiHorario(5);
					else
						t.girarHorario(5);
					t.setVelocidade(6);
				}

				// O algoritmo abaixo sempre verifica se após 2 tanques se colidirem eles ainda
				// estão em colisão
				// eles só dão meia volta se depois que bater e virar, eles não estiverem mais
				// em colisão.

				// Quando a distância for menor que 30 (houver colisão)
				if (distancia <= 30) {

					// Se t não foi a ultima colisão do tanque
					if (tanque.getCid() != t.getId()) {
						tanque.meiaVolta(); // tanque dá meia volta
						tanque.setCid(t.getId()); // t se torna a ultima colisao do tanque
						tanque.setEmColisao(true);
					}

					else { // se t foi a ultima colisão do tanque
						if (tanque.getCid() == t.getId()) {
							// se o tanque não está mais em colisão
							if (!tanque.isEmColisao()) {
								tanque.meiaVolta(); // tanque dá meia volta
								tanque.setEmColisao(true); // tanque entra em colisão
							}
						}
					}
				}
				// quando a distancia entre Tanque e t for maior que 30
				else {
					// Se t foi o ultimo a colidir com o tanque
					if (tanque.getCid() == t.getId()) {
						// Se o tanque ainda está em colisão
						if (tanque.isEmColisao()) {
							tanque.setEmColisao(false); // o tanque passa a não estar mais em colisão
						}
					}
				}
			}
		}
	}

	public void AI(Missel missel) {
		for (Tanque tanque : safeTanques) {
			if (tanque.getId() != missel.getId()) {
				double distancia = Math
						.sqrt(Math.pow(tanque.getX() - missel.getX(), 2) + Math.pow(tanque.getY() - missel.getY(), 2));

				if (distancia <= 100) {
					int random = new Random().nextInt(15);

					if (agora % 2 == 0)
						tanque.girarHorario((int) (Math.PI * random) / 9);
					else
						tanque.girarAntiHorario((int) (Math.E) * random / 15);

					tanque.setVelocidade(6);
				}
			}
		}

	}

	// Método para verificar colisão
	public void colisao() {
		Tanque bonus = null;
		Missel m = null;
		for (Missel missel : safeMissel) {
			for (Tanque tanque : safeTanques) {
				if (tanque.getId() != missel.getId()) {
					if (missel.getBounds().intersects(tanque.getBounds())) {
						m = missel;

						/*
						 * Verifica se quem acertou deve receber vida
						 */
						if (tanque.getTipo().equals("curandeiro")) {
							for (Tanque tt : safeTanques)
								if (tt.getId() == m.getId())
									bonus = tt;
						}
						tanque.diminuiVida();
						autoColisao(tanque);
					}
				}
				AI(missel);
			}
			if (!(bonus == null))
				bonus.aumentaVida();
		}
		safeMissel.remove(m);
	}

	/*
	 * 
	 * Método chamado pelo repaint(), responsável em pintar todas as cores e objetos
	 */
	public void paint(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(245, 245, 255));
		g2d.fillRect(0, 0, largura, altura);
		g2d.setColor(new Color(220, 220, 220));

		g2d.drawImage(bg, 0, 0, null);

		Tanque t = null;
		for (Tanque tanque : safeTanques) {
			if (tanque.getLife() > 0) {
				drawTanque(tanque, g2d);
			} else
				t = tanque;
		}

		safeTanques.remove(t);

		for (Tanque tanque : safeTanques)
			autoColisao(tanque);

		for (Missel missel : safeMissel)
			drawMissel(missel, g2d);

		g2d.setColor(Color.WHITE);
		g2d.drawString("Quantidade de tanques no jogo: " + safeTanques.size(), 370, 20);

		g.dispose();
	}

	public void mouseClicked(MouseEvent e) {
		for (Tanque t : safeTanques) {
			if (!(t.getTipo().equals("curandeiro")))
				t.setIsAlive(false);
		}
		for (Tanque t : safeTanques) {
			boolean selecionado = t.getRectEnvolvente().contains(e.getX(), e.getY());
			if (selecionado && t.getTipo().equals("user") && (t.getTipo().equals("curandeiro")) == false) {
				t.setIsAlive(true);
				apontado = t;
			}
		}
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		for (Tanque t : safeTanques) {

			// Gravando informações para enviar para o servidor
			// utilizado este método por em testes, o jogo ficar mais rápido
			// deste modo

			if (type == 1) {

				t.mexer();
				t.calculaTempo();

				if (t.getId() == GameWindow.copia || t.getTipo().equals("curandeiro") || t.getFerido() > 0) {

					stringTanque.append(t.getLife());
					stringTanque.append(",");
					stringTanque.append(t.getX());
					stringTanque.append(",");
					stringTanque.append(t.getY());
					stringTanque.append(",");
					stringTanque.append(t.getAngulo());
					stringTanque.append(",");
					stringTanque.append(t.getVelocidade());
					stringTanque.append(",");
					stringTanque.append(t.isAlive());
					stringTanque.append(",");
					stringTanque.append(t.getId());
					stringTanque.append(",");
					stringTanque.append(t.getCid());
					stringTanque.append(",");
					stringTanque.append(t.isEmColisao());
					stringTanque.append(",");
					stringTanque.append(t.getTipo());

					mpCliente.sender(stringTanque.toString());
					stringTanque.setLength(0);
					t.setFerido(0);
				}
			} else {
				t.mexer();
				t.calculaTempo();

				if (t.getTipo().equals("bot"))
					t.canhao();
			}
		}
		colisao();

		for (Iterator<Missel> i = safeMissel.iterator(); i.hasNext();) {
			Missel element = i.next();
			element.mexer();
			if (!element.isVisible())
				i.remove();
		}
		repaint();
	}

	public void keyPressed(KeyEvent e) {
		for (Tanque t : safeTanques) {
			t.setIsAlive(false);
			if (t == apontado) {
				t.setIsAlive(true);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					t.girarAntiHorario(22);
					break;
				case KeyEvent.VK_UP:
					t.aumentarVelocidade();
					break;
				case KeyEvent.VK_DOWN:
					t.diminuirVelocidade();
					break;
				case KeyEvent.VK_RIGHT:
					t.girarHorario(22);
					break;
				case KeyEvent.VK_SPACE: {
					t.canhao();
					agora = System.currentTimeMillis();
				}
					break;
				}
				break;
			}
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void drawMissel(Missel missel, Graphics2D g2d) {
		// Armazenamos o sistema de coordenadas original.
		AffineTransform antes = g2d.getTransform();

		// Criamos um sistema de coordenadas para o tanque.
		AffineTransform depois = new AffineTransform();
		depois.translate(missel.getX(), missel.getY());
		depois.rotate(Math.toRadians(missel.getAngulo()));

		// Aplicamos o sistema de coordenadas.
		g2d.transform(depois);

		// Desenhamos o missil
		missel.setImg(new ImageIcon("res/weapon_missel.gif").getImage());
		g2d.drawImage(missel.getImg(), 0, 0, null);

		// Aplicamos o sistema de coordenadas
		g2d.setTransform(antes);
	}

	// Método para desenhar o tanque
	public void drawTanque(Tanque tanque, Graphics2D g2d) {

		// Armazenamos o sistema de coordenadas original.
		AffineTransform antes = g2d.getTransform();

		// Criamos um sistema de coordenadas para o tanque.
		AffineTransform depois = new AffineTransform();
		depois.translate(tanque.getX(), tanque.getY());
		depois.rotate(Math.toRadians(tanque.getAngulo()));

		// Aplicamos o sistema de coordenadas.
		g2d.transform(depois);

		// Desenhamos o tanque. Primeiro o corpo
		if (tanque.getTipo().equals("user"))
			g2d.setColor(Color.BLUE);
		else
			g2d.setColor(Color.RED);
		g2d.fillRect(-10, -12, 20, 24);

		// Agora as esteiras
		for (int i = -12; i <= 8; i += 4) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(-15, i, 5, 4);
			g2d.fillRect(10, i, 5, 4);
			g2d.setColor(Color.BLACK);
			g2d.fillRect(-15, i, 5, 4);
			g2d.fillRect(10, i, 5, 4);
		}

		// O canhão
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(-3, -25, 6, 25);

		g2d.setColor(Color.BLUE);
		g2d.drawRect(-3, -25, 6, 25);

		// Se o tanque estiver ativo
		// Desenhamos uma margem
		if (tanque.isAlive()) {
			g2d.setColor(new Color(120, 120, 120));
			Stroke linha = g2d.getStroke();
			g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 8 }, 0));
			g2d.drawRect(-24, -32, 49, 59);
			g2d.setStroke(linha);

			// Sistema para adicionar vida ao tanque
			g2d.setColor(Color.BLUE);
			intVida = tanque.getLife();
			if (tanque.getLife() >= 350) {
				txtVida = "|||||||";
			} else if (intVida >= 250 && intVida <= 300) {
				txtVida = "||||||";
			} else if (intVida >= 200 && intVida <= 249) {
				txtVida = "|||||";
			} else if (intVida >= 150 && intVida <= 199) {
				g2d.setColor(Color.YELLOW);
				txtVida = "||||";
			} else if (intVida >= 100 && intVida <= 149) {
				g2d.setColor(Color.YELLOW);
				txtVida = "|||";
			} else if (intVida >= 50 && intVida <= 99) {
				g2d.setColor(Color.RED);
				txtVida = "||";
			} else if (intVida >= 10 && intVida <= 49) {
				g2d.setColor(Color.RED);
				txtVida = "|";
			} else {
				txtVida = "";
			}
			g2d.drawString(txtVida, 10, 29);
		} else if (tanque.getTipo().equals("curandeiro"))
			g2d.drawString("Curandeiro", 17, 20);
		else if (tanque.getTipo().equals("bot"))
			g2d.drawString("Bot", 17, 20);
		else
			g2d.drawString("Livre", 17, 20);

		// Aplicamos o sistema de coordenadas
		g2d.setTransform(antes);
	}
}
