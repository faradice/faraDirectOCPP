package com.faradice.ocpp.direct.entities;

import java.io.StringReader;

import javax.xml.bind.JAXB;

public class SoapParsing {

	public static String valueOf(String xml, String key) {
		String xmlLow = xml.toLowerCase();
		String keyLow = key.toLowerCase();
		String value = "";
		int startOfSearch = xmlLow.indexOf(":envelope");
		String lookup = "<" + keyLow;
		int startOfKey = xmlLow.indexOf(lookup, startOfSearch);
		if (startOfKey < startOfSearch) {
			lookup = ":" + keyLow;
			startOfKey = xmlLow.indexOf(lookup, startOfSearch);
		}
		if (startOfKey > startOfSearch) {
			int startOfValue = xml.indexOf(">");
			if (startOfValue > 0) {
				startOfValue++;
				int endOfValue = xml.indexOf("</", startOfValue);
				if (endOfValue > startOfValue) {
					value = xml.substring(startOfValue, endOfValue);
				}
			}
		}
		return value;
	}

	public static <T> T toObject(String xml, Class<? extends T> type) {
		T instance = JAXB.unmarshal(new StringReader(xml), type);
		return instance;
	}

}
