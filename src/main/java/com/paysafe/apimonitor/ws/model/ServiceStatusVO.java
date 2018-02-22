package com.paysafe.apimonitor.ws.model;

public class ServiceStatusVO {

	private String sTimeStamp;
	private String sStatus;
	private int sCode;
	private String message;

	public String getsTimeStamp() {
		return sTimeStamp;
	}

	public String getsStatus() {
		return sStatus;
	}

	public int getCode() {

		return sCode;
	}

	public String getMessage() {

		return message;
	}

	public ServiceStatusVO(String sTimeStamp, String sStatus, int code, String message) {
		this.sTimeStamp = sTimeStamp;
		this.sStatus = sStatus;
		this.message = message;
		this.sCode = code;
	}
}
