package org.exercise.challenge.app;

import org.exercise.challenge.client.EventSource;
import org.exercise.challenge.client.UserClient;
import org.exercise.challenge.server.Server;

public class App {

	public static void main(String[] args) {
		new Thread(new Server()).start();
		boolean isServerOnly = args.length > 0 ? args[0].equals("serverOnly"): false;
		
		if (!isServerOnly) {
			new Thread(new EventSource()).start();

			new Thread(new UserClient(1)).start();
			new Thread(new UserClient(2)).start();
			new Thread(new UserClient(3)).start();
		}

	}

}
