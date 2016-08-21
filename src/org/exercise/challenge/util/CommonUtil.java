package org.exercise.challenge.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CommonUtil {
	public static BufferedReader createBufferReader(Socket socketToClientUser) {
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new InputStreamReader(
					socketToClientUser.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferReader;
	}
	
	public static Integer extractUserId(String event) {
		String[] eventData = event.split(SocketConstant.SEPARATER);
		if (isNumeric(eventData[0])) {
			return Integer.parseInt(eventData[0]);
		} else {
			return null;
		}
	}

	public static boolean isNumeric(String str) {
		if (str != null) {
			return str.matches("-?\\d+(.\\d+)?");
		} else {
			return false;
		}
	}
}
