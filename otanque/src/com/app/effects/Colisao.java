package com.app.effects;

/*
 * Colisão - V0.2
 * 
 * Interface será implementada quando for necessário criar colisões no cenário
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import com.app.characters.Tanque;

public interface Colisao {
	public void colisao();
	public void autoColisao(Tanque tanque);
}
