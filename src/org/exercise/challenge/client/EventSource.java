package org.exercise.challenge.client;

import static org.exercise.challenge.util.Logger.LOG;

import java.io.PrintWriter;
import java.net.Socket;

import org.exercise.challenge.util.RandomGenerateUtil;
import org.exercise.challenge.util.SocketConstant;

public class EventSource implements Runnable {
	private Socket socket;
	private static final int NUMBER_EVENT = 4;
	private static final int NUMBER_BLOCK = 3;

	@Override
	public void run() {
		int i = 0;
		try {
			socket = new Socket(SocketConstant.HOST, SocketConstant.EVENT_SOURCE_PORT);
			LOG.println("[INFO] Start the simulation of event handling system!");
			LOG.println("[INFO] Event source will send "+NUMBER_BLOCK+" blocks of events to server, each block has "
					+NUMBER_EVENT+" random events");
			while (i < NUMBER_BLOCK) {
				String eventRows = RandomGenerateUtil.createRandomEventData(NUMBER_EVENT);
				waiting(3000);
				new PrintWriter(socket.getOutputStream(), true).println(eventRows);
				i++;
				logToConsole(eventRows, i);
			}
			waiting(3000);
			LOG.println("--------------------------------------------------------------");
			LOG.println("[INFO] Finish the simulation of event handling system!");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void logToConsole(String eventRows, Integer i) {
		LOG.println("--------------------------------------------------------------");
		LOG.println("[INFO]New events (block %s) sent from Source:", i.toString());
		LOG.println("- - - - - - - - - - - - - - - - - ");
		LOG.println("UserId|Event name|Type|Description");
		LOG.println("- - - - - - - - - - - - - - - - - ");
		LOG.println(eventRows);
		LOG.println("");
		LOG.println("[INFO]Forwarding to clients");
	}

	private void waiting(int milisecond) {
		try {
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {

		}
	}
}
