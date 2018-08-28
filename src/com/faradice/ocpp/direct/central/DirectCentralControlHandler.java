/*================================================================================
 * Faradice Firmware
 *
 * Copyright (c) 2016 Faradice ehf.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Faradice ehf. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Faradice.
 *================================================================================
 */
package com.faradice.ocpp.direct.central;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.faradice.faraUtil.FaraDates;
import com.faradice.faraUtil.FaraUtil;
import com.faradice.faranet.FaraHtml;
import com.faradice.faranet.FaraHttp;
import com.faradice.faranet.FaraWebHandler;
import com.faradice.firmware.Firmware;
import com.faradice.ocpp.direct.OCPPContext;
import com.faradice.ocpp.direct.OCPPSession;
import com.faradice.ocpp.direct.entities.AuthorizeResponse;
import com.faradice.ocpp.direct.entities.BootNotificationResult;
import com.faradice.ocpp.direct.entities.Heartbeat;
import com.faradice.ocpp.direct.entities.StartTransactionResult;
import com.faradice.ocpp.direct.entities.StopTransaction;

// Test server on digital ocean
// http://104.236.81.197:8088/Ocpp15WebAppDemo/

public class DirectCentralControlHandler extends FaraWebHandler {
	public static final String name = "/chargepoint";

	private String[] fields = { "host", "port", "Endpoint", "rfid"};
	private String[] size = { "200", "60", "300","300"};
	private String[] result = { "http://104.236.81.197","8088","/Ocpp15WebAppDemo/CentralSystemService","04EA6A6AA13780"};
	private String fromService = "";
	private DirectCentralAPI centralService = new DirectCentralAPI();

	public DirectCentralControlHandler() {
		super(name);
	}

	@Override
	public void get() {
		output.println("<html>");
		output.println("<head>");
//		output.println("<meta http-equiv=\"refresh\" content=\"10\" >");
		output.println("</head>");
		output.println("<body>");
		output.println("&nbsp" + "<br>");
		output.println(FaraHtml.linkNoPrefix("Refresh", "../control"));
		output.println("<br>");
		output.println(FaraHtml.seperator() + "<p>");
		output.println("Faradice " + Firmware.VERSION + "<p>");
		output.println("<br>");
		output.println("<b>OCCP Central Configuration " + FaraHttp.hostName() + " &nbsp Imax is now " + FaraUtil.maxI() + "</b><p>");
		output.println("<form method=\"post\">");
		for (int i = 0; i < fields.length; i++) {
			if (i == 0) {
				output.println(FaraHtml.createFocusField(fields[i], size[i], result[i]));
			} else {
				output.println(FaraHtml.createField(fields[i], size[i], result[i]));
			}
			output.println("<br>");
		}
		output.println("<p>");
		output.println(FaraHtml.createButton("OK", "send"));
		output.println("<p>");
		output.println(FaraHtml.seperator() + "<p>");
		output.println("<b>Central Point commands</b>");
		output.println("<p>");
		output.println(FaraHtml.createButton("Boot", "boot"));
		output.println(FaraHtml.createButton("Authenticate", "authenticate"));
		output.println(FaraHtml.createButton("Charge", "charge"));
		output.println(FaraHtml.createButton("End", "end"));
		output.println(FaraHtml.createButton("Heartbeat", "authenticate"));
        output.println("<p>");
 		output.println("</form>");
		output.println(FaraHtml.seperator());
		output.println("<p>");
		output.println("<p>");
		output.println("</body>");
		output.println("</html>");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		output.println("Result from Central OCPP Service");
		output.println("<br>");
		output.println(fromService);
	}

	@Override
	public void post() throws IOException, ServletException {
		// Store soap context from fields
		initSession();
		String line = readInputLine();
		if (line.contains("=Boot")) {
			boot();
			return;
		} else if (line.contains("=Authenticate")) {
			authenticate();
			return;
		} else if (line.contains("=Charge")) {
			startTransaction();
			return;
		} else if (line.contains("=End")) {
			stopTransaction();
			return;
		} else if (line.contains("=Heartbeat")) {
			heartbeat();
			return;
		}
		
		int i = 0;
		int pos = 0;
		while (line != null) {
			pos = line.indexOf(fields[i] + "=", pos);
			while (pos >= 0 && i < fields.length) {
				pos += fields[i].length() + 1;
				int endPos = line.indexOf("&", pos);
				if (endPos > pos) {
					result[i++] = line.substring(pos, endPos);
					pos = endPos + 1;
				} else {
					pos = -1;
				}
			}
			line = readInputLine();
		}
		loadSubPage(DirectCentralControlHandler.name);
	}

	private void initSession() {
		String host = result[0];
		String port = result[1].trim();
		String endpoint = result[2];
		System.out.println("OCPP configuration:");
		System.out.println("---------------------");
		System.out.println("Host: "+host);
		System.out.println("port: "+port);
		System.out.println("endpoint: "+endpoint);
		// Load from get variables
		int portNr = Integer.parseInt(port);
		OCPPContext.set(new OCPPSession(host, portNr, endpoint,  "Faradice"));
	}
	
	private void boot() throws IOException {
		System.out.println("\nBoot notification result");
		BootNotificationResult br = centralService.bootNotification("FD-Firm-Type1-Type2");
		System.out.println("\nBoot Notification");
		System.out.println(br.currentTime);
		System.out.println(br.heartbeatInterval);
		System.out.println(br.status);
		fromService = "Boot Notification<br>";
		fromService += br.currentTime+"<br>";
		fromService += br.heartbeatInterval+"<br>";
		fromService += br.status+"<br>";
		loadSubPage(DirectCentralControlHandler.name);
	}
	
	private void authenticate() throws IOException {
		System.out.println("<br>Authenticate result<br>");
		AuthorizeResponse aur = centralService.authorize(result[3]);		
		System.out.println(aur.status);
		fromService = "Authenticate result<br>";
		fromService += aur.status;
		loadSubPage(DirectCentralControlHandler.name);
	}

	private void startTransaction() throws IOException {
		StartTransactionResult str  =  centralService.startTransaction("Faradice1", result[3]);
		System.out.println("Start Transaction result");
		System.out.println(str.idTagInfo);
		System.out.println(str.transactionId);
		fromService = "<br>Start Transaction result<br>";
		fromService += str.idTagInfo+"<br>";
		fromService += str.transactionId+"<br>";
		loadSubPage(DirectCentralControlHandler.name);
	}

	private void stopTransaction() throws IOException {
		String xmlRes = StopTransaction.call("20", result[3], "8500");
		System.out.println("\nStop Transaction result\n");
		System.out.println(xmlRes);
		fromService = "<br>Stop Transaction result<br>";
		fromService += xmlRes;
		loadSubPage(DirectCentralControlHandler.name);
	}

	private void heartbeat() throws IOException {
		String xmlRes = Heartbeat.call(FaraDates.getDateTimeNowOCPP());
		System.out.println("Heartbeat result");
		System.out.println(xmlRes+"<br>");
		fromService = "<br>Heartbeat result<br>";
		fromService += xmlRes;
		loadSubPage(DirectCentralControlHandler.name);
	}
}
