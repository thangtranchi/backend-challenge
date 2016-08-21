package org.exercise.challenge.util;

import static java.lang.String.format;

public class Logger {
	public static final Logger LOG = new Logger();

	public void println(String message) {
		System.out.println(message);
	}

	public void println(String message, String value) {
		System.out.println(format(message, value));
	}
}
