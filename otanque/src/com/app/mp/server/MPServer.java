package com.app.mp.server;

/*
 * MPServer - V4.0
 * 
 * Servidor de conexões dos clientes, faz a interface entre novas conexões e o gerênciador
 * 
 * Criadores: Akira Kotsugai e Felipe Menino
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.app.mp.manager.MPManager;

public class MPServer implements Runnable {

	private final int PORT;

	public MPServer(int SPort) {
		PORT = SPort;
	}

	public void initServer() {
		ServerSocket serverMP = null;
		try {
			serverMP = new ServerSocket(PORT);
			System.out.println("Servidor inicialializado em: " + serverMP.getLocalSocketAddress());
			while (true) {
				Socket socket = serverMP.accept();

				/*
				 * Os sets TcpNoDelay e PerformancePreferences ditam mudanças sobre a forma da
				 * comunicação do socket
				 * 
				 * setTcpNoDelay: Desabilita o algoritimo de Nagle, favorecendo a comunicação de
				 * pequenos pacotes
				 * 
				 * setPerformancePreferences(connectiontime, latency, bandwidth); Neste caso (1,
				 * 0, 0) estou preferindo banda alta e baixa latência
				 */
				socket.setTcpNoDelay(true);
				socket.setPerformancePreferences(1, 0, 0);

				new Thread(new MPManager(socket)).start();
				System.out.println("Novo cliente conectado: " + socket.getInetAddress());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		initServer();
	}
}
