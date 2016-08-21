package org.exercise.challenge.client;

import static org.exercise.challenge.util.Logger.LOG;
import static org.exercise.challenge.util.CommonUtil.createBufferReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.exercise.challenge.util.SocketConstant;

public class UserClient implements Runnable {

	private int userId;
	private Socket socket;

	public UserClient() {
	}

	public UserClient(int userId) {
		this.userId = userId;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(SocketConstant.HOST, SocketConstant.CLIENT_PORT);
			new PrintWriter(socket.getOutputStream(), true).println(userId);
			while (true) {
				BufferedReader bufferReader = createBufferReader(socket);
				String message = bufferReader.readLine();
				LOG.println("User " + userId + " received event : " + message);
			}
		} catch (IOException exception) {
			LOG.println("[ERROR] Could not create connection to server");
		}
	}

}
