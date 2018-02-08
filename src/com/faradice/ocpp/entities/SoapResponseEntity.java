package com.faradice.ocpp.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.faradice.faraUtil.FaraFiles;

public abstract class SoapResponseEntity extends SoapEntity {

	public String toXML() {
		String actionName = action();
		if (!actionName.startsWith("/")) {
			actionName = "/" + actionName;
		}
		List<String> commonHead = soapHeader();
		String soapXMLHead = "";
		for (String line : commonHead) {
			soapXMLHead += line;
		}
		
		// Inject common header varables
		soapXMLHead = soapXMLHead.replaceFirst("%s", actionName);
		soapXMLHead = soapXMLHead.replaceFirst("%s", UUID.randomUUID().toString());
		soapXMLHead = soapXMLHead.replaceFirst("%s", messageId);
		
		int startOfBody = soapXMLHead.indexOf("</soap:Envelope>");
		soapXMLHead = soapXMLHead.substring(0, startOfBody);
		String soapXMLBody = FaraFiles.loadFile(OCPP_SOAP_TEMPLATE_FOLDER + actionName + ".soap");
		soapXMLBody = formatXML(soapXMLBody);
		String xmlResult = soapXMLHead + soapXMLBody + soapXMLHead.substring(startOfBody);
		return xmlResult;
	}
	
	@Override
	public List<String> soapHeader() {
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
