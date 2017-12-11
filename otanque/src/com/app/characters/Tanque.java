package com.app.characters;

/* 
 * Arena - V4.0
 * 
 * Classe que representa o tanque utilizado pelo jogador
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import com.app.ammunition.Missel;
import com.app.maps.Arena;
import com.app.mp.client.MPCliente;
import com.app.weapon.Canhao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Tanque implements Canhao {

	private double life;
	private double x, y;
	private double angulo;
	private double velocidade;
	private int ferido;

	private boolean isAlive;
	private long time;
	private int id;
	private int cid; // ID do ultimo tanque com quem se colidiu.
	private boolean emColisao; // diz se ainda está ou não em colisão
	private String tipo;
	private Color cor;

	private GsonBuilder builder = new GsonBuilder();
	private Gson gson = builder.excludeFieldsWithoutExposeAnnotation().create();

	public Tanque(double x, double y, double angulo, int id) {
		this.life = 350;
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.isAlive = false;
		this.id = id;
		this.cid = id; // inicialmente a ultima colisão é com si mesmo, não afeta o comportamento
		this.emColisao = false;
		this.velocidade = 0;
		this.ferido = 0;
		this.tipo = "user";
	}

	// Construtor dos bots de vida
	public Tanque(double x, double y, double angulo, int id, String tipo, double life) {
		this.life = life;
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.isAlive = false;
		this.id = id;
		this.velocidade = 0;
		this.tipo = tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Color getCor() {
		return cor;
	}

	public void setCor(Color cor) {
		this.cor = cor;
	}

	public String getTipo() {
		return tipo;
	}

	public double getLife() {
		return life;
	}

	public void setLife(double life) {
		this.life = life;
	}

	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}

	public double getAngulo() {
		return angulo;
	}

	public double getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(double velocidade) {

		if (this.tipo.equals("curandeiro")) {
			velocidade = 1;
		} else {
			this.velocidade = velocidade;
		}
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public int getId() {
		return id;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setIsAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public boolean isEmColisao() {
		return emColisao;
	}

	public void setEmColisao(boolean emColisao) {
		this.emColisao = emColisao;
	}

	public void aumentarVelocidade() {

		if (this.tipo.equals("curandeiro")) {
			velocidade = 1;
		} else {
			if (velocidade <= 3)
				velocidade++;
		}
	}

	public void diminuirVelocidade() {
		if (velocidade > 0)
			velocidade--;
	}

	public int getFerido() {
		return ferido;
	}

	public void setFerido(int ferido) {
		this.ferido = ferido;
	}

	public void aumentaVida() {
		this.life += 35;
	}

	public void diminuiVida() {
		this.ferido += 1;
		this.life -= 35;
	}

	public void girarHorario(int angulo) {
		this.angulo += angulo;
		if (this.angulo >= 360)
			this.angulo -= 350;
	}

	public void girarAntiHorario(int angulo) {
		this.angulo -= angulo;
		if (this.angulo <= 0)
			this.angulo = 360 - angulo;
	}

	public void calculaTempo() {
		if (!isAlive) {
			if (System.currentTimeMillis() - time > 5000) {
				if (velocidade > 0)
					velocidade = 2;
				else
					velocidade = -2;
			}
		}
	}

	public void meiaVolta() {
		angulo = anguloOposto(angulo);
	}

	public Shape getRectEnvolvente() {
		AffineTransform at = new AffineTransform();
		at.translate(x, y);
		at.rotate(Math.toRadians(angulo));
		Rectangle rect = new Rectangle(-24, -32, 48, 55);
		return at.createTransformedShape(rect);
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 43, 50);
	}

	public Rectangle radar() {
		return new Rectangle((int) x, (int) y, 96, 96);
	}

	public double anguloOposto(double angulo) {
		if (angulo + 180 < 360)
			return angulo + 180;
		else
			return angulo - 180;
	}

	/*
	 * Representa a arma utilizada pelo tanque
	 */
	public void canhao() {
		int cont = 0;
		int maximo = 2;

		if (this.tipo.equals("bot"))
			maximo = 1;

		for (Missel missel : Arena.safeMissel)
			if (missel.getId() == this.getId())
				cont++;
		if (cont < maximo) {
			Missel temp = new Missel(this.getX(), this.getY(), this.getAngulo(), this.getId());
			Arena.safeMissel.add(temp);
			MPCliente.arrayGson = gson.toJson(temp);
		}
	}

	// Método que criado para a realização da movimentação do tanque
	public void mexer() {
		x += Math.sin(Math.toRadians(angulo)) * velocidade;
		y -= Math.cos(Math.toRadians(angulo)) * velocidade;

		// As estruturas abaixo são utilizadas para
		// realizar o controle do tanque em relação as dimensões
		// da janela
		if (x <= 35) {
			if (angulo >= 270 && angulo < 360)
				angulo = 360 - angulo;

			if (angulo > 180 && angulo <= 270)
				angulo = 360 - angulo;

			if (velocidade < 0) {
				velocidade *= -1;
				girarHorario(5);
			}
		}

		if (x <= 2) {
			this.x = 2;
			girarAntiHorario(90);
		}

		if (y <= 35) {
			if (angulo > 270 && angulo <= 360)
				angulo = 360 - angulo + 180;

			if (angulo >= 0 && angulo < 90)
				angulo = 360 - angulo - 180;

			if (velocidade < 0)
				velocidade *= -1;
		}

		if (y <= 2) {
			this.y = 2;
			girarHorario(90);
		}

		if (y >= 450) {
			if (angulo > 90 && angulo < 180)
				angulo = 360 - angulo - 180;

			if (angulo >= 180 && angulo < 270)
				angulo = 360 - angulo + 180;

			if (velocidade < 0) {
				velocidade *= -1;
				girarAntiHorario(5);
			}
		}
		if (x >= 615) {
			if (angulo > 0 && angulo <= 90)
				angulo = 360 - angulo;

			if (angulo >= 90 && angulo < 180)
				angulo = 360 - angulo;

			if (velocidade < 0)
				velocidade *= -1;
		}
	}

	// Para evitar adições iguais no HashSet
	// equals e hashCode
	@Override
	public boolean equals(Object obj) {
		Tanque tanque = (Tanque) obj;
		if (tanque.getId() == this.getId())
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
