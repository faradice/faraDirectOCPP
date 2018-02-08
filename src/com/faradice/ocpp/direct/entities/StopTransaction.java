package com.faradice.ocpp.direct.entities;

import com.faradice.faraUtil.FaraDates;

public class StopTransaction extends SoapEntity {
	public final String transactionId;
	public final String idTag;
	public final String timestamp;
	public final String meterStop;
	public final String tdTimestamp;
	public final String tdcontext = "Context";
	public final String tdMeasurand = "Measureand";
	public final String tdFormat = "Format";
	public final String tdLocation = "Reykjavik";
	public final String tdUnit = "Wh";
	public final String tdValue = "tdValue";

	public static final String call(String transactionId, String idTag, String meterStop) {
		StopTransaction au = new StopTransaction(transactionId, idTag, meterStop);
		return au.call();
	}

	public StopTransaction(String transactionId, String idTag, String meterStop) {
		this.transactionId = transactionId;
		this.idTag = idTag;
		this.timestamp = FaraDates.getDateTimeNowOCPP();
		this.meterStop = meterStop;
		this.tdTimestamp = FaraDates.getDateTimeNowOCPP();
	}

	@Override
	public String formatXML(String xml) {
		String xmlInput = String.format(xml, 
				transactionId, 
				idTag, 
				timestamp, 
				meterStop, 
				timestamp, 
				tdcontext, 
				tdMeasurand, 
				tdFormat, 
				tdLocation, 
				tdUnit, 
				tdValue);
		return xmlInput;
	}

}
