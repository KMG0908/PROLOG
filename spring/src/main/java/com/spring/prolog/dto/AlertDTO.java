package com.spring.prolog.dto;

public class AlertDTO {
	private String email;
	private String checkedLog;
	private String detectedLog;
	private String danger1;
	private String danger2;
	private String date;
	
	public AlertDTO(String email, String checkedLog, String detectedLog, String danger1, String danger2, String date) {
		super();
		this.email = email;
		this.checkedLog = checkedLog;
		this.detectedLog = detectedLog;
		this.danger1 = danger1;
		this.danger2 = danger2;
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCheckedLog() {
		return checkedLog;
	}

	public void setCheckedLog(String checkedLog) {
		this.checkedLog = checkedLog;
	}

	public String getDetectedLog() {
		return detectedLog;
	}

	public void setDetectedLog(String detectedLog) {
		this.detectedLog = detectedLog;
	}

	public String getDanger1() {
		return danger1;
	}

	public void setDanger1(String danger1) {
		this.danger1 = danger1;
	}

	public String getDanger2() {
		return danger2;
	}

	public void setDanger2(String danger2) {
		this.danger2 = danger2;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
