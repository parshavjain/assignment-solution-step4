package com.stackroute.datamunger.query;

//header class
public class Header {

	/*
	 * this class should contain a member variable which is a String array, to hold
	 * the headers.
	 */
	private String[] headers;

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	/**
	 * @return headers the headers to get
	 */
	public String[] getHeaders() {
		return this.headers;
	}
}
