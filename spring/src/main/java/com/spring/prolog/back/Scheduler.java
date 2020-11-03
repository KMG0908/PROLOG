package com.spring.prolog.back;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Timer;

public class Scheduler {
	public static Socket socket = null;
	public static int flag = 0;
	
	public Scheduler(String email) { 
		WeeklySearch weeklySearch = new WeeklySearch();  
		weeklySearch.setEmail(email);
		SixHour sixHour = new SixHour();
		sixHour.setEmail(email);
		Timer timer = new Timer();   
		Calendar date = Calendar.getInstance();
		 
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		
		timer.schedule(weeklySearch, 1000 * 60, 1000 * 60 * 3);
		timer.schedule(sixHour, date.getTime(), 1000 * 60 * 60 * 6);
	}
}
