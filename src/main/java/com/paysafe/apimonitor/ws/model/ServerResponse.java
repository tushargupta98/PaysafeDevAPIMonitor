package com.paysafe.apimonitor.ws.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author tushar
 *
 */
public class ServerResponse {
	@JsonProperty
	String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
