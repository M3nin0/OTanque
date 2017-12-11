package com.app.ammunition;

/*
 * Missel - V1.8.2
 * 
 * Classe que faz a representação da bala utilizada pelas armas
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import com.google.gson.annotations.Expose;

public class Missel {

	/*
	 * serialização/deserialização (Obj-Json / Json-Obj)
	 * 
	 * excludeFieldsWithoutExposeAnnotation(), faz com que apenas os campos marcados
	 * com @Expose seja serializado
	 */
	@Expose(serialize = false, deserialize = false)
	private Image img;
	@Expose(serialize = true, deserialize = true)
	private double x, y;
	@Expose(serialize = true, deserialize = true)
	private double angulo;
	@Expose(serialize = true, deserialize = true)
	private int id;
	@Expose(serialize = true, deserialize = true)
	private boolean isVisible = true;

	public Missel(double x, double y, double angulo, int id) {
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.id = id;

		img = new ImageIcon("res/weapon_missel.gif").getImage();
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Image getImg() {
		return img;
	}

	public double getAngulo() {
		return angulo;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 40, 40);
	}

	public void mexer() {
		if (isVisible) {
			x += Math.sin(Math.toRadians(angulo)) * 10;
			y -= Math.cos(Math.toRadians(angulo)) * 10;
		}
		// Caso esteja fora da tela
		if (x < 5 || x > 645 || y < -5 || y > 485)
			isVisible = false;
	}

}
