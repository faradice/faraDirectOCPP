package com.faradice.ocpp.central;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.faradice.faraUtil.FaraFiles;
import com.faradice.ocpp.entities.Authorize;

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
			System.out.println(soapInputContent);

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
		// AuthorizeResult aur = new AuthorizeResult(status, expiryDate, parentId)
		String res = "";
		res += "<?xml version='1.0' encoding='UTF-8'?>" + "<S:Envelope xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\">" + "<S:Header>" + "<Action xmlns=\"http://www.w3.org/2005/08/addressing\" xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\" S:mustUnderstand=\"false\">/AuthorizeResponse"
				+ "</Action>" + "<MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:b4bbf0c0-0598-428c-af75-6e83a81a74fd</MessageID>" + "<RelatesTo xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:f61ad85b-0745-4397-b0f2-e793f4ae5e18</RelatesTo>"
				+ "<To xmlns=\"http://www.w3.org/2005/08/addressing\">http://www.w3.org/2005/08/addressing/anonymous</To>" + "</S:Header>" + "<S:Body>" + "<authorizeResponse xmlns=\"urn://Ocpp/Cs/2015/10/\">" + "<idTagInfo>" + "<status>Accepted</status>"
				+ "<expiryDate>2018-02-05T14:54:20.107Z</expiryDate>" + "<parentIdTag>Parent</parentIdTag>" + "</idTagInfo>" + "</authorizeResponse>" + "</S:Body><" + "/S:Envelope>";
		return res;
	}
}
