package com.faradice.ocpp.chargepoint.test;

import java.io.StringReader;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;

import com.faradice.ocpp.direct.entities.SoapParsing;

/*
 * https://stackoverflow.com/questions/19395400/jaxb-unmarshall-with-namespaces-and-prefix
 * 
 * https://stackoverflow.com/questions/20967242/how-to-marshall-soap-response-to-pojo
 * 
 */

public class SoapMarshall {
    public static void main(String[] args) throws Exception {
        String s = "<Result><Temperature>32.2</Temperature></Result>";
        TempResponse res = JAXB.unmarshal(new StringReader(s), TempResponse.class);
        System.out.println(res.getResult());
        
        TempResponse res2 = SoapParsing.toObject(s, TempResponse.class);
        System.out.println(res2.getResult());
              
    }
    
    public static String soapMsg() {
        String requestXml =
                "<Temp xmlns=\"http://www.w3schools.com/webservices/\">" +
                "    <Temperature>22.4</Temperatur>" +
                "</FahrenheitToCelsius>";

        // Create the Message object
        return requestXml;
 
    }
}

class TempResponse {
    @XmlElement(name = "Temperature")
    private double temp;

    public double getResult() {
        return temp;
    }
}
