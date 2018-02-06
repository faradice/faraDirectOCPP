package com.faradice.ocpp.entities;

public class SoapParsing {

	public static String valueOf(String xml, String key) {
		String value = "";
		String lookup = "<"+key+">";
		int startOfSearch = xml.indexOf(":Body");
		if (startOfSearch < 0) startOfSearch = 1;
		int startOfKey = xml.indexOf(lookup, startOfSearch);
		if (startOfKey > startOfSearch) {
			int startOfValue = startOfKey + lookup.length();
			int endOfValue = xml.indexOf("</", startOfValue);
			if (endOfValue > startOfValue) {
				value = xml.substring(startOfValue, endOfValue);
			}
		}		
		return value;
	}
	
}
