package com.machine.learning;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LearningSchedular {
	public static void startSchedular() {
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    Date aDate = new Date();
    Calendar with = Calendar.getInstance();
    with.setTime(aDate);

    int hour = with.get(Calendar.HOUR_OF_DAY);
    int intDelayInHour = hour < 5 ? 5 - hour : 24 - (hour - 5);

    System.out.println("Current Hour: "+hour);
    System.out.println("Comuted Delay for next 5 AM: "+intDelayInHour);

    scheduler.scheduleAtFixedRate( new Runnable() {
		
		@Override
		public void run() {
			LearningModulesPool.learn();
			
		}
	}, intDelayInHour, 24, TimeUnit.HOURS);
	}
}
