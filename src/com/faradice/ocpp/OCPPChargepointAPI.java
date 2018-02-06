package com.faradice.ocpp;

import com.faradice.ocpp.entities.AuthorizeResponse;
import com.faradice.ocpp.entities.BootNotificationResult;
import com.faradice.ocpp.entities.HeartbeatResult;
import com.faradice.ocpp.entities.StartTransactionResult;
import com.faradice.ocpp.entities.StopTransactionResult;

public interface OCPPChargepointAPI {
	  BootNotificationResult bootNotification(String rfid);
	  AuthorizeResponse authorize(String rfid);
	  StartTransactionResult startTransaction(String connectorId, String idTag);
	  StopTransactionResult stopTransaction(String transactionId, String idTag, String meterStop);
	  HeartbeatResult heartbeat(String undefined);
}
