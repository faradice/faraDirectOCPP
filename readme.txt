# Soap tutorial Document style
# Use the wsdl file generated from the SoapTest project wich is a java only (no wsdl) soap tutorial

http://104.236.81.197:8088/Ocpp15WebAppDemo/CentralSystemService?wsdl

# The tutorial: https://www.mkyong.com/webservices/jax-ws/jax-ws-hello-world-example-document-style/

# To generate the classes and java files using the wsdl:
 wsimport -keep -s src -clientjar occpClient.jar http://104.236.81.197:8088/Ocpp15WebAppDemo/CentralSystemService?wsdl
 
  <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">"
                           +"<ns:chargeBoxIdentity>%s</ns:chargeBoxIdentity>"
                           +"<wsa:Action soap:mustUnderstand=\"1\">"
                           +"/Authorize"
                           +"</wsa:Action>"
                           +"<wsa:MessageID soap:mustUnderstand=\"1\">"
                           +"uuid:%s"
                           +"</wsa:MessageID>"
                           +"<wsa:To soap:mustUnderstand=\"1\">"
                           +"%s"
                           +"</wsa:To>"
                           +"</soap:Header>"