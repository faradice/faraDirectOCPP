package com.faradice.ocpp.direct.entities;

public class AuthorizeResponse extends SoapResponseEntity {
	public static final String ACCEPTED = "Accepted";
	public static final String BLOCKED = "Blocked";
	public static final String EXPIRED = "Expired";
	public static final String INVALID = "Invalid";
	public static final String CONCURRENT_TX = "ConcurrentTx";

	public final String status;
	public final String expiryDate;
	public final String parentIdTag;

	public AuthorizeResponse(String status, String expiryDate, String parentId, String relatesTo) {
		this.messageId = relatesTo;
		this.status = status;
		this.expiryDate = expiryDate;
		this.parentIdTag = parentId;
	}

	public static AuthorizeResponse buildFromXML(String xml) {
		String status = SoapParsing.valueOf(xml, "status");
		String expDate = SoapParsing.valueOf(xml, "expiryDate");
		String parentId = SoapParsing.valueOf(xml, "parentIdTag");
		String messageId = SoapParsing.valueOf(xml, "RelatesTo");
		return new AuthorizeResponse(status, expDate, parentId, messageId);
	}

	@Override
	public String formatXML(String xml) {
		return String.format(xml, status, expiryDate, parentIdTag);
	}
}
