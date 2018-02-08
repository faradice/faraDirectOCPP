package com.faradice.ocpp.direct.entities;

public class Authorize extends SoapEntity {
	public final String idTag;

	public static final String call(String rfid) {
		Authorize au = new Authorize(rfid, null);
		return au.call();
	}

	public Authorize(String idTag, String messageId) {
		this.idTag = idTag;
		this.messageId = messageId;
	}
	
	@Override
	public String formatXML(String xml) {
		String xmlInput = String.format(xml, idTag);
		return xmlInput;
	}
	
	public static Authorize buildFromXML(String xml) {
		String idTag = SoapParsing.valueOf(xml,"idTag");
		String messageId = SoapParsing.valueOf(xml,"MessageID");
		Authorize au = new Authorize(idTag, messageId);
		return au;
	}	
}
