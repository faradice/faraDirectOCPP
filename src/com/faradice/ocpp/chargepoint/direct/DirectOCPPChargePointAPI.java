package com.faradice.ocpp.chargepoint.direct;

import com.faradice.ocpp.OCPPChargepointAPI;
import com.faradice.ocpp.entities.Authorize;
import com.faradice.ocpp.entities.AuthorizeResponse;
import com.faradice.ocpp.entities.BootNotification;
import com.faradice.ocpp.entities.BootNotificationResult;
import com.faradice.ocpp.entities.Heartbeat;
import com.faradice.ocpp.entities.HeartbeatResult;
import com.faradice.ocpp.entities.StartTransaction;
import com.faradice.ocpp.entities.StartTransactionResult;
import com.faradice.ocpp.entities.StopTransaction;
import com.faradice.ocpp.entities.StopTransactionResult;

public class DirectOCPPChargePointAPI implements OCPPChargepointAPI {
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
