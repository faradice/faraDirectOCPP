package com.faradice.ocpp.entities;

public class Heartbeat extends SoapEntity {
	public final String undefined;
	
	public static final String call(String undefined) {
		Heartbeat au = new Heartbeat(undefined);
		return au.call();
	}

	public Heartbeat(String undefined) {
		this.undefined = undefined;
	}

	@Override
	public String formatXML(String xml) {
		return xml;
	}
}
