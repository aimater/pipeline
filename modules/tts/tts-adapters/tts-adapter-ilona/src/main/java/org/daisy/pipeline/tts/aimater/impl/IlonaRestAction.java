package org.daisy.pipeline.tts.aimater.impl;

public enum IlonaRestAction {

	VOICES("GET","/voices"),
	SPEECH("POST","/synthesize");

	public String method;
	public String domain;

	/**
	 * @param method the HTTP method (usually GET or POST)
	 * @param domain the domain/endpoint of the requested action
	 */
	IlonaRestAction(String method, String domain) {
		this.method = method;
		this.domain = domain;
	}
}
