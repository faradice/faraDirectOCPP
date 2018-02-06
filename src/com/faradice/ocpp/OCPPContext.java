package com.faradice.ocpp;

public class OCPPContext {
	private static OCPPSession session = null;
	
	public static final OCPPSession get() {
		if (session == null) {
			throw new RuntimeException("Default context missing for the session.  Use set method before calling get");
		}
		return session;
	}
	
	public static final void set(OCPPSession sc) {
		session = sc;
	}
}
