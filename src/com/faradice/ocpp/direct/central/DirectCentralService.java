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
			System.out.println("pathInfo:" + servletPath);
			System.out.println("contextPath:" + contextPath);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/xml;charset=UTF-8");
			List<String> rows = FaraFiles.loadRows("wsdl/OCPP_CentralSystemService_1.6.wsdl");
			for (String row : rows) {
				response.getWriter().println(row);
			}
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
//			System.out.println(soapInputContent);

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
		Authorize au = Authorize.buildFromXML(soapAuthorizeReq);
		String status = AuthorizeResponse.ACCEPTED;
		GregorianCalendar gc = new GregorianCalendar();
		gc.roll(GregorianCalendar.DATE, 1);
		String expiryDate = FaraDates.xmlGregorian(gc).toString();
		AuthorizeResponse aur = new AuthorizeResponse(status, expiryDate, "ResaultParent", au.messageId);
		return aur.toXML();
	}
}
