package com.faradice.ocpp.entities;

import java.util.ArrayList;
import java.util.List;

import com.faradice.faraUtil.FaraFiles;

public abstract class SoapResponseEntity {
	public static final String OCPP_SOAP_TEMPLATE_FOLDER = "soap/";
	public String relatesTo;

	public String toXML() {
		String result = "";
		String actionName = action();
		if (!actionName.startsWith("/")) {
			actionName = "/" + actionName;
		}
		
		for (String row : soapHeader()) {
			result+=row;
		}
		// TODO replace header params with values
		
		// Create body
		String soapXMLBody = FaraFiles.loadFile(OCPP_SOAP_TEMPLATE_FOLDER + actionName + ".soap");

		// Format body with abstact method
		
		return result;
	}
	
	public abstract String formatXML(String xml);

	public String action() {
		String actionName = this.getClass().getSimpleName();
		return actionName;
	}
	
	private List<String> soapHeader() {
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

}
