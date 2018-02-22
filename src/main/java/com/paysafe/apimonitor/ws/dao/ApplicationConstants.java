package com.paysafe.apimonitor.ws.dao;

public final class ApplicationConstants {

	private ApplicationConstants() {
		
	}
	
	public static final String SERVICE_MONITOR_DAO = "com.paysafe.apimonitor.ws.dao.ServiceMonitorDAOImpl";
	public static final String SERVICE_STATUS_DAO = "com.paysafe.apimonitor.ws.dao.ServiceStatusDAOImpl";
	public static final int MILLISECONDS=1000;
	public static final String MONITORING_DOWN_MSG="DOWN";
	public static final String EMPTY_STRING="";
	public static final String SERVICE_READY="READY";
	public static final String MONITORING_UP_MSG="UP";
	public static final int HTTP_STATUS_OK_CODE=200;
	public static final String TIME_ZONE = "America/Montreal";
}
