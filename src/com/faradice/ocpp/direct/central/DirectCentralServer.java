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

import com.faradice.faranet.FaraJettyServer;

/**
 * Skoða þennan betur.  Virðist góður
 * https://www.javaworld.com/article/3215966/java-language/web-services-in-java-se-part-2-creating-soap-web-services.html
 * 
 * 
 * FaraOCPP Central server using SOAP protocol
 */
public class DirectCentralServer {
	public final static int defaultPort = 8085;
	public final static String path = "/Fara_occp";
	public final static String name = "/CentralSystemService/*";

	public static void main(String[] args) throws Exception {
		FaraJettyServer server = new FaraJettyServer(defaultPort, args);
		server.addServlet(path, name, new DirectCentralService());
		System.out.println("Endpoint: http://" + FaraJettyServer.hostName() + ":" + defaultPort + path + name);
		server.run();
	}
}
