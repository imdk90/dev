package com.bayan.securityauth.model;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String access_token;
	private String token_type;
	private int expires_in;
	private int consented_on;
	private String scope;

	public AuthenticationResponse(String access_token, String token_type, int expires_in, int consented_on,
			String scope) {
		super();
		this.access_token = access_token;
		this.token_type = token_type;
		this.expires_in = expires_in;
		this.consented_on = consented_on;
		this.scope = scope;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public int getConsented_on() {
		return consented_on;
	}

	public void setConsented_on(int consented_on) {
		this.consented_on = consented_on;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAccess_token() {
		return access_token;
	}

}
