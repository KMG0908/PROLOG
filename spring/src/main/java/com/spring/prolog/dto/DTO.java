package com.spring.prolog.dto;

public class DTO {
	public String AttackLevel;
	public String Problem;
	public String Country;
	public String Date;
	public String Time;
	public String ClientIP;
	public String ServerIP;
	public String Port;
	
	public DTO() {}
	
	public DTO(String attackLevel, String problem, String country, String date, String time, String clientIP,
			String serverIP, String port) {
		super();
		AttackLevel = attackLevel;
		Problem = problem;
		Country = country;
		Date = date;
		Time = time;
		ClientIP = clientIP;
		ServerIP = serverIP;
		Port = port;
	}

	public String getAttackLevel() {
		return AttackLevel;
	}

	public void setAttackLevel(String attackLevel) {
		AttackLevel = attackLevel;
	}

	public String getProblem() {
		return Problem;
	}

	public void setProblem(String problem) {
		Problem = problem;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getClientIP() {
		return ClientIP;
	}

	public void setClientIP(String clientIP) {
		ClientIP = clientIP;
	}

	public String getServerIP() {
		return ServerIP;
	}

	public void setServerIP(String serverIP) {
		ServerIP = serverIP;
	}

	public String getPort() {
		return Port;
	}

	public void setPort(String port) {
		Port = port;
	}
}
