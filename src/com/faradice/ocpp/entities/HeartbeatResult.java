package com.faradice.ocpp.entities;

import java.util.Date;

import com.faradice.faraUtil.FaraDates;

public class HeartbeatResult {
	
	public final Date timestamp;
	
	public HeartbeatResult(Date ts) {
		this.timestamp = ts;
	}
	
	public static HeartbeatResult buildFromXML(String xml) {
		Date ts = FaraDates.parseDate(SoapParsing.valueOf(xml,"timestamp"));
		return new HeartbeatResult(ts);
	}
	
}
