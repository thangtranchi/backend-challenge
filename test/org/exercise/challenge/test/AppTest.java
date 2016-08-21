package org.exercise.challenge.test;

import static org.exercise.challenge.util.CommonUtil.createBufferReader;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.exercise.challenge.app.App;
import org.exercise.challenge.util.SocketConstant;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppTest {

	private Socket eventSource;
	private Socket socketClient_1;
	private Socket socketClient_2;
	private Socket socketClient_3;

	@BeforeClass
	public static void startServer() throws IOException {
		new App();
		App.main(new String[] { "serverOnly" });
	}

	@After
	public void stopServer() throws IOException {
		eventSource.close();
		socketClient_1.close();
		socketClient_2.close();
		socketClient_3.close();
		// App sockets closed by shutdown hook
	}

	@Test
	public void testForwardEventsToConnectedClientUserSuccesfully() throws UnknownHostException, IOException {
		// given
		eventSource = new Socket(SocketConstant.HOST, SocketConstant.EVENT_SOURCE_PORT);
		socketClient_1 = new Socket(SocketConstant.HOST, SocketConstant.CLIENT_PORT);
		socketClient_2 = new Socket(SocketConstant.HOST, SocketConstant.CLIENT_PORT);
		socketClient_3 = new Socket(SocketConstant.HOST, SocketConstant.CLIENT_PORT);
		new PrintWriter(socketClient_1.getOutputStream(), true).println("1");
		new PrintWriter(socketClient_2.getOutputStream(), true).println("2");
		new PrintWriter(socketClient_3.getOutputStream(), true).println("3");

		String eventToClients = "1|Team Building|LOW_PRIORITY|Party in Berlin\n" + "2|Interview|HIGH_PRIORITY|new java candidate";

		// when
		new PrintWriter(eventSource.getOutputStream(), true).println(eventToClients);

		// then
		String eventReceivedInClient1 = readEvent(socketClient_1);
		assertEquals(eventReceivedInClient1, "1|Team Building|LOW_PRIORITY|Party in Berlin");

		String eventReceivedInClient2 = readEvent(socketClient_2);
		assertEquals(eventReceivedInClient2, "2|Interview|HIGH_PRIORITY|new java candidate");

	}

	private String readEvent(Socket socket) throws IOException {
		BufferedReader bufferReader = createBufferReader(socket);
		return bufferReader.readLine();
	}
}
