package com.faradice.ocpp.entities;

public class Authorize extends SoapEntity {
	public final String idTag;

	public static final String call(String rfid) {
		Authorize au = new Authorize(rfid);
		return au.call();
	}

	public Authorize(String idTag) {
		this.idTag = idTag;
	}

	@Override
	public String formatXML() {
		String xmlInput = String.format(soapXMLIn(), idTag);
		return xmlInput;
	}
	
	public static Authorize buildFromXML(String xml) {
		String idTag = SoapParsing.valueOf(xml,"idTag");
		Authorize au = new Authorize(idTag);
		au.soapXMLIn(xml);
		return au;
	}	
}
