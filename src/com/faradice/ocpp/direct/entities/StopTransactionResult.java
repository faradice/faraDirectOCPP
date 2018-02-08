package com.faradice.ocpp.direct.entities;

import com.faradice.faraUtil.FaraUtil;

public class StopTransactionResult {
	
	public final boolean success;
	
	public StopTransactionResult(boolean didSuccess) {
		this.success = didSuccess;
	}
	
	public static StopTransactionResult buildFromXML(String xml) {
		boolean success = FaraUtil.isTrue(SoapParsing.valueOf(xml,"transactionId"));
		return new StopTransactionResult(success);
	}
}
