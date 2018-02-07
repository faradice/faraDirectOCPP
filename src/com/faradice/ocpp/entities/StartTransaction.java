package com.faradice.ocpp.entities;

import com.faradice.faraUtil.FaraDates;

public class StartTransaction extends SoapEntity {
	public final String connectorId;
	public final String idTag;
	public final String timestamp;
	public final String meterStart;
	public final String reservationId;

	public static final String call(String connectorId, String idTag) {
		StartTransaction au = new StartTransaction(connectorId, idTag);
		return au.call();
	}

	public StartTransaction(String connectorId, String idTag) {
		this.connectorId = connectorId;
		this.idTag = idTag;
		this.timestamp = FaraDates.getDateTimeNowOCPP();
		this.meterStart = "0";
		this.reservationId = "-1";
	}

	@Override
	public String formatXML(String xml) {
		String xmlInput = String.format(xml, connectorId, idTag, timestamp, meterStart, reservationId);
		return xmlInput;
	}

}
