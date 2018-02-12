package com.faradice.ocpp.chargepoint.test;

import java.util.UUID;

import com.faradice.faraUtil.FaraDates;
import com.faradice.ocpp.direct.OCPPContext;
import com.faradice.ocpp.direct.OCPPSession;
import com.faradice.ocpp.direct.central.DirectCentralAPI;
import com.faradice.ocpp.direct.entities.Authorize;
import com.faradice.ocpp.direct.entities.AuthorizeResponse;
import com.faradice.ocpp.direct.entities.BootNotificationResult;
import com.faradice.ocpp.direct.entities.Heartbeat;
import com.faradice.ocpp.direct.entities.SoapEntity;
import com.faradice.ocpp.direct.entities.StartTransactionResult;
import com.faradice.ocpp.direct.entities.StopTransaction;

/*
 * Skoða þennan betur.  Virðist góður
 * https://www.javaworld.com/article/3215966/java-language/web-services-in-java-se-part-2-creating-soap-web-services.html

 * 
 *  Check out jaxb for marshalling
 *  https://docs.oracle.com/javase/tutorial/jaxb/intro/
 */
public class UnitTest {
	DirectCentralAPI chargePoint = new DirectCentralAPI();

	public void testAuthorizeOutput()  {
		String res = Authorize.call("RaggaTag");
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

	public void executeDirect() {
		String actionName = "/Authorize";
		String s = ""; 
		s += "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"urn://Ocpp/Cs/2012/06/\"> "; 
		s += "  <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"> ";
		s += "    <ns:chargeBoxIdentity>Faradice1</ns:chargeBoxIdentity> ";
		s += "    <wsa:Action soap:mustUnderstand=\"1\">/Authorize</wsa:Action> ";
		s += "    <wsa:MessageID>uuid:"+UUID.randomUUID().toString()+"</wsa:MessageID> ";
		s += "    <wsa:To>http://104.236.81.197:8088/cs_ocpp16/CentralSystemService</wsa:To> ";
        s += "  </soap:Header>"; 
        s += "  <soap:Body>\n"; 
        s += "    <ns:authorizeRequest>\n"; 
        s += "        <ns:idTag>Raggi</ns:idTag>\n"; 
        s += "    </ns:authorizeRequest>\n"; 
		s += "  </soap:Body>\n";
		s += "</soap:Envelope> ";
        try {
			String result = SoapEntity.executeSoapAction(actionName, s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	public static void main(String[] args)  {
		UnitTest ut = new UnitTest();
//		OCPPContext.set(OCPPSession.buildLocal8079());
//		OCPPContext.set(OCPPSession.buildDirect8085());
//		OCPPContext.set(OCPPSession.build15());
		OCPPContext.set(OCPPSession.build16());
//		ut.testAuthorize();
		ut.executeDirect();
		
//		ut.testAuthorizeOutput();
	//	ut.testBootNotification();
	//	ut.testStartTransaction();	
	//	ut.testStopTransaction(); 
	//	ut.testHeartbeat(); 
	}
}
