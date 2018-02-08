package com.faradice.ocpp.direct.central;

import com.faradice.ocpp.direct.entities.Authorize;
import com.faradice.ocpp.direct.entities.AuthorizeResponse;
import com.faradice.ocpp.direct.entities.BootNotification;
import com.faradice.ocpp.direct.entities.BootNotificationResult;
import com.faradice.ocpp.direct.entities.Heartbeat;
import com.faradice.ocpp.direct.entities.HeartbeatResult;
import com.faradice.ocpp.direct.entities.StartTransaction;
import com.faradice.ocpp.direct.entities.StartTransactionResult;
import com.faradice.ocpp.direct.entities.StopTransaction;
import com.faradice.ocpp.direct.entities.StopTransactionResult;

public class DirectCentralAPI {
	public AuthorizeResponse authorize(String rfid) {
		String result = Authorize.call(rfid);
		return AuthorizeResponse.buildFromXML(result);
	}

	public BootNotificationResult bootNotification(String cmpModel) {
		String res = BootNotification.call(cmpModel);
		BootNotificationResult br = BootNotificationResult.buildFromXML(res);
		return br;
	}

	public StartTransactionResult startTransaction(String connectorId, String idTag) {
		String res = StartTransaction.call(connectorId, idTag);
		StartTransactionResult sr = StartTransactionResult.buildFromXML(res);
		return sr;
	}

	public StopTransactionResult stopTransaction(String transactionId, String idTag, String meterStop) {
		String res = StopTransaction.call(transactionId, idTag, meterStop);
		StopTransactionResult sr = StopTransactionResult.buildFromXML(res);
		return sr;
	}

	public HeartbeatResult heartbeat(String undefined) {
		String res = Heartbeat.call(undefined);
		HeartbeatResult hr = HeartbeatResult.buildFromXML(res);
		return hr;
	}

}
