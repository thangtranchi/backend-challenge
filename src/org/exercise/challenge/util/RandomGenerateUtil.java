package org.exercise.challenge.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.exercise.challenge.model.EventType;

public class RandomGenerateUtil {
	private final static String SEPARATED = " | ";
	
	private final static String[] EVENT_NAME = {"Meeting","Team Building","Interview","Presentation","Seminar"};
	private final static String[] EVENT_DESC = {"With Peter","Take it easy","Do with your excitement","Feel it free","Do with your best"};

	public static int createRandomNumber(int min, int max) {
		int randomNum = new Random().nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static String createRandomEventName() {
		return EVENT_NAME[createRandomNumber(0,4)];
	}
	
	public static String createRandomEventDescription() {
		return EVENT_DESC[createRandomNumber(0,4)];
	}
	
	public static String createRandomEventData() {
		return  String.valueOf(createRandomNumber(1, 4)) + SEPARATED
				+ createRandomEventName() + SEPARATED
				+ EventType.getRandomType().toString() + SEPARATED
				+ createRandomEventDescription();
	}
	
	public static String createRandomEventData(int number) {
		List<String> events = new ArrayList<>();
		for(int i = 0; i<number; i++){
			events.add(createRandomEventData()); 
		}
		return events.stream().collect(Collectors.joining("\n"));
	}
}
