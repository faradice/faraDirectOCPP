package com.faradice.ocpp.entities;

public class BootNotificationResult {
	public static final String ACCEPTED = "Accepted";
	public static final String BLOCKED = "Rejected";
	
	public final String currentTime;
	public final String heartbeatInterval;
	public final String status;
	
	public BootNotificationResult(String currentTime, String heartbeatInterval, String status) {
		this.currentTime = currentTime;
		this.heartbeatInterval = heartbeatInterval;
		this.status = status;
	}
	
	public static BootNotificationResult buildFromXML(String xml) {
		String currentTime =  SoapParsing.valueOf(xml,"currentTime");
		String heartbeatInterval = SoapParsing.valueOf(xml,"heartbeatInterval");
		String status = SoapParsing.valueOf(xml,"status");
		return new BootNotificationResult(currentTime, heartbeatInterval, status);
	}
	
	public int heartBeatIntervalSec() {
		return Integer.parseInt(heartbeatInterval);
	}
}
