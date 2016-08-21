package org.exercise.challenge.server;

import static org.exercise.challenge.util.CommonUtil.createBufferReader;
import static org.exercise.challenge.util.Logger.LOG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.exercise.challenge.util.CommonUtil;
import org.exercise.challenge.util.SocketConstant;

public class Server implements Runnable {

	private ServerSocket serverSocketToClientUser;

	private ServerSocket serverSocketToEventSource;

	private Map<Integer, Socket> socketMapping = new ConcurrentHashMap<Integer, Socket>();

	public Server() {
	}

	@Override
	public void run() {
		listenToEventThread().start();
		listenToClientsThread().start();
	}

	public Thread listenToEventThread() {
		return new Thread() {
			public void run() {
				Socket eventSourceSocket = null;
				try {
					serverSocketToEventSource = new ServerSocket(SocketConstant.EVENT_SOURCE_PORT);
					LOG.println("Server started and listening to the events source");

					while (true) {
						eventSourceSocket = serverSocketToEventSource.accept();
						BufferedReader bufferReader = createBufferReader(eventSourceSocket);
						
						String event;
						while ((event = bufferReader.readLine()) != null) {
							Thread.sleep(200);
							forwardEventToCorrespondingClient(event);
						}
					}
				} catch (Exception e) {
					LOG.println("Can not create connection to events source!");
					;
				} finally {
					try {
						serverSocketToEventSource.close();
					} catch (Exception e) {
					}
				}
			}
		};
	}

	public Thread listenToClientsThread() {
		return new Thread() {
			public void run() {
				try {
					serverSocketToClientUser = new ServerSocket(SocketConstant.CLIENT_PORT);
					while (true) {
						Socket socketToClientUser = serverSocketToClientUser.accept();
						BufferedReader bufferReader = createBufferReader(socketToClientUser);
						String userId = bufferReader.readLine();

						if (userId != null) {
							LOG.println("[INFO] New socket connection between server and client %s was created", userId);
							socketMapping.put(Integer.parseInt(userId), socketToClientUser);
						}
					}
				} catch (Exception e) {
					LOG.println("Can not create connection to client!");
				}
			}
		};
	}

	private void forwardEventToCorrespondingClient(String event) throws IOException {
		Integer userId = CommonUtil.extractUserId(event);
		if (userId != null && socketMapping.containsKey(userId)) {
			new PrintWriter(socketMapping.get(userId).getOutputStream(), true).println(event);
		} else {
			LOG.println("Connection to user %s is not existing", userId != null ? userId.toString() : "empty");
		}
	}

}
