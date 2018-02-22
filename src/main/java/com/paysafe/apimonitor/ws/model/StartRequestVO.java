package com.paysafe.apimonitor.ws.model;

public class StartRequestVO {

	private String serviceURL;
	private long interval;
	public String getServiceURL() {
		return serviceURL;
	}
	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	
}
