package com.faradice.ocpp.entities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.faradice.faraUtil.FaraDates;
import com.faradice.faraUtil.FaraFiles;
import com.faradice.faraUtil.Log;
import com.faradice.ocpp.OCPPContext;

import sun.net.www.http.PosterOutputStream;

/*
 * Check out this lib to convert SOAP to JSON https://github.com/stleary/JSON-java
 * 
 */ 
public abstract class SoapEntity {
	public static final String OCPP_SOAP_TEMPLATE_FOLDER = "soap/";
	private String soapXMLIn = null;

	public abstract String formatXML();

	public String action() {
		String actionName = this.getClass().getSimpleName();
		return actionName;
	}

	public String call() {
		String soapXMLOut = null;
		try {
			String actionName = action();
			if (!actionName.startsWith("/")) {
				actionName = "/" + actionName;
			}
			List<String> commonHead = createSoapRequestHeader();
			String soapXMLBody = FaraFiles.loadFile(OCPP_SOAP_TEMPLATE_FOLDER + actionName + ".soap");
			String soapXMLHead = "";

			for (String line : commonHead) {
				soapXMLHead += line;
			}
			int startOfBody = soapXMLHead.indexOf("</soap:Envelope>");
			soapXMLIn = soapXMLHead.substring(0, startOfBody);
			soapXMLIn += soapXMLBody;
			soapXMLIn += soapXMLHead.substring(startOfBody);

			// Inject action name first three common varables
			soapXMLIn = soapXMLIn.replaceFirst("%s", OCPPContext.get().cpid);
			soapXMLIn = soapXMLIn.replaceFirst("%s", actionName);
			soapXMLIn = soapXMLIn.replaceFirst("%s", UUID.randomUUID().toString());
			soapXMLIn = soapXMLIn.replaceFirst("%s", OCPPContext.get().wsURL());
			String soapXMLWithParams = formatXML();
			System.out.println("REQUEST");
			System.out.println(soapXMLWithParams);

			logSoapCall(soapXMLWithParams);
			soapXMLOut = executeSoapAction(actionName, soapXMLWithParams);
			System.out.println("RESULT");
			System.out.println(soapXMLOut);
			logSoapResult(soapXMLOut);
		} catch (Exception ex) {
			Log.error("Action " + action() + " error.", ex);
		}
		return soapXMLOut;
	}

	public void soapXMLIn(String xml) {
		this.soapXMLIn = xml;
	}
	
	public String soapXMLIn() {
		return soapXMLIn;
	}

	public String executeSoapAction(String action, String soapXML) throws MalformedURLException, IOException {
		String responseString;
		String outputString = "";
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[soapXML.length()];
		buffer = soapXML.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		URL url = new URL(OCPPContext.get().wsURL());
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		String contentType = String.format("application/soap+xml;charset=UTF-8;action=\"%s\"", action);
		httpConn.setRequestProperty("Content-Type", contentType);
		httpConn.setRequestProperty("Host", OCPPContext.get().hostWithPort());
		httpConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
//		httpConn.setDoInput(true);

		// Write the content of the request to the outputstream of the HTTP Connection.
		OutputStream out = httpConn.getOutputStream();
		out.write(b);
		if (out instanceof PosterOutputStream) {
			// Log the complete post request
			PosterOutputStream pout = (PosterOutputStream) out;
			byte[] content = pout.toByteArray();
			FaraFiles.appendToFile(OCPP_SOAP_TEMPLATE_FOLDER + "log/postContent.log", content, content.length);
			FaraFiles.appendToFile(OCPP_SOAP_TEMPLATE_FOLDER + "log/postContent.log", "\n");
		}

		// Ready with sending the request.

		// Read the response.
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		// Write the SOAP message response to a String.
		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}
		isr.close();
		out.close();
		return outputString;
	}

	public static List<String> createSoapRequestHeader() {
		ArrayList<String> hl = new ArrayList<>();
		hl.add("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"urn://Ocpp/Cs/2012/06/\">");
		hl.add("  <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
		hl.add("    <ns:chargeBoxIdentity>%s</ns:chargeBoxIdentity>");
		hl.add("    <wsa:Action soap:mustUnderstand=\"1\">%s</wsa:Action>");
		hl.add("    <wsa:MessageID>uuid:%s</wsa:MessageID>");
		hl.add("    <wsa:To>%s</wsa:To>");
		hl.add("  </soap:Header>");
		hl.add("</soap:Envelope>");
		return hl;
	}
	
	public static List<String> createSoapResponseHeader() {
		ArrayList<String> hl = new ArrayList<>();
		hl.add("<S:Envelope xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\">");
	    hl.add("<S:Header>");
	    hl.add("<Action xmlns=\"http://www.w3.org/2005/08/addressing\" xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\" S:mustUnderstand=\"true\">$s");
	    	hl.add("</Action>");
	    	hl.add("<MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">$s</MessageID>");
	    hl.add("<RelatesTo xmlns=\"http://www.w3.org/2005/08/addressing\">$s</RelatesTo>");
	    	hl.add("<To xmlns=\"http://www.w3.org/2005/08/addressing\">http://www.w3.org/2005/08/addressing/anonymous</To>");
	    	hl.add("</S:Header>");
	    	hl.add("</S:Header>");
	    	hl.add("</S:Envelope>");
	    	return hl;
	}
	
	private void logSoapCall(String soapXML) {
		String log = FaraDates.getDateTimeNow() + " Action: " + action();
		log = log + "Calling \naction: " + action() + "\n";
		log = log + soapXML + "\n";
		FaraFiles.appendToFile(OCPP_SOAP_TEMPLATE_FOLDER + "log/calls.log", log);
	}

	private void logSoapResult(String soapXML) {
		String log = FaraDates.getDateTimeNow() + " Action: " + action();
		log = log + "Result from \naction: " + action() + "\n";
		log = log + soapXML + "\n\n";
		FaraFiles.appendToFile(OCPP_SOAP_TEMPLATE_FOLDER + "log/results.log", log);
	}

}
