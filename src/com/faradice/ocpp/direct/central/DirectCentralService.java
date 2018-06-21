package com.faradice.ocpp.direct.central;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.faradice.faraUtil.FaraDates;
import com.faradice.faraUtil.FaraFiles;
import com.faradice.ocpp.direct.entities.Authorize;
import com.faradice.ocpp.direct.entities.AuthorizeResponse;

public class DirectCentralService extends HttpServlet {

	public DirectCentralService() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String servletPath = request.getServletPath();
			String contextPath = request.getContextPath();
			String query = request.getQueryString();
			System.out.println("pathInfo:" + servletPath);
			System.out.println("contextPath:" + contextPath);
			System.out.println("Req URI:" + request.getRequestURL());
			System.out.println("Query:" + query);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/xml;charset=UTF-8");
			boolean isWSDLQuery = (servletPath.endsWith("CentralSystemService") && query.equalsIgnoreCase("wsdl"));
			String output = "";
			if (isWSDLQuery) {
				List<String> rows = FaraFiles.loadRows("wsdl/OCPP_CentralSystemService_1.6.wsdl");
				for (String row : rows) {
					response.getWriter().println(row);
					output+=row+"\n";
				}
			}
			System.out.println("GET RESULT:");
			int len = (output.length() > 100) ? 99 : output.length();
			System.out.println(output.substring(0, len)+"...");
		} catch (Exception ex) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			response.getWriter().close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			String soapInputContent = "";
			while ((line = reader.readLine()) != null) {
				soapInputContent += line;
			}
			System.out.println("Got POST:");
			System.out.println(soapInputContent);
			
			// Just create the Authorize response class for initial testing
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/soap+xml");
			response.getWriter().println(authorizeResponse(soapInputContent));
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			response.getWriter().close();
		}
	}

	String authorizeResponse(String soapAuthorizeReq) {
		System.out.println("Auhorize Request:");
		System.out.println(soapAuthorizeReq);
		Authorize au = Authorize.buildFromXML(soapAuthorizeReq);
		String status = AuthorizeResponse.ACCEPTED;
		GregorianCalendar gc = new GregorianCalendar();
		gc.roll(GregorianCalendar.DATE, 1);
		String expiryDate = FaraDates.xmlGregorian(gc).toString();
		AuthorizeResponse aur = new AuthorizeResponse(status, expiryDate, "ResaultParent", au.messageId);
		String s = aur.toXML();
		System.out.println("Result:");
		System.out.println(s);
		return s;
	}
}
