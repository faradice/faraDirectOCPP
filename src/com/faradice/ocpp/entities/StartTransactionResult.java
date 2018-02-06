package com.faradice.ocpp.entities;

public class StartTransactionResult {
	
	public final String transactionId;
	public final String idTagInfo; 
	
	public StartTransactionResult(String transactionId, String idTagInfo) {
		this.transactionId = transactionId;
		this.idTagInfo = idTagInfo;
	}
	
	public static StartTransactionResult buildFromXML(String xml) {
		String transactionId = SoapParsing.valueOf(xml,"transactionId");
		String idTagInfo = SoapParsing.valueOf(xml,"idTagInfo");
		return new StartTransactionResult(transactionId, idTagInfo);
	}
}
