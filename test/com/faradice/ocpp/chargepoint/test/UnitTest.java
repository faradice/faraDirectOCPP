package com.faradice.ocpp.chargepoint.test;
import com.faradice.faraUtil.FaraDates;
import com.faradice.ocpp.OCPPContext;
import com.faradice.ocpp.OCPPSession;
import com.faradice.ocpp.central.DirectCentralAPI;
import com.faradice.ocpp.entities.Authorize;
import com.faradice.ocpp.entities.AuthorizeResponse;
import com.faradice.ocpp.entities.BootNotificationResult;
import com.faradice.ocpp.entities.Heartbeat;
import com.faradice.ocpp.entities.StartTransactionResult;
import com.faradice.ocpp.entities.StopTransaction;

/*
 *  Check out jaxb for marshalling
 *  https://docs.oracle.com/javase/tutorial/jaxb/intro/
 */
public class UnitTest {
	DirectCentralAPI chargePoint = new DirectCentralAPI();

	public void testAuthorizeOutput()  {
		String res = Authorize.call("04EA6A6AA13780");
		System.out.println(res);
	}

	
	public void testAuthorize()  {
		AuthorizeResponse aur = chargePoint.authorize("04EA6A6AA13780");
		System.out.println(aur.status);
		System.out.println(aur.expiryDate);
		System.out.println(aur.parentIdTag);
	}

	public void testBootNotification()  {
		BootNotificationResult br = chargePoint.bootNotification("FD-Firm-Type1-Type2");
		System.out.println("\nBoot Notification");
		System.out.println(br.currentTime);
		System.out.println(br.heartbeatInterval);
		System.out.println(br.status);
	}

	public void testStartTransaction() {
		StartTransactionResult str  =  chargePoint.startTransaction("Faradice1", "04EA6A6AA13780");
		System.out.println("\nStart Transaction");
		System.out.println(str.idTagInfo);
		System.out.println(str.transactionId);
	}

	public void testStopTransaction() {
		System.out.println(StopTransaction.call("20", "04EA6A6AA13780", "8500"));
/*		
		StopTransactionResult str  =  OCPPClientAPI.stopTransaction("Faradice1", "04EA6A6AA13780");
		System.out.println("\nStop Transaction");
		System.out.println(str.idTagInfo);
		System.out.println(str.transactionId);
*/
	}

	public void testHeartbeat() {
		System.out.println(Heartbeat.call(FaraDates.getDateTimeNowOCPP()));
/*		
		HeartbeatResult hbr  =  OCPPClientAPI.Heartbeat(FaraDates.getDateTimeNowOCPP());
		System.out.println("\nHeartbeat");
		System.out.println(hbr.timestamp);
*/
	}

	
	public static void main(String[] args)  {
		UnitTest ut = new UnitTest();
//		OCPPContext.set(OCPPSession.buildLocal8079());
//		OCPPContext.set(OCPPSession.buildDirect8085());
		OCPPContext.set(OCPPSession.build16());
//		ut.testAuthorize();
		
//		ut.testAuthorizeOutput();
		ut.testBootNotification();
	//	ut.testStartTransaction();	
	//	ut.testStopTransaction(); 
	//	ut.testHeartbeat(); 
	}
}
