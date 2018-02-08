package com.faradice.ocpp.direct;

import java.security.InvalidParameterException;
import java.util.List;

import com.faradice.faraUtil.FaraFiles;

public class OCPPSession {
	public final String host;
	public final int port;
	public final String endpoint;
	public final String cpid;

	public OCPPSession(String host, int port, String endpoint, String chargePointIdentity) {
		if (host == null) throw new NullPointerException("host is null");
		if (port < 1024) throw new InvalidParameterException("Invalid port number:"+port);
		if (endpoint == null) throw new NullPointerException("EndPoint is null");
		if (chargePointIdentity == null) throw new NullPointerException("ChargePointId is null");
		this.host = host;
		this.port = port;
		if (!endpoint.startsWith("/")) endpoint = "/"+endpoint;
		this.endpoint = endpoint;
		this.cpid = chargePointIdentity;
	}

	public static OCPPSession build15() {
		return new OCPPSession("http://104.236.81.197", 8088, "/Ocpp15WebAppDemo/CentralSystemService","Faradice1");
	}

	public static OCPPSession build16() {
		return new OCPPSession("http://104.236.81.197", 8088, "/cs_ocpp16/CentralSystemService","Faradice1");
	}

	public static OCPPSession buildLocal8079() {
		return new OCPPSession("http://localhost", 8079, "/FaraCentralSystem","Faradice1");
	}

	public static OCPPSession buildDirect8085() {
		return new OCPPSession("http://localhost", 8085, "/Fara_occp/CentralSystemService","FaraCharger1");
	}

	public static OCPPSession build(String fileName) {
		List<String> rows = FaraFiles.loadRows(fileName);
		String host = null;
		String port = null;
		String endpoint = null;
		String chargePointId = null;
		for (String row : rows) {
			row = row.trim();
			if (row.length() < 2) continue;
			if (row.startsWith("#")) continue;
			String[] keyValue = row.split(" ");
			if (keyValue.length == 2) {
				if (keyValue[0].trim().equalsIgnoreCase("host")) {
					host = keyValue[1].trim();
				} else if (keyValue[0].trim().equalsIgnoreCase("port")) {
					port = keyValue[1].trim();
				} else if (keyValue[0].trim().equalsIgnoreCase("endpoint")) {
					endpoint = keyValue[1].trim();
				} else if (keyValue[0].trim().equalsIgnoreCase("chargePointId")) {
					chargePointId = keyValue[1].trim();
				}
			}
		}
		return new OCPPSession(host, Integer.parseInt(port), endpoint, chargePointId);
	}
	
	public String wsURL() {
		return host+":"+port+endpoint;
	}
	
	public String hostWithPort() {
		return host+":"+port;
	}
}
