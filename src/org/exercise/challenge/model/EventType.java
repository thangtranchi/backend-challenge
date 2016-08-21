package org.exercise.challenge.model;

import java.util.Random;

public enum EventType {
	  URGENT,
	  HIGH_PRIORITY,
	  NORMAL,
	  LOW_PRIORITY;

	  private static final EventType[] VALUES = values();
	  private static final int SIZE = VALUES.length;
	  private static final Random RANDOM = new Random();

	  public static EventType getRandomType()  {
	    return VALUES[RANDOM.nextInt(SIZE)];
	  }
}
