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

	private ServerSocket socketConnectionToClientUser;

	private ServerSocket socketConnectionToEventSource;

	private Map<Integer, Socket> socketMapping = new ConcurrentHashMap<Integer, Socket>();

	public Server() {
	}

	@Override
	public void run() {
		listenToEventSourceThread().start();
		listenToClientsThread().start();
	}

	public Thread listenToEventSourceThread() {
		return new Thread() {
			public void run() {
				Socket eventSourceSocket = null;
				try {
					socketConnectionToEventSource = new ServerSocket(SocketConstant.EVENT_SOURCE_PORT);
					LOG.println("Server started and listening to the events source");

					while (true) {
						eventSourceSocket = socketConnectionToEventSource.accept();
						BufferedReader bufferReader = createBufferReader(eventSourceSocket);
						
						String event;
						while ((event = bufferReader.readLine()) != null) {
							Thread.sleep(200);
							forwardEventToCorrespondingClient(event);
						}
					}
				} catch (Exception e) {
					LOG.println("[ERROR] Can not create connection to events source!");
				} finally {
					try {
						socketConnectionToEventSource.close();
					} catch (Exception e) {
						LOG.println("[ERROR] Can not close the connection to events source!");
					}
				}
			}
		};
	}

	public Thread listenToClientsThread() {
		return new Thread() {
			public void run() {
				try {
					socketConnectionToClientUser = new ServerSocket(SocketConstant.CLIENT_PORT);
					while (true) {
						Socket socketToClientUser = socketConnectionToClientUser.accept();
						BufferedReader bufferReader = createBufferReader(socketToClientUser);
						String userId = bufferReader.readLine();

						if (userId != null) {
							LOG.println("[INFO] New socket connection between server and client %s was created", userId);
							
							//Keep socket in a map and reuse it when server have to sends event
							socketMapping.put(Integer.parseInt(userId), socketToClientUser);
						}
					}
				} catch (Exception e) {
					LOG.println("[ERROR] Can not create connection to client!");
				}
			}
		};
	}

	private void forwardEventToCorrespondingClient(String event) throws IOException {
		Integer userId = CommonUtil.extractUserId(event);
		
		//Find corresponding socket connection in socketMap and send event to it
		if (userId != null && socketMapping.containsKey(userId)) {
			new PrintWriter(socketMapping.get(userId).getOutputStream(), true).println(event);
		} else {
			LOG.println("Connection to user %s is not existing", userId != null ? userId.toString() : "empty");
		}
	}

}
