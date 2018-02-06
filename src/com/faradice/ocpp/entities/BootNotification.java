package com.faradice.ocpp.entities;

public class BootNotification extends SoapEntity {
	public final String cpvendor = "Faradice1";
	public final String cpmodel;
	public final String cpserialNumber = "FD192-201707";
	public final String cbserialNumber = "FD-20170607";
	public final String firmwareVersion = "3.5.2-M3";
	public final String iccid = "IC-SM-Vod3";
	public final String imsi = "IM-SM-Vod-300";
	public final String meterType = "MT-Etactiva-M320";
	public final String meterSerial = "20170518";

	public static final String call(String cpmodel) {
		BootNotification au = new BootNotification(cpmodel);
		return au.call();
	}
	
	public BootNotification(String cpModel) {
		this.cpmodel = cpModel;
	}

	@Override
	public String formatXML() {
		String xmlInput = String.format(soapXMLIn(), 
				cpvendor, 
				cpmodel, 
				cpserialNumber, 
				cbserialNumber, 
				firmwareVersion, 
				iccid, 
				imsi, 
				meterType, 
				meterSerial);
		return xmlInput;
	}

}
