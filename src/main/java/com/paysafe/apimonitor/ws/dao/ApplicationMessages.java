package com.paysafe.apimonitor.ws.dao;

public enum ApplicationMessages {
	// Messages related to monitoring Start
	MONITORING_STARTED(1001, "Monitoring Started Successfully"), ALREADY_MONITORING(1002,
			"Service is already being monitored"),

	// Messages related to monitoring Stop
	MONITORING_STOPPED(2001, "Monitoring stopped successfully"), ALREADY_STOPPED(2002,
			"Monitoring already stopped"), NO_MONITORING_HISTORY(2003, "No monitoring history found"),

	// HTTP Request Messages
	HEALTHY_SERVER(200, "Server is UP"), INVALID_DNS(9001, "Invalid DNS"),

	// Application Error Messages
	INVALID_INTERVAL(3001, "Inverval must be greater than Zero"), INTERNAL_SERVER_ERROR(500, "Internal Server Error");

	private final int id;
	private final String msg;

	ApplicationMessages(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public int getId() {
		return this.id;
	}

	public String getMsg() {
		return this.msg;
	}

}
